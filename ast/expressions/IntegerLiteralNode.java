package ast.expressions;

import visitor.Visitor;

public class IntegerLiteralNode extends NumericalExpressionNode {

   private int integer;

   public IntegerLiteralNode(String token_image) {
      integer = Integer.parseInt(token_image);
   }

   public void setValue(int value) {
      this.integer = value;
   }

   @Override
   public void acceptVisitor(Visitor v) {
      v.visitIntegerLiteralNode(integer);
   }

}
