package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public abstract class NonCharExpressionNode extends ExpressionNode {

   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
