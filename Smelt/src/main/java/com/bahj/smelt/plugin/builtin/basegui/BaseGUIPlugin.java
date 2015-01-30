package com.bahj.smelt.plugin.builtin.basegui;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationPluginsConfiguredEvent;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIConstructionContext;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIConstructionContextImpl;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIExecutionContext;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIEvent;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializedEvent;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializingEvent;
import com.bahj.smelt.plugin.builtin.basegui.menu.SmeltBasicMenuItem;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.util.StrongReference;
import com.bahj.smelt.util.event.AbstractEventGenerator;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

/**
 * This plugin provides a base GUI for the Smelt framework. It featues a menu bar and an (initially empty) tab pane.
 * Menu items exist to open and close the Smelt specification as well as close the application.
 * <p/>
 * Other plugins may add content to the GUI by responding to the {@link BaseGUIInitializingEvent} that this plugin fires
 * in response to the global {@link SmeltApplicationPluginsConfiguredEvent}. That object is useful in two ways: first,
 * it allows the addition of menu items via the {@link GUIConstructionContext} contained in the event. Second, that
 * {@link GUIConstructionContext} contains a {@link StrongReference} to a {@link GUIExecutionContext} (which may only be
 * used <i>after</i> the {@link BaseGUIInitializedEvent} begins dispatch) which can be used to add tabs to the tab pane.
 * 
 * @author Zachary Palmer
 */
public class BaseGUIPlugin extends AbstractEventGenerator<BaseGUIEvent> implements SmeltPlugin {
    @Override
    public void registeredToApplicationModel(final SmeltApplicationModel model) {
        model.addListener(new TypedEventListener<>(SmeltApplicationPluginsConfiguredEvent.class,
                new EventListener<SmeltApplicationPluginsConfiguredEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationPluginsConfiguredEvent event) {
                        // Once the plugins are configured, initialize the GUI window.
                        GUIConstructionContextImpl guiContext = new GUIConstructionContextImpl();

                        // Add options to open and close Smelt descriptor files (triggering the parsing of the AST and
                        // the configuration of the application metastate.
                        guiContext.addMenuItemGroup("File",
                                Arrays.asList(new SmeltBasicMenuItem("Open Smelt Specification", new AbstractAction() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // TODO
                                    }
                                }), new SmeltBasicMenuItem("Close Smelt Specification", new AbstractAction() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // TODO
                                    }

                                    @Override
                                    public boolean isEnabled() {
                                        return model.isApplicationMetaStateLoaded();
                                    }

                                    // TODO: only enabled if a specification is open.
                                })));

                        // Tell all of the plugins that we're currently gathering contributions for the GUI. It's now or
                        // never for them.
                        fireEvent(new BaseGUIInitializingEvent(guiContext));

                        // Add the traditional "Exit" option to the end of the "File" menu.
                        guiContext.addMenuItemGroup("File",
                                Collections.singletonList(new SmeltBasicMenuItem("Exit", new AbstractAction() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // TODO: something a little more elegant than this - prompts and stuff
                                        System.exit(0);
                                    }
                                })));

                        // Now build the GUI.
                        BaseGUIFrame frame = new BaseGUIFrame(guiContext);
                        guiContext.getExecutionContextReference()
                                .setValue(new GUIExecutionContext(frame.getTabPanel()));

                        // Let everyone know the GUI's finished.
                        fireEvent(new BaseGUIInitializedEvent());

                        // And now show it.
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                }));
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        // This plugin claims no declarations.
        return false;
    }

    @Override
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes) {
        // This plugin should never receive any declarations to handle.
        if (declarationNodes.size() > 0) {
            throw new IllegalStateException("Declarations provided to base GUI plugin erroneously!");
        }
    }
}
