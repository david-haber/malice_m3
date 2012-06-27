package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class BoolOrExpressionNode extends BinaryBoolExpressionNode {

   public BoolOrExpressionNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitBoolOrExpressionNode(getLeftChild(), getRightChild());
   }

}
