package ast.functions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.Type;
import ast.basic.Node;

public class ArgumentDefinitionNode implements Node {

   private Type argType;
   private String argName;
   private boolean isSpider;

   public ArgumentDefinitionNode(Type argType, String argName, boolean isSpider) {
      this.isSpider = isSpider;
      this.argType = argType;
      this.argName = argName;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitArgumentDefinitionNode(argName, argType, isSpider);

   }

   public Type getType() {
      return this.argType;
   }

   public String getArgName() {
      return this.argName;
   }

   public boolean isSpider() {
      return this.isSpider;
   }

}
