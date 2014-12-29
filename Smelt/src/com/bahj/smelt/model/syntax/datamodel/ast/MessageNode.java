package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;
import java.util.Map;

public class MessageNode implements Ast {
    private Header header;
    private List<Ast> children;

    public MessageNode(Header header, List<Ast> children) {
        super();
        this.header = header;
        this.children = children;
    }

    public Header getHeader() {
        return header;
    }

    public List<Ast> getChildren() {
        return children;
    }

    public static class Header {
        private List<String> name;
        private List<String> positional;
        private Map<String, String> named;

        public Header(List<String> name, List<String> positional, Map<String, String> named) {
            super();
            this.name = name;
            this.positional = positional;
            this.named = named;
        }

        public List<String> getName() {
            return name;
        }

        public List<String> getPositional() {
            return positional;
        }

        public Map<String, String> getNamed() {
            return named;
        }

    }
}
