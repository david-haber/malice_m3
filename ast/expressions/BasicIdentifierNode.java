package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class BasicIdentifierNode extends IdentifierNode {

   private String name;

   public BasicIdentifierNode(String name) {
      // NB. _name is got from token.image
      this.name = name;
   }

   public String getName() {
      return name;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitIdentifierNode(name);
   }

}
