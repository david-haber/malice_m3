package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class MinusExpressionNode extends BinaryNumericalExpressionNode {

   public MinusExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitMinusExpressionNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}