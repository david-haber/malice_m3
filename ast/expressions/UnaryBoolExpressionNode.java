package ast.expressions;

public abstract class UnaryBoolExpressionNode implements UnaryExpressionNode {

   BoolExpressionNode childExpression;

   public BoolExpressionNode getChildExpression() {
      return childExpression;
   }

   public void setChildExpression(BoolExpressionNode expressionNode) {
      this.childExpression = expressionNode;
   }

}
