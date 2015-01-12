package com.bahj.smelt.syntax.ast;

import java.util.List;

public interface AstNode {
    public String getSimpleDescription();
    
    public List<? extends AstNode> getDescriptionChildren();
    
    default public String getTreeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(getSimpleDescription()).append("\n");
        for (AstNode child : this.getDescriptionChildren()) {
            sb.append("  ");
            sb.append(child.getTreeDescription().trim().replace("\n","\n  "));
            sb.append("\n");
        }
        return sb.toString(); 
    }
}
