package semanticAnalyzer;

import ast.ProgramNode;

public interface SemanticAnalyzerInterface {

   /**
    * Entry point for a semantic analyzer
    * 
    * @param programNode
    * @throws MultipleDeclarationException
    * @throws IdentifierNotDeclaredException
    * @throws DivisionByZeroException
    * @throws TypeMatchException
    * @throws IdentifierNotInitializedException
    * @throws KeyWordException
    */
   public abstract void visitProgramNode(ProgramNode programNode)
         throws MultipleDeclarationException, IdentifierNotDeclaredException,
         DivisionByZeroException, TypeMatchException,
         IdentifierNotInitializedException, KeyWordException;

}