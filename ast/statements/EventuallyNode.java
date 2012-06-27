package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.expressions.NonCharExpressionNode;

public class EventuallyNode extends StatementNode {

   private NonCharExpressionNode condition;
   private StatementListNode statList;

   public EventuallyNode(NonCharExpressionNode condition,
         StatementListNode statList) {
      this.condition = condition;
      this.statList = statList;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitEventuallyNode(condition, statList);

   }

}
