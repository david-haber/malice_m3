package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public abstract class BoolExpressionNode extends NonCharExpressionNode {

   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
