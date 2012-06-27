package ast.expressions;

import visitor.Visitor;

public class CharacterLiteralNode extends ExpressionNode {

   private char constant;

   public CharacterLiteralNode(String token_image) {
      constant = token_image.charAt(1);
      // 1 as token_image looks like 'c' and we only need c
      // .charAt(0) would refer to the first single quotation char
   }

   public void setConstant(char constant) {
      this.constant = constant;
   }

   @Override
   public void acceptVisitor(Visitor v) {
      v.visitCharacterLiteralNode(constant);
   }

}
