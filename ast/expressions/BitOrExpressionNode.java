package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class BitOrExpressionNode extends BinaryNumericalExpressionNode {

   public BitOrExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitBitOrExpressionNode(this.getLeftChildExpression(), this
            .getRightChildExpression());
   }

}