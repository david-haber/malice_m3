package semanticAnalyzer;

public class KeyWordException extends SemanticException {

   /**
    * This exception is thrown if the user attempts to declare an identifier
    * that is equal to one of the keywords specified in the MAlice language
    */
   private static final long serialVersionUID = 1L;

   public KeyWordException(String idName) {
      super("Identifier '" + idName + "' is a MAlice keyword.");
   }

}
