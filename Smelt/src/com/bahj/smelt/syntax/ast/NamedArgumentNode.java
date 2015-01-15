package com.bahj.smelt.syntax.ast;

import java.util.List;

public interface NamedArgumentNode extends ArgumentNode {

    public String getName();

    public List<String> getArgs();

}