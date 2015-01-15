package com.bahj.smelt.plugin.builtin.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
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
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

public class DataModelPlugin implements SmeltPlugin {
    private SmeltDataModel model = new SmeltDataModel();

    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        // Time to add the appropriate listener to the application model so that we can add components to the GUI.
        EventListener<SmeltApplicationEvent> listener = new TypedEventListener<>(
                SmeltApplicationConfigurationLoadedEvent.class,
                new EventListener<SmeltApplicationConfigurationLoadedEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationConfigurationLoadedEvent event) {
                        // Make sure we add the appropriate content to the Smelt GUI. This consists of the menu
                        // items necessary to activate the opening and closing of databases.
                        SmeltApplicationModel model = event.getApplicationModel();
                        // TODO
                    }
                });
        model.addListener(listener);
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        // While the data declaration plugin does depend on the GUI plugin for GUI layout, it does not have any
        // dependency in terms of declaration handling order.
        return Collections.emptySet();
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
        // must be a DAG of dependencies in their definitions. (All data types are embedded -- there is no indirection
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
                        SmeltType<?> fieldType = getTypeForTypeName(fieldMessage.getHeader().getName());
                        String fieldName = fieldMessage.getHeader().insistSinglePositionalArgument("field name")
                                .insistSingleComponent();
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