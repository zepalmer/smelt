package com.bahj.smelt.plugin.builtin.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationSpecificationInitializedEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializedEvent;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.type.DataType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.type.TextType;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactoryRegistry;
import com.bahj.smelt.plugin.builtin.editor.forms.builtin.ContainerFormFactory;
import com.bahj.smelt.plugin.builtin.editor.forms.builtin.DatumFieldFormFactory;
import com.bahj.smelt.plugin.builtin.editor.forms.builtin.TextFormFactory;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.MessageNode;
import com.bahj.smelt.syntax.ast.decoration.DeclarationNodeDecorator;
import com.bahj.smelt.syntax.ast.decoration.DecoratorNodeContext;
import com.bahj.smelt.syntax.ast.decoration.MessageNodeDecorator;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

/**
 * A Smelt plugin which provides editing actions for the basic data model.
 * 
 * @author Zachary Palmer
 */
public class EditorPlugin implements SmeltPlugin {
    private static final int MINOR_SPACING = 5;
    private static final int MAJOR_SPACING = 20;

    private FormFactoryRegistry formFactoryRegistry;
    private EditorModel editorModel;

    public EditorPlugin() {
        this.formFactoryRegistry = new FormFactoryRegistry();
    }

    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        // Make sure that we attach to the GUI after it's loaded.
        model.addListener(new TypedEventListener<>(SmeltApplicationConfigurationLoadedEvent.class,
                new EventListener<SmeltApplicationConfigurationLoadedEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationConfigurationLoadedEvent event) {
                        SmeltApplicationModel model = event.getApplicationModel();

                        DataModelPlugin dataModelPlugin = model.getPluginRegistry().getPlugin(DataModelPlugin.class);
                        BaseGUIPlugin guiPlugin = model.getPluginRegistry().getPlugin(BaseGUIPlugin.class);

                        guiPlugin.addListener(new TypedEventListener<>(BaseGUIInitializedEvent.class,
                                new EventListener<BaseGUIInitializedEvent>() {
                                    @Override
                                    public void eventOccurred(BaseGUIInitializedEvent event) {
                                        editorModel = new EditorModelImpl(dataModelPlugin, formFactoryRegistry, event
                                                .getExecutionContextRef());
                                    }
                                }));
                    }
                }));

        // Make sure we reinitialize this plugin whenever the specification is initialized.
        model.addListener(new TypedEventListener<>(SmeltApplicationSpecificationInitializedEvent.class,
                new EventListener<SmeltApplicationSpecificationInitializedEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationSpecificationInitializedEvent event) {
                        EditorPlugin.this.formFactoryRegistry.clear();

                        // Add default form factories.
                        EditorPlugin.this.formFactoryRegistry.registerFormFactory(TextType.INSTANCE,
                                TextFormFactory.INSTANCE);
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
        // Clearly, this plugin requires the base data model plugin to work. It also requires the base GUI.
        return new HashSet<>(Arrays.asList(BaseGUIPlugin.class, DataModelPlugin.class));
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        if (declarationNode instanceof MessageNode) {
            MessageNode messageNode = (MessageNode) declarationNode;
            return messageNode.getHeader().getName().equals("editor");
        } else {
            return false;
        }
    }

    @Override
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes)
            throws DeclarationProcessingException {
        DataModelPlugin dataModelPlugin = context.getModel().getPluginRegistry().getPlugin(DataModelPlugin.class);
        DecoratorNodeContext decoratorNodeContext = new DecoratorNodeContext(context, this);

        Set<String> requiredTypeNames = new HashSet<String>();
        for (DeclarationNode node : declarationNodes) {
            if (node instanceof MessageNode) {
                MessageNodeDecorator messageNode = new MessageNodeDecorator((MessageNode) node, decoratorNodeContext);
                String typeName = messageNode.getHeader().insistSinglePositionalArgument("type name")
                        .insistSingleComponent();
                SmeltType<?, ?> type = dataModelPlugin.getModel().getTypes().get(typeName);
                if (type == null) {
                    // TODO: raise an exception for referring to a missing type
                    throw new NotYetImplementedException();
                }
                if (!(type instanceof DataType)) {
                    // TODO: raise an exception for referring to a non-data type.
                    throw new NotYetImplementedException();
                } else {
                    requiredTypeNames.addAll(processDeclarationAndRegisterFactory(context, dataModelPlugin,
                            (DataType) type, messageNode));
                }
            } else {
                throw new IllegalStateException("processDeclarations was provided non-MesssageNode: " + node);
            }
        }

        Set<String> missingTypes = new HashSet<>();
        for (String requiredTypeName : requiredTypeNames) {
            if (dataModelPlugin.getModel().getTypes().get(requiredTypeName) == null) {
                missingTypes.add(requiredTypeName);
            }
        }

        if (missingTypes.size() > 0) {
            // TODO: raise an exception because some referenced types did not have declared editors
            // this will probably require additional metadata (rather than just a Set<String>)
            throw new NotYetImplementedException();
        }
    }

    private Set<String> processDeclarationAndRegisterFactory(SmeltPluginDeclarationHandlerContext context,
            DataModelPlugin dataModelPlugin, DataType type, MessageNodeDecorator messageNodeDecorator)
            throws DeclarationProcessingException {
        DeclarationNodeDecorator<?> decoratedDeclarationNode = messageNodeDecorator.insistOneChild();
        FormFactoryBuilder builder = new FormFactoryBuilder(type);
        FormFactory factory = builder.buildFormFactory(decoratedDeclarationNode);
        this.formFactoryRegistry.registerFormFactory(type, factory);
        return builder.getRequiredTypeNames();
    }

    private class FormFactoryBuilder {
        private DataType type;
        private Set<String> requiredTypeNames;

        /**
         * Creates a form factory builder for a given type.
         * 
         * @param type
         *            The type of data for which to build a form.
         */
        public FormFactoryBuilder(DataType type) {
            this.type = type;
            this.requiredTypeNames = new HashSet<>();
        }

        public Set<String> getRequiredTypeNames() {
            return requiredTypeNames;
        }

        /**
         * Creates a factory for the provided type of data and the given form description node.
         * 
         * @param type
         *            The type of data for which a factory is being created.
         * @param node
         *            The node describing the structure of the form.
         * @return The form factory in question.
         * @throws DeclarationProcessingException
         *             If the provided node is incorrectly formatted.
         */
        public FormFactory buildFormFactory(DeclarationNodeDecorator<?> node) throws DeclarationProcessingException {
            MessageNodeDecorator messageNodeDecorator = node.insistMessageNode();
            // Each decorator is either a container or a base element. We build a factory appropriately.
            if (messageNodeDecorator.getHeader().getName().equals("container")) {
                // First, do some sanity checking on the node.
                messageNodeDecorator.getHeader().insistNoNamedArguments();
                String orientationName = messageNodeDecorator.getHeader().insistSinglePositionalArgument("orientation")
                        .insistSingleComponent();
                ContainerFormFactory.Orientation orientation;
                switch (orientationName) {
                    case "horizontal":
                        orientation = ContainerFormFactory.Orientation.HORIZONTAL;
                        break;
                    case "vertical":
                        orientation = ContainerFormFactory.Orientation.VERTICAL;
                        break;
                    default:
                        // TODO: raise an error for an unrecognized orientation
                        throw new NotYetImplementedException();
                }

                // Create form factories for each child. Each of these factories will take the SmeltDatum as an
                // argument and build part of its form; we will glue them together.
                List<? extends DeclarationNodeDecorator<?>> children = messageNodeDecorator.getChildren();
                List<FormFactory> factories = new ArrayList<>(children.size());
                for (DeclarationNodeDecorator<?> child : children) {
                    factories.add(this.buildFormFactory(child));
                }

                // Produce a factory which will build an appropriate container for all of the provided children.
                return new ContainerFormFactory(factories, orientation, MAJOR_SPACING);
            } else {
                String fieldName = messageNodeDecorator.getHeader().getName();

                // First, do some sanity checking on the node.
                messageNodeDecorator.getHeader().insistNoNamedArguments();
                messageNodeDecorator.getHeader().insistNoPositionalArguments();
                messageNodeDecorator.insistNoChildren();

                // Next, ensure that the type in question has a field by that name. If it does not, eagerly fail.
                SmeltType<?, ?> fieldType = type.getProperties().get(fieldName);
                if (fieldType == null) {
                    // TODO: appropriate error
                    throw new NotYetImplementedException();
                }

                // Indicate that this field's type should have a form.
                this.requiredTypeNames.add(fieldType.getName());

                // Now create a factory which will get an appropriate form for the component. We do this lazily so we
                // don't have to worry about the order in which forms are defined.
                return new DatumFieldFormFactory(EditorPlugin.this.formFactoryRegistry, fieldType, fieldName,
                        MINOR_SPACING);
            }
        }
    }

    /**
     * Retrieves the form factory registry used by this plugin. Other plugins can use this registry to modify the
     * behavior of the {@link EditorPlugin} by adding forms for new Smelt types (or overriding forms which already
     * exist).
     * 
     * @return The form factory registry for this plugin.
     */
    public FormFactoryRegistry getRegistry() {
        return formFactoryRegistry;
    }

    /**
     * Retrieves the editor model for this plugin. Other plugins can use this object to trigger the presentation of
     * editors for values in the database.
     * 
     * @return The editor model for this plugin.
     */
    public EditorModel getEditorModel() {
        return editorModel;
    }
}
