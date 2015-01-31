package com.bahj.smelt.plugin.builtin.editor;

import java.util.Collections;
import java.util.Set;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.syntax.ast.DeclarationNode;

/**
 * A Smelt plugin which provides editing actions for the basic data model.
 * @author Zachary Palmer
 */
public class EditorPlugin implements SmeltPlugin {
    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        // This plugin must wait until the data model has been established before it can build a profile for its
        // editors.
        return Collections.singleton(DataModelPlugin.class);
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getRuntimeDependencyTypes() {
        // Clearly, this plugin requires the base data model plugin to work.
        return Collections.singleton(DataModelPlugin.class);
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes)
            throws DeclarationProcessingException {
        // TODO Auto-generated method stub
        
    }
}
