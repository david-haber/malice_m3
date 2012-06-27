package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public abstract class BinaryBoolExpressionNode extends BoolExpressionNode
      implements BinaryExpressionNode {

   private NonCharExpressionNode leftChildExpression;
   private NonCharExpressionNode rightChildExpression;

   public BinaryBoolExpressionNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) {
      this.leftChildExpression = leftChildExpression;
      this.rightChildExpression = rightChildExpression;
   }

   public void setLeftChild(NonCharExpressionNode leftChildExpression) {
      this.leftChildExpression = leftChildExpression;
   }

   public void setRightChild(NonCharExpressionNode rightChildExpression) {
      this.rightChildExpression = rightChildExpression;
   }

   public NonCharExpressionNode getLeftChild() {
      return leftChildExpression;
   }

   public NonCharExpressionNode getRightChild() {
      return rightChildExpression;
   }

   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
