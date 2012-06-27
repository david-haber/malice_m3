package ast.statements;

import visitor.Visitor;

public class SaidNode extends StatementNode {
   private String text;

   public SaidNode(String text) {
      // Removes the double quotes
      this.text = text.substring(1, text.length() - 1);
   }

   public String getText() {
      return this.text;
   }

   @Override
   public void acceptVisitor(Visitor v) {
   }
}
