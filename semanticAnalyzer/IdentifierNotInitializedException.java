package semanticAnalyzer;

public class IdentifierNotInitializedException extends SemanticException {

   /**
    * This exception is thrown if the identifier used has not been initialized
    * in the symbol table
    */
   private static final long serialVersionUID = 1L;

   public IdentifierNotInitializedException(String identName) {
      super("Identifier '" + identName + "' has not been initialized.");
   }

}
