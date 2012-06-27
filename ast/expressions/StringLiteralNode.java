package ast.expressions;

import visitor.Visitor;

public class StringLiteralNode extends ExpressionNode {

   private String constant;

   public StringLiteralNode(String constant) {
      // Remove quotation marks at beginning and end
      this.constant = constant.substring(1, constant.length() - 1);

   }

   @Override
   public void acceptVisitor(Visitor v) {
      v.visitStringLiteralNode(constant);
   }

}
