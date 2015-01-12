package com.bahj.smelt.plugin.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.model.datamodel.DuplicateTypeNameException;
import com.bahj.smelt.model.datamodel.type.DataType;
import com.bahj.smelt.model.datamodel.type.SmeltType;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.MessageNode;
import com.bahj.smelt.util.NotYetImplementedException;

// TODO: consider -- the data model and database seem to be in a very centralized location
//       But maybe storing that information is the job of this plugin?
//       Any plugins which want to store additional metadata can't rely on an external model class to hold that
//       information.  Anyone who needs this information should look up this plugin -- which handles the data model --
//       by using the plugin registry.  This seems like the right way to go...

public class DataDeclarationPlugin implements SmeltPlugin {

    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisteredFromApplicationModel(SmeltApplicationModel model) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        if (declarationNode instanceof MessageNode) {
            MessageNode messageNode = (MessageNode) declarationNode;
            String name = messageNode.getHeader().getName();
            return name.equals("data");  // TODO: handle enum types as well
        } else {
            return false;
        }
    }

    @Override
    public void processDeclarations(final SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes) {
        // This process is sort of non-trivial. We want to allow the declarations to be provided unordered, but there
        // must be a DAG of dependencies in their definitions. (All data types are embedded -- there is no indirection
        // -- so no cyclic types need be created, but we might need to initialize a new data type during the middle of
        // initializing another. We accomplish this by keeping a dictionary of unhandled declarations and gradually
        // (and statefully) consuming them, raising the appropriate exceptions in the case of a problem.
        Map<String, MessageNode> declarationsByName = new HashMap<>();
        for (DeclarationNode declarationNode : declarationNodes) {
            if (declarationNode instanceof MessageNode) {
                MessageNode messageNode = (MessageNode)declarationNode;
                if (!messageNode.getHeader().getNamed().isEmpty()) {
                    // TODO: error: no named arguments are accepted here
                    throw new NotYetImplementedException();
                }
                if (messageNode.getHeader().getPositional().size() < 1) {
                    // TODO: error: no data name provided
                    throw new NotYetImplementedException();
                }
                if (messageNode.getHeader().getPositional().get(0).getComponents().size() != 1) {
                    // TODO: error: either no data name was provided or the name contains spaces
                    throw new NotYetImplementedException();
                }
                String name = messageNode.getHeader().getPositional().get(0).getComponents().get(0);
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
            private Map<String, MessageNode> declarations = declarationsByName;
            
            public void processDeclaration(String name) {
                if (processedNames.contains(name)) {
                    // Then the name was already processed; ignore this call.
                    return;
                } else if (consumedNames.contains(name)) {
                    // Then there is a cyclic dependency!
                    // TODO: appropriate error
                    throw new NotYetImplementedException();
                } else
                {
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
                        context.getModel().getLogicalModel().getDataModel().addType(dataType);
                    } catch (DuplicateTypeNameException e) {
                        // TODO: handle this exception
                        throw new NotYetImplementedException(e);
                    }
                    
                    // Finally, indicate that we have processed this declaration.
                    processedNames.add(name);
                }
            }
            
            private DataType constructDataTypeFromDeclaration(String typeName, MessageNode messageNode) {
                Map<String, SmeltType<?>> fields = new HashMap<>();
                for (AstNode node : messageNode.getDescriptionChildren()) {
                    if (node instanceof MessageNode) {
                        MessageNode fieldMessage = (MessageNode)node;
                        if (!messageNode.getHeader().getNamed().isEmpty()) {
                            // TODO: error: no named arguments are accepted here
                            throw new NotYetImplementedException();
                        }
                        if (fieldMessage.getChildren().size() > 0) {
                            // TODO: report syntax error (as children of fields cannot exist)
                            throw new NotYetImplementedException();
                        }
                        SmeltType<?> fieldType = getTypeForTypeName(fieldMessage.getHeader().getName());
                        if (fieldMessage.getHeader().getPositional().size() != 1) {
                            // TODO: report syntax error (as exactly one positional argument is expected: the field name
                            throw new NotYetImplementedException();
                        }
                        if (fieldMessage.getHeader().getPositional().get(0).getComponents().size()!=1){ 
                            // TODO: report syntax error (as field name should not contain multiple components)
                            throw new NotYetImplementedException();
                        }
                        String fieldName = fieldMessage.getHeader().getPositional().get(0).getComponents().get(0);
                        fields.put(fieldName, fieldType);
                    } else {
                        // TODO: report syntax error via appropriate exception
                        throw new NotYetImplementedException();
                    }
                }
                return new DataType(typeName, fields);
            }

            private SmeltType<?> getTypeForTypeName(String name) {
                // First try to look it up in the model.
                SmeltType<?> type = context.getModel().getLogicalModel().getDataModel().getTypes().get(name);
                if (type == null) {
                    // If that fails, let's try to process that name and then see if we can find anything.
                    processDeclaration(name);
                    type = context.getModel().getLogicalModel().getDataModel().getTypes().get(name);
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
