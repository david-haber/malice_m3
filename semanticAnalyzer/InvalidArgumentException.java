package semanticAnalyzer;

public class InvalidArgumentException extends SemanticException {

   /**
    * This exception is thrown if the identifier used is not declared in the
    * symbol table
    */
   private static final long serialVersionUID = 1L;

   public InvalidArgumentException(String idName) {
      super(idName);
   }

}
