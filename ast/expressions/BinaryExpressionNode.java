package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.basic.Node;

public interface BinaryExpressionNode extends Node {

   public void acceptVisitor(Visitor v) throws SemanticException;

}
