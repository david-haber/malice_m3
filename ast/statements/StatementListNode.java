package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.basic.Node;

public class StatementListNode extends ElseNode implements Node {

   private StatementNode statementNode;
   private StatementListNode restNode;

   public StatementListNode(StatementNode statementNode,
         StatementListNode restNode) {
      this.statementNode = statementNode;
      this.restNode = restNode;
   }

   public StatementListNode(StatementNode statementNode) {
      this.statementNode = statementNode;
      this.restNode = null;
   }

   public void setStatement(StatementNode statementNode) {
      this.statementNode = statementNode;
   }

   public void setRest(StatementListNode restNode) {
      this.restNode = restNode;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitStatementListNode(statementNode, restNode);
   }

}
