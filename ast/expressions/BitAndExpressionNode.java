package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class BitAndExpressionNode extends BinaryNumericalExpressionNode {

   public BitAndExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitBitAndExpressionNode(this.getLeftChildExpression(), this
            .getRightChildExpression());
   }

}