package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class ComparisonGTENode extends ComparisonNode {

   public ComparisonGTENode(NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitComparisonGTENode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
