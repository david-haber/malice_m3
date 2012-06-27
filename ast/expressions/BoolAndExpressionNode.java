package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class BoolAndExpressionNode extends BinaryBoolExpressionNode {

   public BoolAndExpressionNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) {
      super(leftChildExpression, rightChildExpression);
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitBoolAndExpressionNode(this.getLeftChild(), this.getRightChild());

   }

}
