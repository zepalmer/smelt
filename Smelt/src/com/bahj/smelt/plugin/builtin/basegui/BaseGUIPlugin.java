package com.bahj.smelt.plugin.builtin.basegui;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationPluginsConfiguredEvent;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIConstructionContextImpl;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIExecutionContext;
import com.bahj.smelt.plugin.builtin.basegui.context.SmeltBasicMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIEvent;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializedEvent;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializingEvent;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.util.event.AbstractEventGenerator;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

public class BaseGUIPlugin extends AbstractEventGenerator<BaseGUIEvent> implements SmeltPlugin {
    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        model.addListener(new TypedEventListener<>(SmeltApplicationPluginsConfiguredEvent.class,
                new EventListener<SmeltApplicationPluginsConfiguredEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationPluginsConfiguredEvent event) {
                        // Once the plugins are configured, initialize the GUI window.
                        GUIConstructionContextImpl guiContext = new GUIConstructionContextImpl();

                        // Tell all of the plugins that we're currently gathering contributions for the GUI. It's now or
                        // never for them.
                        fireEvent(new BaseGUIInitializingEvent(guiContext));

                        // Add the traditional "Exit" option to the "File" menu.
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
