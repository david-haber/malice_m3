package semanticAnalyzer;

public class DivisionByZeroException extends SemanticException {

   /**
    * This exception is thrown if division by zero occurs
    */
   private static final long serialVersionUID = 1L;

   public DivisionByZeroException() {
      super("Division by zero");
   }

}
