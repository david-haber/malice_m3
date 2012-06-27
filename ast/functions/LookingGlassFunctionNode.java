package ast.functions;

import java.io.FileNotFoundException;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

import ast.Type;
import ast.statements.StatementListNode;

public class LookingGlassFunctionNode extends FunctionNode {

   private String name;
   private Type argType;
   private StatementListNode functionBody;

   public LookingGlassFunctionNode(String name, Type argType,
         StatementListNode functionBody) {
      this.name = name;
      this.argType = argType;
      this.functionBody = functionBody;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException,
         FileNotFoundException {
      v.visitLookingGlassFunctionNode(name, argType, functionBody);

   }

}
