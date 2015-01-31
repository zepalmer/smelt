package com.bahj.smelt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bahj.smelt.configuration.ApplicationModelCreationException;
import com.bahj.smelt.configuration.Configuration;
import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationEvent;
import com.bahj.smelt.event.SmeltApplicationMetaStateInitializedEvent;
import com.bahj.smelt.event.SmeltApplicationMetaStateLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationMetaStateUnloadedEvent;
import com.bahj.smelt.event.SmeltApplicationPluginsConfiguredEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.ReadOnlySmeltPluginRegistryProxy;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.SmeltPluginRegistry;
import com.bahj.smelt.plugin.SmeltPluginRegistryImpl;
import com.bahj.smelt.syntax.SmeltParseFailureException;
import com.bahj.smelt.syntax.SmeltParser;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.DocumentNode;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.AbstractEventGenerator;
import com.bahj.smelt.util.partialorder.InconsistentPartialOrderException;
import com.bahj.smelt.util.partialorder.PartialOrderConstraint;
import com.bahj.smelt.util.partialorder.PartialOrderUtils;

/*
 * TODO: naming consistency: "application meta-state" should be called "specification"  
 */

/**
 * This class represents the overall state of the Smelt application. It includes state for both the UI and the data
 * model as well as the plugins which influence both.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationModel extends AbstractEventGenerator<SmeltApplicationEvent> {
    private SmeltPluginRegistryImpl pluginRegistry;
    private boolean applicationMetaStateLoaded;

    /**
     * Creates a new application model based on the provided configuration.
     * 
     * @param configuration
     *            The configuration to load.
     */
    public SmeltApplicationModel(Configuration configuration) throws ApplicationModelCreationException {
        this.pluginRegistry = new SmeltPluginRegistryImpl();
        this.applicationMetaStateLoaded = false;

        for (Class<? extends SmeltPlugin> pluginClass : configuration.getPlugins()) {
            instantiateAndRegisterPlugin(pluginClass);
        }

        // Check that all runtime dependencies are fulfilled.
        // TODO: consider just grabbing all of the dependent classes as well.
        // The only reason not to do that is to prevent confusion about why plugins are being loaded, and the GUI
        // could easily provide a mechanism for ascertaining that.
        for (SmeltPlugin plugin : this.pluginRegistry.asMap().values()) {
            for (Class<? extends SmeltPlugin> dependency : plugin.getRuntimeDependencyTypes()) {
                if (this.pluginRegistry.getPlugin(dependency) == null) {
                    throw new ApplicationModelCreationException("Plugin " + plugin.getClass() + " requires plugin "
                            + dependency.getClass() + " but the latter was not to be loaded.");
                }
            }
        }

        fireEvent(new SmeltApplicationConfigurationLoadedEvent(this, configuration));
        fireEvent(new SmeltApplicationPluginsConfiguredEvent(this));
    }

    private <T extends SmeltPlugin> void instantiateAndRegisterPlugin(Class<T> pluginClass)
            throws ApplicationModelCreationException {
        T plugin;
        try {
            plugin = pluginClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            this.pluginRegistry = null;
            throw new ApplicationModelCreationException(e);
        }
        this.pluginRegistry.registerPlugin(pluginClass, plugin);
        plugin.registeredToApplicationModel(this);
    }

    /**
     * Loads an application metadata state from a file. This function simply parses the contents of the file (expecting
     * a Smelt specifciation) and calls {@link #loadApplicationMetaState(DocumentNode)} with the result. Any errors
     * encountered during reading and parsing are reported.
     */
    public void loadApplicationMetaState(File file) throws IOException, SmeltParseFailureException {
        DocumentNode node;
        try (FileInputStream fis = new FileInputStream(file)) {
            SmeltParser parser = new SmeltParser(file.getPath());
            node = parser.parse(fis);
        }
        loadApplicationMetaState(node);
    }

    /**
     * Loads an application metadata state (unloading any previous one if it exists). This creates fresh UI and logical
     * models for the Smelt application and initializes them using the provided Smelt document.
     * 
     * @param document
     *            The document AST to use.
     */
    public void loadApplicationMetaState(DocumentNode document) {
        if (this.applicationMetaStateLoaded) {
            unloadApplicationMetaState();
        }

        try {
            this.applicationMetaStateLoaded = true;

            // TODO: initialize UI model
            fireEvent(new SmeltApplicationMetaStateInitializedEvent(this));

            // Determine an order in which to process declarations.
            Set<PartialOrderConstraint<SmeltPlugin>> constraints = new HashSet<>();
            for (SmeltPlugin plugin : this.pluginRegistry.asMap().values()) {
                Set<Class<? extends SmeltPlugin>> dependencies = plugin.getDeclarationDependencyTypes();
                for (Class<? extends SmeltPlugin> dependency : dependencies) {
                    SmeltPlugin dependencyPlugin = this.pluginRegistry.getPlugin(dependency);
                    constraints.add(new PartialOrderConstraint<SmeltPlugin>(dependencyPlugin, plugin));
                }
            }
            List<SmeltPlugin> pluginProcessingOrder;
            try {
                pluginProcessingOrder = PartialOrderUtils.orderByConstraints(this.pluginRegistry.asMap().values(),
                        constraints,
                        (SmeltPlugin a, SmeltPlugin b) -> a.getClass().getName().compareTo(b.getClass().getName()));
            } catch (InconsistentPartialOrderException e) {
                // TODO: error handling: the plugins declare a cyclic dependency!
                throw new NotYetImplementedException();
            }

            // For each node in the document, determine how it is to be handled.
            // TODO: consider: can this logic be abstracted and reused when we start handling the extra attributes of
            // data declarations (e.g. the LaTeX plugin)?
            SmeltPluginDeclarationHandlerContext context = new SmeltPluginDeclarationHandlerContext(this);
            Map<SmeltPlugin, Set<DeclarationNode>> declarationMapping = new HashMap<>();
            for (DeclarationNode declarationNode : document.getDeclarations()) {
                Set<SmeltPlugin> claims = new HashSet<>();
                for (SmeltPlugin plugin : this.pluginRegistry.asMap().values()) {
                    if (plugin.claimsDeclaration(context, declarationNode)) {
                        claims.add(plugin);
                    }
                }
                if (claims.size() == 0) {
                    // None of the plugins wanted this declaration!
                    // TODO: error
                    throw new NotYetImplementedException();
                } else if (claims.size() > 1) {
                    // Multiple plugins wanted this declaration! This isn't okay because the claim predicate above
                    // implies
                    // exclusive ownership. (If a plugin wanted to snoop on a declaration, this framework needs to be
                    // extended to have an "it's not mine but I'd like to look at it" sort of response.)
                    // TODO: error
                    throw new NotYetImplementedException();
                } else {
                    SmeltPlugin plugin = claims.iterator().next();
                    Set<DeclarationNode> declarationNodes = declarationMapping.get(plugin);
                    if (declarationNodes == null) {
                        declarationNodes = new HashSet<>();
                        declarationMapping.put(plugin, declarationNodes);
                    }
                    declarationNodes.add(declarationNode);
                }
            }

            // Now that we have the node mapping, process all of the plugins.
            // TODO: rather than fail on the first complaint, gather up everything that goes wrong and report it
            // in particular, this requires that we be able to inspect the partial order so we don't process plugins
            // which depend on the ones that have already failed
            // this would require a refactoring of the partial order code so that a finite partial order can be
            // declared and then the order can be used to arbitrarily order the dependencies
            // it would also be a good idea to provide the plugins with some kind of framework, possibly through the
            // SmeltPluginDeclarationHandlerContext, by which they could produce non-stopping errors (which will
            // fail the plugin processing but not the immediate control flow like exceptions do) and perhaps even
            // warnings
            for (SmeltPlugin plugin : pluginProcessingOrder) {
                try {
                    plugin.processDeclarations(context, declarationMapping.getOrDefault(plugin, Collections.emptySet()));
                } catch (DeclarationProcessingException e) {
                    throw new NotYetImplementedException(e);
                }
            }

            fireEvent(new SmeltApplicationMetaStateLoadedEvent(this));
        } catch (Throwable t) {
            unloadApplicationMetaState();
            throw t;
        }
    }

    /**
     * Unloads the current application metadata state. This clears the current logical and UI models.
     */
    public void unloadApplicationMetaState() {
        this.applicationMetaStateLoaded = false;
        fireEvent(new SmeltApplicationMetaStateUnloadedEvent(this));
    }

    public boolean isApplicationMetaStateLoaded() {
        return this.applicationMetaStateLoaded;
    }

    public SmeltPluginRegistry getPluginRegistry() {
        return new ReadOnlySmeltPluginRegistryProxy(pluginRegistry);
    }

}
