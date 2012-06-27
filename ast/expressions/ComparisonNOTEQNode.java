package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class ComparisonNOTEQNode extends ComparisonNode {

   public ComparisonNOTEQNode(NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitComparisonNOTEQNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
