package com.bahj.smelt.plugin;

import com.bahj.smelt.syntax.SourceLocation;

/**
 * Describes an error in processing a declaration.
 * 
 * @author Zachary Palmer
 */
public class DeclarationProcessingFailure {
    /** The Smelt plugin. */
    private SmeltPlugin plugin;
    /** The error message associated with this failure. */
    private String errorMessage;
    /** The location in the source file associated with this failure. */
    private SourceLocation location;

    public DeclarationProcessingFailure(SmeltPlugin plugin, String errorMessage, SourceLocation location) {
        super();
        this.plugin = plugin;
        this.errorMessage = errorMessage;
        this.location = location;
    }

    public SmeltPlugin getPlugin() {
        return plugin;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public SourceLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return this.location + ": " + this.errorMessage;
    }
}
