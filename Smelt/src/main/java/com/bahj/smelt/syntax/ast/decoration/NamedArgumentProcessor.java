package com.bahj.smelt.syntax.ast.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.bahj.smelt.plugin.DeclarationProcessingException;

public class NamedArgumentProcessor {
    private MessageHeaderNodeDecorator node;
    private Map<String, ? extends NamedArgumentNodeDecorator> unprocessedArgumentNodeMap;

    public NamedArgumentProcessor(MessageHeaderNodeDecorator node) {
        super();
        this.node = node;
        this.unprocessedArgumentNodeMap = new HashMap<>(node.getNamed());
    }

    public MessageHeaderNodeDecorator getNode() {
        return node;
    }

    /**
     * Processes a required named argument. If the provided processor throws a
     * {@link DeclarationProcessingRuntimeException}, this method will unwrap it and throw the contents.
     * 
     * @param name
     *            The name of the required argument.
     * @param processor
     *            The processor which handles that argument.
     * @return The result of processing.
     * @throws DeclarationProcessingException
     *             If processing the argument failed.
     */
    public <T> T processRequired(String name, Function<NamedArgumentNodeDecorator, T> processor)
            throws DeclarationProcessingException {
        NamedArgumentNodeDecorator node = unprocessedArgumentNodeMap.get(name);
        if (node == null) {
            throw this.node.failureWithMessage("Missing named argument: " + name);
        }
        try {
            T result = processor.apply(node);
            unprocessedArgumentNodeMap.remove(name);
            return result;
        } catch (DeclarationProcessingRuntimeException e) {
            throw e.getException();
        }
    }

    /**
     * Processes an optional named argument. If the provided processor throws a
     * {@link DeclarationProcessingRuntimeException}, this method will unwrap it and throw the contents.
     * 
     * @param name
     *            The name of the optional argument.
     * @param defaultValue
     *            The default value to return if the argument does not appear.
     * @param processor
     *            The processor which handles that argument.
     * @return The result of processing.
     * @throws DeclarationProcessingException
     *             If processing the argument failed.
     */
    public <T> T processOptional(String name, T defaultValue, Function<NamedArgumentNodeDecorator, T> processor)
            throws DeclarationProcessingException {
        NamedArgumentNodeDecorator node = unprocessedArgumentNodeMap.get(name);
        if (node == null) {
            return defaultValue;
        }
        try {
            T result = processor.apply(node);
            unprocessedArgumentNodeMap.remove(name);
            return result;
        } catch (DeclarationProcessingRuntimeException e) {
            throw e.getException();
        }
    }

    /**
     * Processes a required named argument. If the provided processor throws a
     * {@link DeclarationProcessingRuntimeException}, this method will unwrap it and throw the contents.
     * 
     * @param name
     *            The name of the required argument.
     * @param processor
     *            The processor which handles that argument.
     * @return This object.
     * @throws DeclarationProcessingException
     *             If processing the argument failed.
     */
    public NamedArgumentProcessor require(String name, Consumer<NamedArgumentNodeDecorator> processor)
            throws DeclarationProcessingException {
        processRequired(name, (NamedArgumentNodeDecorator n) -> {
            processor.accept(n);
            return null;
        });
        return this;
    }

    /**
     * Processes an optional named argument. If the provided processor throws a
     * {@link DeclarationProcessingRuntimeException}, this method will unwrap it and throw the contents.
     * 
     * @param name
     *            The name of the required argument.
     * @param processor
     *            The processor which handles that argument.
     * @return This object.
     * @throws DeclarationProcessingException
     *             If processing the argument failed.
     */
    public NamedArgumentProcessor allow(String name, Consumer<NamedArgumentNodeDecorator> processor)
            throws DeclarationProcessingException {
        processOptional(name, null, (NamedArgumentNodeDecorator n) -> {
            processor.accept(n);
            return null;
        });
        return this;
    }

    /**
     * Completes the processing of these arguments. If any unprocessed arguments still exist, an error is generated.
     * 
     * @throws DeclarationProcessingException
     *             If there are any remaining unprocessed arguments.
     */
    public void finsh() throws DeclarationProcessingException {
        if (!unprocessedArgumentNodeMap.isEmpty()) {
            throw this.node.failureWithMessage("Unrecognized named arguments: "
                    + String.join(",", new ArrayList<>(unprocessedArgumentNodeMap.keySet())));
        }
    }
}
