package com.bahj.smelt.plugin.builtin.editor;

import java.awt.CardLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIExecutionContext;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializingEvent;
import com.bahj.smelt.plugin.builtin.basegui.menu.SmeltBasicMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.tabs.DefaultGUITab;
import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITab;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseOpenedEvent;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

/**
 * A Smelt plugin which provides editing actions for the basic data model.
 * 
 * @author Zachary Palmer
 */
public class EditorPlugin implements SmeltPlugin {
    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        // Make sure that we add components to the GUI after the configuration is loaded.
        model.addListener(new TypedEventListener<>(SmeltApplicationConfigurationLoadedEvent.class,
                new EventListener<SmeltApplicationConfigurationLoadedEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationConfigurationLoadedEvent event) {
                        SmeltApplicationModel model = event.getApplicationModel();

                        DataModelPlugin dataModelPlugin = model.getPluginRegistry().getPlugin(DataModelPlugin.class);
                        BaseGUIPlugin guiPlugin = model.getPluginRegistry().getPlugin(BaseGUIPlugin.class);

                        // Create the editor and tab. We put a card panel between the tab and the editor panel so we
                        // can hide the editor when no database is loaded.
                        final EditorPanel editorPanel = new EditorPanel(guiPlugin.getBaseFrame(), dataModelPlugin);
                        final CardLayout cardLayout = new CardLayout(0, 0);
                        final JPanel containerPanel = new JPanel(cardLayout);
                        final String editorName = "editor";
                        final String noEditorName = "no-editor";
                        containerPanel.add(editorPanel, editorName);
                        containerPanel.add(new JLabel("No Database Loaded", SwingConstants.CENTER), noEditorName);
                        final GUITab tab = new DefaultGUITab(EditorPanel.Key.INSTANCE, "Database Editor",
                                containerPanel);
                        cardLayout.show(containerPanel, noEditorName);

                        dataModelPlugin.addListener(new TypedEventListener<>(DatabaseOpenedEvent.class,
                                new EventListener<DatabaseOpenedEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseOpenedEvent event) {
                                        cardLayout.show(containerPanel, editorName);
                                    }
                                }));
                        dataModelPlugin.addListener(new TypedEventListener<>(DatabaseClosedEvent.class,
                                new EventListener<DatabaseClosedEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseClosedEvent event) {
                                        cardLayout.show(containerPanel, noEditorName);
                                    }
                                }));

                        // Make sure we add the appropriate content to the Smelt GUI. This consists of the primary
                        // editor pane and a menu option to activate it.
                        guiPlugin.addListener(new TypedEventListener<>(BaseGUIInitializingEvent.class,
                                new EventListener<BaseGUIInitializingEvent>() {
                                    @Override
                                    public void eventOccurred(BaseGUIInitializingEvent event) {
                                        Action showEditorAction = event.getContext().constructExecutionAction(
                                                (GUIExecutionContext context) -> {
                                                    context.ensureTab(tab);
                                                });
                                        event.getContext().addMenuItemGroup("View",
                                                new SmeltBasicMenuItem("Editor", showEditorAction));
                                    }
                                }));
                    }
                }));
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        // This plugin must wait until the data model has been established before it can build a profile for its
        // editors.
        return Collections.singleton(DataModelPlugin.class);
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getRuntimeDependencyTypes() {
        // Clearly, this plugin requires the base data model plugin to work.  It also requires the base GUI.
        return new HashSet<>(Arrays.asList(BaseGUIPlugin.class, DataModelPlugin.class));
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes)
            throws DeclarationProcessingException {
        // TODO Auto-generated method stub

    }
}
