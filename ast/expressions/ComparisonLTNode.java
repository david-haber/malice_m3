package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class ComparisonLTNode extends ComparisonNode {

   public ComparisonLTNode(NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitComparisonLTNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
