package semanticAnalyzer;

import ast.Type;

public class TypeMatchException extends SemanticException {

   /**
    * This exception is thrown if an operation attempts to operate on two
    * operands of different type
    */
   private static final long serialVersionUID = 1L;

   public TypeMatchException(Type type1, Type type2) {
      super("Type " + type1.name() + " and " + type2.name() + " do not match.");
   }

   public TypeMatchException() {
      super("Types do not match.");
   }

}
