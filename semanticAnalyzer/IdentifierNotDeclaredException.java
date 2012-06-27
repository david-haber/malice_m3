package semanticAnalyzer;

public class IdentifierNotDeclaredException extends SemanticException {

   /**
    * This exception is thrown if the identifier used is not declared in the
    * symbol table
    */
   private static final long serialVersionUID = 1L;

   public IdentifierNotDeclaredException(String idName) {
      super("Identifier '" + idName
            + "' has not been declared in this context.");
   }

}
