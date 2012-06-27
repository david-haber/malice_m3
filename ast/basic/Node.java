package ast.basic;

import java.io.FileNotFoundException;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public interface Node {

   public void acceptVisitor(Visitor v) throws SemanticException,
         FileNotFoundException;

}