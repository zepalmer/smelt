package com.bahj.smelt.plugin;

import com.bahj.smelt.SmeltApplicationModel;

/**
 * The context in which plugins handle declarations. This object contains data that plugins may require when making
 * decisions regarding declarations or when processing them.
 * 
 * @author Zachary Palmer
 */
public class SmeltPluginDeclarationHandlerContext {
    private SmeltApplicationModel model;

    public SmeltPluginDeclarationHandlerContext(SmeltApplicationModel model) {
        super();
        this.model = model;
    }

    public SmeltApplicationModel getModel() {
        return model;
    }
}
