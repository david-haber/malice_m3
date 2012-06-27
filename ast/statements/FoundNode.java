package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.expressions.ExpressionNode;

public class FoundNode extends StatementNode {

   private ExpressionNode expressionNode;

   public FoundNode(ExpressionNode expNode) {
      this.expressionNode = expNode;
   }

   public void setExpNode(ExpressionNode expNode) {
      this.expressionNode = expNode;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitFoundNode(expressionNode);

   }

}
