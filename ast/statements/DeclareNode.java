package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public abstract class DeclareNode extends StatementNode {

   @Override
   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
