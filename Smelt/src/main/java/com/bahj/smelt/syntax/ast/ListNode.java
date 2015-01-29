package com.bahj.smelt.syntax.ast;

import java.util.List;

public interface ListNode extends DeclarationNode {
    public List<String> getValues();
}