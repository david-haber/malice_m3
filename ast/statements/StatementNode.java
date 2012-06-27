package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.basic.Node;

public abstract class StatementNode implements Node {

   @Override
   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
