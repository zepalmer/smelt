package com.bahj.smelt.syntax.ast;

/**
 * Represents a raw, parsed AST in the format of the Smelt data model. While individual processors may interpret these
 * trees differently, the AST is parsed into a single common subtree format before they are called.
 * 
 * @author Zachary Palmer
 */
public interface DeclarationNode extends AstNode {

}
