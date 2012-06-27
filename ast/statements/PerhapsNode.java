package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.expressions.NonCharExpressionNode;

public class PerhapsNode extends ElseNode {

   private NonCharExpressionNode condition;
   private StatementListNode ifBody;
   private ElseNode elseBody = null;

   public PerhapsNode(NonCharExpressionNode condition,
         StatementListNode ifBody, ElseNode elseBody) {
      this.condition = condition;
      this.ifBody = ifBody;
      this.elseBody = elseBody;
   }

   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitPerhapsNode(condition, ifBody, elseBody);
   }

}
