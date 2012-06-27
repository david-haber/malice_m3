package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public abstract class ElseNode extends StatementNode {

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {

   }

}
