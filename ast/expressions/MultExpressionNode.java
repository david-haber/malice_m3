package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class MultExpressionNode extends BinaryNumericalExpressionNode {

   public MultExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitMultExpressionNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
