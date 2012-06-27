package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public class ComparisonEQNode extends ComparisonNode {

   public ComparisonEQNode(NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      v.visitComparisonEQNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}
