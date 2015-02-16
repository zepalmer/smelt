package com.bahj.smelt.syntax.ast.impl;

import java.util.Collections;
import java.util.List;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.ListNode;

public class ListNodeImpl extends AbstractAstNodeImpl implements ListNode {
    private List<String> values;

    public ListNodeImpl(SourceLocation location, List<String> values) {
        super(location);
        this.values = values;
    }

    @Override
    public List<String> getValues() {
        return values;
    }

    @Override
    public String getSimpleDescription() {
        StringBuilder sb = new StringBuilder();
        for (String value : this.values) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(value);
        }
        return sb.toString();
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        return Collections.emptyList();
    }
}
