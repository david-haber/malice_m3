package semanticAnalyzer;

public abstract class SemanticException extends Exception {

   /**
    * This exception is thrown by the Semantic Analyzer
    */
   private static final long serialVersionUID = 1L;

   public SemanticException(String s) {
      super(s);
   }

}
