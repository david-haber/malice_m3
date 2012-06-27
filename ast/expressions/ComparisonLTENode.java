package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class ComparisonLTENode extends ComparisonNode {

   public ComparisonLTENode(NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitComparisonLTENode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
