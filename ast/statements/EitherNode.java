package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.expressions.NonCharExpressionNode;

public class EitherNode extends StatementNode {

   private NonCharExpressionNode conditionNode;
   private StatementListNode ifBody;
   private StatementListNode elseBody;

   public EitherNode(NonCharExpressionNode conditionNode,
         StatementListNode ifBody, StatementListNode elseBody) {
      this.conditionNode = conditionNode;
      this.ifBody = ifBody;
      this.elseBody = elseBody;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitEitherNode(conditionNode, ifBody, elseBody);

   }

}
