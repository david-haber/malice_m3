package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public abstract class UnaryNumericalExpressionNode extends
      NumericalExpressionNode implements UnaryExpressionNode {

   @Override
   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
