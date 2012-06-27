package semanticAnalyzer;

public class InvalidFunctionCallException extends SemanticException {

   private static final long serialVersionUID = 1L;

   public InvalidFunctionCallException(String functionName) {
      super(functionName + " does not take these arguments");
   }

}
