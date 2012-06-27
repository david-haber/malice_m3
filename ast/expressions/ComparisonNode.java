package ast.expressions;

public abstract class ComparisonNode extends BoolExpressionNode implements
      BinaryExpressionNode {

   private NumericalExpressionNode leftChildExpression;
   private NumericalExpressionNode rightChildExpression;

   public ComparisonNode(NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) {
      this.leftChildExpression = leftChildExpression;
      this.rightChildExpression = rightChildExpression;
   }

   public void setLeftChild(NumericalExpressionNode leftChildExpression) {
      this.leftChildExpression = leftChildExpression;
   }

   public void setRightChild(NumericalExpressionNode rightChildExpression) {
      this.rightChildExpression = rightChildExpression;
   }

   public NumericalExpressionNode getLeftChildExpression() {
      return leftChildExpression;
   }

   public NumericalExpressionNode getRightChildExpression() {
      return rightChildExpression;
   }

}
