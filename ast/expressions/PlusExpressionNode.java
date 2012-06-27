package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class PlusExpressionNode extends BinaryNumericalExpressionNode {

   public PlusExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitPlusExpressionNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
