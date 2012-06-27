package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.expressions.ExpressionNode;

public class SaidSpokeNode extends StatementNode {

   private ExpressionNode expNode;

   public SaidSpokeNode(ExpressionNode expNode) {
      this.expNode = expNode;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitSpokeNode(expNode);
   }

}
