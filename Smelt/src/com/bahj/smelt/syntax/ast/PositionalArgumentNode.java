package com.bahj.smelt.syntax.ast;

import java.util.List;

public interface PositionalArgumentNode extends ArgumentNode {

    public List<String> getComponents();

}