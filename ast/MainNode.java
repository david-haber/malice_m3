package ast;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.basic.Node;
import ast.statements.StatementListNode;

public class MainNode implements Node {

   private StatementListNode statementList;

   public MainNode(StatementListNode statementList) {
      this.statementList = statementList;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitMainNode(statementList);
   }

}
