package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class BitXorExpressionNode extends BinaryNumericalExpressionNode {

   public BitXorExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitBitXorExpressionNode(this.getLeftChildExpression(), this
            .getRightChildExpression());
   }

}