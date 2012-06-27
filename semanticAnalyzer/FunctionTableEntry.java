package semanticAnalyzer;

import java.util.LinkedList;

import ast.Type;
import ast.functions.ArgumentDefinitionNode;

public class FunctionTableEntry {

   private LinkedList<ArgumentDefinitionNode> arguments;
   private Type returnType;

   public FunctionTableEntry(LinkedList<ArgumentDefinitionNode> arguments,
         Type returnType) {
      this.arguments = arguments;
      this.returnType = returnType;
   }

   public FunctionTableEntry(Type returnType) {
      this.arguments = null;
      this.returnType = returnType;
   }

   public Type getType() {
      return this.returnType;
   }

   public LinkedList<ArgumentDefinitionNode> getArgs() {
      return this.arguments;
   }

}
