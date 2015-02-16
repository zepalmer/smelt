package com.bahj.smelt.plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * An exception which is raised when a Smelt plugin encounters a syntactic or semantic error when processing
 * declarations.
 * 
 * @author Zachary Palmer
 */
public class DeclarationProcessingException extends Exception {
    private static final long serialVersionUID = 1L;

    private Collection<DeclarationProcessingFailure> failures;

    public DeclarationProcessingException(DeclarationProcessingFailure... failures) {
        this(Arrays.asList(failures));
    }

    public DeclarationProcessingException(Collection<DeclarationProcessingFailure> failures) {
        super();
        this.failures = failures;
        if (this.failures.size() == 0) {
            throw new IllegalStateException("Cannot create a declaration processing exception with zero failures.");
        }
    }

    public Collection<DeclarationProcessingFailure> getFailures() {
        return Collections.unmodifiableCollection(failures);
    }
}
