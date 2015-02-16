package com.bahj.smelt.plugin.builtin.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.data.gui.DataGUIPlugin;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.syntax.ast.DeclarationNode;

/**
 * This plugin provides functionality for managing data models and databases in Smelt. It is actually a symbolic plugin
 * which forces the load of two other plugins: the data model plugin and the data GUI plugin (the latter of which
 * provides menu options and other GUI-related tools).
 * 
 * @author Zachary Palmer
 */
public class DataPlugin implements SmeltPlugin {

    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getRuntimeDependencyTypes() {
        return new HashSet<>(Arrays.asList(DataModelPlugin.class, DataGUIPlugin.class));
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        return false;
    }

    @Override
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes)
            throws DeclarationProcessingException {
    }

}
