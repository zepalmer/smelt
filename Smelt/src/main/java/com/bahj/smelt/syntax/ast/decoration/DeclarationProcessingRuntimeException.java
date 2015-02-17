package com.bahj.smelt.syntax.ast.decoration;

import com.bahj.smelt.plugin.DeclarationProcessingException;

/**
 * An exception specifically designed to carry the (checked) {@link DeclarationProcessingRuntimeException} over unchecked
 * boundaries (like anonymous functions) so it can be reported elsewhere.
 * @author Zachary Palmer
 *
 */
public class DeclarationProcessingRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private DeclarationProcessingException exception;

    public DeclarationProcessingRuntimeException(DeclarationProcessingException exception) {
        super(exception.getMessage(), exception);
        this.exception = exception;
    }

    public DeclarationProcessingException getException() {
        return exception;
    }
}
