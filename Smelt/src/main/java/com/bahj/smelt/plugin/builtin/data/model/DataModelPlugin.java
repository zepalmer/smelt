package com.bahj.smelt.plugin.builtin.data.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationSpecificationInitializedEvent;
import com.bahj.smelt.event.SmeltApplicationSpecificationUnloadedEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;
import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabaseSerializationStrategy;
import com.bahj.smelt.plugin.builtin.data.model.database.event.DatabaseEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DataModelPluginEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseOpenedEvent;
import com.bahj.smelt.plugin.builtin.data.model.model.DuplicateTypeNameException;
import com.bahj.smelt.plugin.builtin.data.model.model.SmeltDataModel;
import com.bahj.smelt.plugin.builtin.data.model.type.DataType;
import com.bahj.smelt.plugin.builtin.data.model.type.EnumType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.type.TextType;
import com.bahj.smelt.plugin.builtin.data.model.value.serialization.SmeltDatumSerializationStrategy;
import com.bahj.smelt.plugin.builtin.data.model.value.serialization.SmeltEnumValueSerializationStrategy;
import com.bahj.smelt.plugin.builtin.data.model.value.serialization.SmeltTextSerializationStrategy;
import com.bahj.smelt.plugin.builtin.data.model.value.serialization.SmeltValueSerializationStrategyRegistry;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.serialization.SerializationUtils;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.decoration.DeclarationNodeDecorator;
import com.bahj.smelt.syntax.ast.decoration.DecoratorNodeContext;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.AbstractEventGenerator;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

public class DataModelPlugin extends AbstractEventGenerator<DataModelPluginEvent> implements SmeltPlugin {
    /** The model for this plugin. */
    private SmeltDataModel model = null;
    /** The database for this plugin. */
    private SmeltDatabase database = null;
    /** The relay listener we attach to the database. */
    private EventListener<DatabaseEvent> relayListener = new DatabaseEventRelayListener();

    @Override
    public void registeredToApplicationModel(final SmeltApplicationModel model) {
        // Make sure we initialized the Smelt data model whenever the application specification is initialized.
        model.addListener(new TypedEventListener<>(SmeltApplicationSpecificationInitializedEvent.class, (
                SmeltApplicationSpecificationInitializedEvent event) -> {
            DataModelPlugin.this.model = new SmeltDataModel();
        }));

        // If the application specification is unloaded, we dump the database and the model.
        model.addListener(new TypedEventListener<>(SmeltApplicationSpecificationUnloadedEvent.class, (
                SmeltApplicationSpecificationUnloadedEvent event) -> {
            setDatabase(null);
            DataModelPlugin.this.model = null;
            return;
        }));
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getRuntimeDependencyTypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        if (declarationNode instanceof DeclarationNode) {
            DeclarationNode messageNode = (DeclarationNode) declarationNode;
            String name = messageNode.getHeader().getName();
            return name.equals("data") || name.equals("enum"); // TODO: handle enum types as well
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
        Map<String, DeclarationNodeDecorator> declarationsByName = new HashMap<>();
        DecoratorNodeContext decoratorNodeContext = new DecoratorNodeContext(context, this);
        for (DeclarationNode declarationNode : declarationNodes) {
            if (declarationNode instanceof DeclarationNode) {
                DeclarationNodeDecorator messageNode = new DeclarationNodeDecorator((DeclarationNode) declarationNode,
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
            private Map<String, DeclarationNodeDecorator> declarations = declarationsByName;

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
                    DeclarationNodeDecorator node = declarations.get(name);
                    switch (node.getHeader().getName()) {
                        case "data":
                            try {
                                model.addType(constructDataTypeFromDeclaration(name, node));
                            } catch (DuplicateTypeNameException e) {
                                // TODO: handle this exception
                                throw new NotYetImplementedException(e);
                            }
                            break;
                        case "enum":
                            try {
                                model.addType(constructEnumTypeFromDeclaration(name, node));
                            } catch (DuplicateTypeNameException e) {
                                // TODO: handle this exception
                                throw new NotYetImplementedException(e);
                            }
                            break;
                        default:
                            throw new IllegalStateException("Received unexpected node for processing ("
                                    + node.getHeader().getName() + "): " + node);
                    }

                    // Finally, indicate that we have processed this declaration.
                    processedNames.add(name);
                }
            }

            private DataType constructDataTypeFromDeclaration(String typeName, DeclarationNodeDecorator messageNode)
                    throws DeclarationProcessingException {
                // Sanity checks on the outer node.
                messageNode.getHeader().insistSinglePositionalArgument("data type name");
                messageNode.getHeader().insistNoNamedArguments();

                // For each child, assemble a field.
                Map<String, SmeltType<?, ?>> fields = new HashMap<>();
                String firstTextFieldName = null;
                for (DeclarationNodeDecorator fieldNode : messageNode.getChildren()) {
                    fieldNode.getHeader().insistNoNamedArguments();
                    fieldNode.insistNoChildren();
                    SmeltType<?, ?> fieldType = getTypeForTypeName(fieldNode.getHeader()
                            .insistSinglePositionalArgument("field name").insistSingleComponent());
                    String fieldName = fieldNode.getHeader().getName();
                    if (firstTextFieldName == null && fieldType.equals(TextType.INSTANCE)) {
                        firstTextFieldName = fieldName;
                    }
                    fields.put(fieldName, fieldType);
                }
                return new DataType(typeName, fields, firstTextFieldName);
            }

            private EnumType constructEnumTypeFromDeclaration(String typeName, DeclarationNodeDecorator declarationNode)
                    throws DeclarationProcessingException {
                // Sanity checks.
                declarationNode.getHeader().insistSinglePositionalArgument("enumeration name");
                declarationNode.getHeader().insistNoNamedArguments();

                // For each child, add an option.
                List<String> choices = new ArrayList<>();
                for (DeclarationNodeDecorator child : declarationNode.getChildren()) {
                    child.insistNoChildren();
                    child.getHeader().insistNoPositionalArguments();
                    child.getHeader().insistNoNamedArguments();
                    choices.add(child.getHeader().getName());
                }

                // Now build the type.
                return new EnumType(typeName, choices);
            }

            private SmeltType<?, ?> getTypeForTypeName(String name) throws DeclarationProcessingException {
                // First try to look it up in the model.
                SmeltType<?, ?> type = model.getTypes().get(name);
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

    /**
     * Retrieves the Smelt data model currently in use by this plugin.
     * 
     * @return The data model in use (or <code>null</code> if no data model exists).
     */
    public SmeltDataModel getModel() {
        return model;
    }

    /**
     * Retrieves the Smelt database currently in use by this plugin.
     * 
     * @return The database in use (or <code>null</code> if no database exists).
     */
    public SmeltDatabase getDatabase() {
        return database;
    }

    /**
     * Changes the database in use by this plugin.
     * 
     * @param database
     *            The database to use (or <code>null</code> simply to close any existing database).
     * @throws IllegalStateException
     *             If no data model is loaded.
     */
    public void setDatabase(SmeltDatabase database) {
        if (this.model == null) {
            throw new IllegalStateException("Cannot modify database in use when no data model exists!");
        }
        if (this.database != null) {
            this.database.removeListener(this.relayListener);
            this.database = null;
            fireEvent(new DatabaseClosedEvent());
        }
        if (database != null) {
            this.database = database;
            this.database.addListener(this.relayListener);
            fireEvent(new DatabaseOpenedEvent());
        }
    }

    private class DatabaseEventRelayListener implements EventListener<DatabaseEvent> {
        @Override
        public void eventOccurred(DatabaseEvent event) {
            fireEvent(event);
        }
    }

    public void openDatabase(File selectedFile) throws IOException, DeserializationException {
        SmeltDatabaseSerializationStrategy strategy = createDatabaseSerializationStrategy();
        SmeltDatabase database = SerializationUtils.readFile(selectedFile, strategy);
        setDatabase(database);
    }

    public void saveDatabase(File selectedFile) throws IOException, SerializationException {
        SmeltDatabaseSerializationStrategy strategy = createDatabaseSerializationStrategy();
        SerializationUtils.writeFileSafely(selectedFile, strategy, this.database);
    }

    private SmeltDatabaseSerializationStrategy createDatabaseSerializationStrategy() {
        SmeltValueSerializationStrategyRegistry registry = new SmeltValueSerializationStrategyRegistry();
        SmeltDatabaseSerializationStrategy databaseSerializationStrategy = new SmeltDatabaseSerializationStrategy(
                registry);

        // Add value serialization strategies to the registry.
        registry.registerSerializationStrategy(new SmeltTextSerializationStrategy());
        registry.registerSerializationStrategy(new SmeltEnumValueSerializationStrategy(this.model));
        registry.registerSerializationStrategy(new SmeltDatumSerializationStrategy(this.model, registry));

        return databaseSerializationStrategy;
    }
}
