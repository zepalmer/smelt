package com.bahj.smelt.plugin.builtin.viewbytype;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.SmeltPluginRegistry;
import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltBasicMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializingEvent;
import com.bahj.smelt.plugin.builtin.basegui.execution.BasicGUIPosition;
import com.bahj.smelt.plugin.builtin.basegui.execution.DockableIdentifier;
import com.bahj.smelt.plugin.builtin.basegui.execution.GUIExecutionContext;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.editor.EditorModel;
import com.bahj.smelt.plugin.builtin.editor.EditorPlugin;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

public class ViewByTypePlugin implements SmeltPlugin {
    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        model.addListener(new TypedEventListener<>(SmeltApplicationConfigurationLoadedEvent.class,
                new EventListener<SmeltApplicationConfigurationLoadedEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationConfigurationLoadedEvent event) {
                        SmeltPluginRegistry pluginRegistry = event.getApplicationModel().getPluginRegistry();

                        // Add menu option to GUI to present the view on request.
                        DataModelPlugin dataModelPlugin = pluginRegistry.getPlugin(DataModelPlugin.class);
                        BaseGUIPlugin guiPlugin = pluginRegistry.getPlugin(BaseGUIPlugin.class);
                        EditorPlugin editorPlugin = pluginRegistry.getPlugin(EditorPlugin.class);
                        guiPlugin.addListener(new TypedEventListener<>(BaseGUIInitializingEvent.class,
                                new EventListener<BaseGUIInitializingEvent>() {
                                    @Override
                                    public void eventOccurred(BaseGUIInitializingEvent event) {
                                        event.getContext().addMenuItemGroup(
                                                "View",
                                                new SmeltBasicMenuItem("Objects by Type", event.getContext()
                                                        .constructExecutionAction(
                                                                (GUIExecutionContext context) -> {
                                                                    ViewByTypePlugin.this.presentObjectsByTypeView(
                                                                            dataModelPlugin,
                                                                            editorPlugin.getEditorModel(), context);
                                                                })));
                                    }
                                }));
                    }
                }));
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getRuntimeDependencyTypes() {
        return new HashSet<>(Arrays.asList(DataModelPlugin.class, BaseGUIPlugin.class, EditorPlugin.class));
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        return false;
    }

    @Override
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes)
            throws DeclarationProcessingException {
        if (declarationNodes.size() > 0) {
            throw new IllegalStateException(
                    "This plugin should not have been asked to process any declarations: it claims none!");
        }
    }

    private void presentObjectsByTypeView(DataModelPlugin dataModelPlugin, EditorModel editorModel,
            GUIExecutionContext context) {
        context.createOrDisplay(ObjectsByTypeViewIdentifier.INSTANCE, BasicGUIPosition.LEFT, () -> {
            DefaultMultipleCDockable dockable = new DefaultMultipleCDockable(null);
            dockable.setTitleText("View by Type");
            dockable.setCloseable(true);
            dockable.setExternalizable(false); // TODO: why is this feature broken?
                DatabaseTreeViewByTypePanel treeViewByTypePanel = new DatabaseTreeViewByTypePanel(dataModelPlugin,
                        editorModel);
                dockable.add(treeViewByTypePanel);
                return dockable;
            });
    }

    private static enum ObjectsByTypeViewIdentifier implements DockableIdentifier {
        INSTANCE
    }
}
