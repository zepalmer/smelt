package com.bahj.smelt.plugin.builtin.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationSpecificationInitializedEvent;
import com.bahj.smelt.event.SmeltApplicationSpecificationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationSpecificationUnloadedEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIExecutionContext;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializingEvent;
import com.bahj.smelt.plugin.builtin.basegui.menu.SmeltBasicMenuItem;
import com.bahj.smelt.plugin.builtin.data.event.DataModelEvent;
import com.bahj.smelt.plugin.builtin.data.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.event.DatabaseOpenedEvent;
import com.bahj.smelt.plugin.builtin.data.model.DuplicateTypeNameException;
import com.bahj.smelt.plugin.builtin.data.model.SmeltDataModel;
import com.bahj.smelt.plugin.builtin.data.type.DataType;
import com.bahj.smelt.plugin.builtin.data.type.SmeltType;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.MessageNode;
import com.bahj.smelt.syntax.ast.decoration.DeclarationNodeDecorator;
import com.bahj.smelt.syntax.ast.decoration.DecoratorNodeContext;
import com.bahj.smelt.syntax.ast.decoration.MessageNodeDecorator;
import com.bahj.smelt.syntax.ast.impl.MessageNodeImpl;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.AbstractEventGenerator;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

public class DataModelPlugin extends AbstractEventGenerator<DataModelEvent> implements SmeltPlugin {
    /** The model for this plugin. */
    private SmeltDataModel model = null;

    // TODO: field for the database

    @Override
    public void registeredToApplicationModel(final SmeltApplicationModel model) {
        // Make sure that we add components to the GUI after the configuration is loaded.
        model.addListener(new TypedEventListener<>(SmeltApplicationConfigurationLoadedEvent.class,
                new EventListener<SmeltApplicationConfigurationLoadedEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationConfigurationLoadedEvent event) {
                        // Make sure we add the appropriate content to the Smelt GUI. This consists of the menu
                        // items necessary to activate the opening and closing of databases.
                        SmeltApplicationModel model = event.getApplicationModel();
                        BaseGUIPlugin guiPlugin = model.getPluginRegistry().getPlugin(BaseGUIPlugin.class);
                        guiPlugin.addListener(new TypedEventListener<>(BaseGUIInitializingEvent.class,
                                new EventListener<BaseGUIInitializingEvent>() {
                                    @Override
                                    public void eventOccurred(BaseGUIInitializingEvent event) {
                                        final Action newDatabaseAction = event.getContext().constructExecutionAction(
                                                (GUIExecutionContext context) -> {
                                                    // TODO: create new in-memory database
                                                });
                                        final Action openDatabaseAction = event.getContext().constructExecutionAction(
                                                (GUIExecutionContext context) -> {
                                                    // TODO: present GUI, then open file
                                                });
                                        final Action saveDatabaseAction = event.getContext().constructExecutionAction(
                                                (GUIExecutionContext context) -> {
                                                    // TODO: save file to known filename (or present GUI if necessary)
                                                });
                                        final Action saveDatabaseAsAction = event.getContext()
                                                .constructExecutionAction((GUIExecutionContext context) -> {
                                                    // TODO: present GUI, then save file
                                                    });
                                        final Action closeDatabaseAction = event.getContext().constructExecutionAction(
                                                (GUIExecutionContext context) -> {
                                                    // TODO: close file, probably with confirmation dialogs
                                                });

                                        newDatabaseAction.setEnabled(false);
                                        openDatabaseAction.setEnabled(false);
                                        saveDatabaseAction.setEnabled(false);
                                        saveDatabaseAsAction.setEnabled(false);
                                        closeDatabaseAction.setEnabled(false);

                                        event.getContext().addMenuItemGroup("File",
                                                new SmeltBasicMenuItem("New Database", newDatabaseAction),
                                                new SmeltBasicMenuItem("Open Database", openDatabaseAction),
                                                new SmeltBasicMenuItem("Save Database", saveDatabaseAction),
                                                new SmeltBasicMenuItem("Save Database As...", saveDatabaseAsAction),
                                                new SmeltBasicMenuItem("Close Database", closeDatabaseAction));

                                        // When the specification is loaded, databases can be opened.
                                        model.addListener(new TypedEventListener<>(
                                                SmeltApplicationSpecificationLoadedEvent.class,
                                                new EventListener<SmeltApplicationSpecificationLoadedEvent>() {
                                                    @Override
                                                    public void eventOccurred(
                                                            SmeltApplicationSpecificationLoadedEvent event) {
                                                        newDatabaseAction.setEnabled(true);
                                                        openDatabaseAction.setEnabled(true);
                                                    }
                                                }));

                                        // When a database is opened, it can be saved or closed.
                                        DataModelPlugin.this.addListener(new TypedEventListener<>(
                                                DatabaseOpenedEvent.class, new EventListener<DatabaseOpenedEvent>() {
                                                    @Override
                                                    public void eventOccurred(DatabaseOpenedEvent event) {
                                                        saveDatabaseAction.setEnabled(true);
                                                        saveDatabaseAsAction.setEnabled(true);
                                                        closeDatabaseAction.setEnabled(true);
                                                    }
                                                }));

                                        // When the database is closed, it can no longer be saved or closed.
                                        DataModelPlugin.this.addListener(new TypedEventListener<>(
                                                DatabaseClosedEvent.class, new EventListener<DatabaseClosedEvent>() {
                                                    @Override
                                                    public void eventOccurred(DatabaseClosedEvent event) {
                                                        saveDatabaseAction.setEnabled(false);
                                                        saveDatabaseAsAction.setEnabled(false);
                                                        closeDatabaseAction.setEnabled(false);
                                                    }
                                                }));

                                        // When the specification is unloaded, databases can no longer be opened.
                                        model.addListener(new TypedEventListener<>(
                                                SmeltApplicationSpecificationUnloadedEvent.class,
                                                new EventListener<SmeltApplicationSpecificationUnloadedEvent>() {
                                                    @Override
                                                    public void eventOccurred(
                                                            SmeltApplicationSpecificationUnloadedEvent event) {
                                                        newDatabaseAction.setEnabled(false);
                                                        openDatabaseAction.setEnabled(false);
                                                    }
                                                }));
                                    }
                                }));
                    }
                }));

        // Make sure we initialized the Smelt data model whenever the application specification is initialized.
        model.addListener(new TypedEventListener<>(SmeltApplicationSpecificationInitializedEvent.class, (
                SmeltApplicationSpecificationInitializedEvent event) -> {
            DataModelPlugin.this.model = new SmeltDataModel();
        }));

        // If the application specification is unloaded, we dump the database and the model.
        model.addListener(new TypedEventListener<>(SmeltApplicationSpecificationUnloadedEvent.class, (
                SmeltApplicationSpecificationUnloadedEvent event) -> {
            // TODO: wipe database as well
                DataModelPlugin.this.model = null;
            }));
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        // While the data declaration plugin does depend on the GUI plugin for GUI layout, it does not have any
        // dependency in terms of declaration handling order.
        return Collections.emptySet();
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getRuntimeDependencyTypes() {
        return Collections.singleton(BaseGUIPlugin.class);
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        if (declarationNode instanceof MessageNodeImpl) {
            MessageNode messageNode = (MessageNode) declarationNode;
            String name = messageNode.getHeader().getName();
            return name.equals("data"); // TODO: handle enum types as well
        } else {
            return false;
        }
    }

    @Override
    public void processDeclarations(final SmeltPluginDeclarationHandlerContext context,
            Set<DeclarationNode> declarationNodes) throws DeclarationProcessingException {
        // This process is sort of non-trivial. We want to allow the declarations to be provided unordered, but there
        // must be a DAG of dependencies in their definitions. All data types are embedded -- there is no indirection
        // -- so no cyclic types need be created, but we might need to initialize a new data type during the middle of
        // initializing another. We accomplish this by keeping a dictionary of unhandled declarations and gradually
        // (and statefully) consuming them, raising the appropriate exceptions in the case of a problem.
        Map<String, MessageNodeDecorator> declarationsByName = new HashMap<>();
        DecoratorNodeContext decoratorNodeContext = new DecoratorNodeContext(context, this);
        for (DeclarationNode declarationNode : declarationNodes) {
            if (declarationNode instanceof MessageNode) {
                MessageNodeDecorator messageNode = new MessageNodeDecorator((MessageNode) declarationNode,
                        decoratorNodeContext);
                messageNode.getHeader().insistNoNamedArguments();
                String name = messageNode.getHeader().insistSinglePositionalArgument("name").insistSingleComponent();
                if (declarationsByName.containsKey(name)) {
                    // TODO: error: duplicate data declaration
                    throw new NotYetImplementedException();
                }
                declarationsByName.put(name, messageNode);
            } else {
                throw new IllegalStateException(
                        "Non-MessageNode provided to DataDeclarationPlugin; should've been excluded!");
            }
        }

        class DeclarationProcessor {
            private Set<String> consumedNames = new HashSet<>();
            private Set<String> processedNames = new HashSet<>();
            private Map<String, MessageNodeDecorator> declarations = declarationsByName;

            public void processDeclaration(String name) throws DeclarationProcessingException {
                if (processedNames.contains(name)) {
                    // Then the name was already processed; ignore this call.
                    return;
                } else if (consumedNames.contains(name)) {
                    // Then there is a cyclic dependency!
                    // TODO: appropriate error
                    throw new NotYetImplementedException();
                } else {
                    // If the name doesn't exist, fail; there's no declaration for it.
                    if (declarations.get(name) == null) {
                        // TODO: appropriate error
                        throw new NotYetImplementedException();
                    }

                    // Mark that we are consuming this name.
                    consumedNames.add(name);

                    // Process the declaration for this name.
                    // TODO: handle enum types as well
                    DataType dataType = constructDataTypeFromDeclaration(name, declarations.get(name));
                    try {
                        model.addType(dataType);
                    } catch (DuplicateTypeNameException e) {
                        // TODO: handle this exception
                        throw new NotYetImplementedException(e);
                    }

                    // Finally, indicate that we have processed this declaration.
                    processedNames.add(name);
                }
            }

            private DataType constructDataTypeFromDeclaration(String typeName, MessageNodeDecorator messageNode)
                    throws DeclarationProcessingException {
                Map<String, SmeltType<?>> fields = new HashMap<>();
                for (DeclarationNodeDecorator<?> node : messageNode.getChildren()) {
                    if (node instanceof MessageNodeDecorator) {
                        MessageNodeDecorator fieldMessage = (MessageNodeDecorator) node;
                        fieldMessage.getHeader().insistNoNamedArguments();
                        fieldMessage.insistNoChildren();
                        SmeltType<?> fieldType = getTypeForTypeName(fieldMessage.getHeader()
                                .insistSinglePositionalArgument("field name").insistSingleComponent());
                        String fieldName = fieldMessage.getHeader().getName();
                        fields.put(fieldName, fieldType);
                    } else {
                        // TODO: report syntax error via appropriate exception
                        throw new NotYetImplementedException();
                    }
                }
                return new DataType(typeName, fields);
            }

            private SmeltType<?> getTypeForTypeName(String name) throws DeclarationProcessingException {
                // First try to look it up in the model.
                SmeltType<?> type = model.getTypes().get(name);
                if (type == null) {
                    // If that fails, let's try to process that name and then see if we can find anything.
                    processDeclaration(name);
                    type = model.getTypes().get(name);
                }
                if (type == null) {
                    // This case should be impossible; either processDeclaration ensures the name is in the types of the
                    // data model or it fails with an exception.
                    throw new IllegalStateException("Unable to handle request for type of name " + name);
                }
                return type;
            }
        }

        // As long as we feed each name in turn to the declaration processor, they will all be processed correctly.
        // (They might not be processed in the same order that we report them based on their dependencies, but they will
        // all be processed.)
        DeclarationProcessor processor = new DeclarationProcessor();
        for (String name : declarationsByName.keySet()) {
            processor.processDeclaration(name);
        }
    }
}
