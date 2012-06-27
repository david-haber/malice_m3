package semanticAnalyzer;

public class MultipleDeclarationException extends SemanticException {

   /**
    * This exception is thrown if an identifier is declared twice in the symbol
    * table
    */

   private static final long serialVersionUID = 1L;

   public MultipleDeclarationException(String ident) {
      super("Identifier '" + ident + "' has been declared multiple times.");
   }

}
