package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class ComparisonGTNode extends ComparisonNode {

   public ComparisonGTNode(NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitComparisonGTNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
