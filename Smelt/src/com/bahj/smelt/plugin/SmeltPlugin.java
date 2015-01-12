package com.bahj.smelt.plugin;

import java.util.Set;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.syntax.ast.DeclarationNode;

/**
 * An interface implemented by any class which operates as a plugin to Smelt. Smelt plugins are loaded at startup and
 * may affect the semantics of a Smelt data model file.
 * <p/>
 * Smelt plugins are required to have a nullary constructor.
 * 
 * @author Zachary Palmer
 */
public interface SmeltPlugin {
    /**
     * A method which is invoked when this plugin is registered with an application model. A plugin may wish to add
     * listeners to that model.
     * 
     * @param model
     */
    public void registeredToApplicationModel(SmeltApplicationModel model);

    /**
     * A method which is invoked when this plugin is unregistered from an application model. A plugin may wish to remove
     * listeners from that model.
     * 
     * @param model
     */
    public void unregisteredFromApplicationModel(SmeltApplicationModel model);

    /**
     * Determines whether this plugin is responsible for the provided declaration in a Smelt configuration file.
     * 
     * @param context
     *            The context in which to process the declaration.
     * @param declarationNode
     *            The declaration node.
     * @return <code>true</code> if this plugin takes responsibility for the declaration; <code>false</code> if it does
     *         not.
     */
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode);

    /**
     * Processes the provided declarations. Callers guarantee that they will not provide declarations which are not
     * approved by the {@link SmeltPlugin#claimsDeclaration(SmeltPluginDeclarationHandlerContext, DeclarationNode)}
     * predicate. This method may only be called once per load of the application meta state of an application model;
     * that is, if the same declaration is passed to this function twice and a duplicate declaration error is raised,
     * this plugin is not at fault.
     * 
     * @param context
     *            The context in which to process the declarations.
     * @param declarationNodes
     *            The declarations to process.
     */
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes);
}
