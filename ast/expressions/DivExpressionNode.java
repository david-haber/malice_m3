package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class DivExpressionNode extends BinaryNumericalExpressionNode {

   public DivExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitDivExpressionNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}