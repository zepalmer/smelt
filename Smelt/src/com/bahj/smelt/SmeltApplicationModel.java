package com.bahj.smelt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationConfigurationUnloadedEvent;
import com.bahj.smelt.event.SmeltApplicationEvent;
import com.bahj.smelt.event.SmeltApplicationMetaStateInitializedEvent;
import com.bahj.smelt.event.SmeltApplicationMetaStateLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationMetaStateUnloadedEvent;
import com.bahj.smelt.model.Configuration;
import com.bahj.smelt.model.ModelCreationException;
import com.bahj.smelt.model.SmeltLogicalModel;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.SmeltPluginRegistry;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.DocumentNode;
import com.bahj.smelt.ui.SmeltUIModel;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.EventGenerator;
import com.bahj.smelt.util.event.EventListener;

/**
 * This class represents the overall state of the Smelt application. It includes state for both the UI and the data
 * model as well as the plugins which influence both.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationModel implements EventGenerator<SmeltApplicationEvent> {
    private SmeltPluginRegistry pluginRegistry;
    private SmeltLogicalModel logicalModel;
    private SmeltUIModel uiModel;

    private Set<EventListener<? super SmeltApplicationEvent>> listeners;

    public SmeltApplicationModel() {
        this.listeners = new HashSet<>();

        this.pluginRegistry = null;
        this.logicalModel = null;
        this.uiModel = null;
    }

    /**
     * Loads the provided Smelt configuration. If a model or a database are loaded, they will be unloaded first (with
     * the appropriate associated events).
     * 
     * @param configuration
     */
    public void loadConfiguration(Configuration configuration) throws ModelCreationException {
        if (this.pluginRegistry != null) {
            unloadConfiguration();
        }

        this.pluginRegistry = new SmeltPluginRegistry();
        for (Class<? extends SmeltPlugin> pluginClass : configuration.getPlugins()) {
            instantiateAndRegisterPlugin(pluginClass);
        }
        fireEvent(new SmeltApplicationConfigurationLoadedEvent(this, configuration));
    }

    private <T extends SmeltPlugin> void instantiateAndRegisterPlugin(Class<T> pluginClass)
            throws ModelCreationException {
        T plugin;
        try {
            plugin = pluginClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            this.pluginRegistry = null;
            throw new ModelCreationException(e);
        }
        this.pluginRegistry.registerPlugin(pluginClass, plugin);
        plugin.registeredToApplicationModel(this);
    }

    /**
     * Unloads the current configuration. If logical and UI models are loaded, they will be unloaded first.
     */
    public void unloadConfiguration() {
        unloadApplicationMetaState();
        for (SmeltPlugin plugin : this.pluginRegistry.asMap().values()) {
            plugin.unregisteredFromApplicationModel(this);
        }
        this.pluginRegistry = null;
        fireEvent(new SmeltApplicationConfigurationUnloadedEvent(this));
    }

    /**
     * Loads an application metadata state (unloading any previous one if it exists). This creates fresh UI and logical
     * models for the Smelt application and initializes them using the provided Smelt document.
     * 
     * @param document
     *            The document AST to use.
     */
    public void loadApplicationMetaState(DocumentNode document) {
        unloadApplicationMetaState();

        // Initialize the model objects
        this.logicalModel = new SmeltLogicalModel();
        // TODO: initialize UI model
        fireEvent(new SmeltApplicationMetaStateInitializedEvent(this));

        // For each node in the document, determine how it is to be handled.
        // TODO: consider: can this logic be abstracted and reused when we start handling the extra attributes of data
        //                 declarations (e.g. the LaTeX plugin)?
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
                // Multiple plugins wanted this declaration!  This isn't okay because the claim predicate above implies
                // exclusive ownership.  (If a plugin wanted to snoop on a declaration, this framework needs to be
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
        // TODO: enforce some kind of dependency DAG
        for (Map.Entry<SmeltPlugin, Set<DeclarationNode>> entry : declarationMapping.entrySet()) {
            entry.getKey().processDeclarations(context, entry.getValue());
        }
        
        fireEvent(new SmeltApplicationMetaStateLoadedEvent(this));
    }

    /**
     * Unloads the current application metadata state. This clears the current logical and UI models.
     */
    public void unloadApplicationMetaState() {
        if (this.uiModel != null || this.logicalModel != null) {
            this.logicalModel.unloadDatabase();            
            this.logicalModel = null;
            this.uiModel = null;
            fireEvent(new SmeltApplicationMetaStateUnloadedEvent(this));
        }
    }
    
    /**
     * Retrieves the logical model for this application.
     * @return The present logical model for this application or <code>null</code> if no application meta-state is
     * loaded.
     */
    public SmeltLogicalModel getLogicalModel() {
        return this.logicalModel;
    }
    
    /**
     * Retrieves the UI model for this application.
     * @return The present UI model for this application or <code>null</code> if no application meta-data is loaded.
     */
    public SmeltUIModel getUIModel() {
        return this.uiModel;
    }

    @Override
    public void addListener(EventListener<? super SmeltApplicationEvent> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener<? super SmeltApplicationEvent> listener) {
        this.listeners.remove(listener);
    }

    protected void fireEvent(SmeltApplicationEvent event) {
        for (EventListener<? super SmeltApplicationEvent> listener : listeners) {
            listener.eventOccurred(event);
        }
    }
}
