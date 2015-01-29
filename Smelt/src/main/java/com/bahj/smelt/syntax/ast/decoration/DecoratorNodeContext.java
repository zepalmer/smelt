package com.bahj.smelt.syntax.ast.decoration;

import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;

public class DecoratorNodeContext {
    /** The declaration handler context being used during declaration processing. */
    private SmeltPluginDeclarationHandlerContext pluginDeclarationHandlerContext;
    /** The plugin which is processing the node. */
    private SmeltPlugin plugin;

    public DecoratorNodeContext(SmeltPluginDeclarationHandlerContext pluginDeclarationHandlerContext, SmeltPlugin plugin) {
        super();
        this.pluginDeclarationHandlerContext = pluginDeclarationHandlerContext;
        this.plugin = plugin;
    }

    public SmeltPluginDeclarationHandlerContext getPluginDeclarationHandlerContext() {
        return pluginDeclarationHandlerContext;
    }

    public SmeltPlugin getPlugin() {
        return plugin;
    }

}
