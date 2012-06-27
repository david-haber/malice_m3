package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public interface UnaryExpressionNode {

   public void acceptVisitor(Visitor v) throws SemanticException;

}
