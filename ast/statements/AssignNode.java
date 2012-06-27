package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.expressions.*;

public class AssignNode extends StatementNode {

   private IdentifierNode identifierNode;
   private ExpressionNode expressionNode;

   public AssignNode(IdentifierNode name, ExpressionNode expression) {
      this.identifierNode = name;
      this.expressionNode = expression;
   }

   public void setIdentifierName(IdentifierNode identifierNode) {
      this.identifierNode = identifierNode;
   }

   public void setExpressionNode(ExpressionNode expression) {
      this.expressionNode = expression;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      if (identifierNode instanceof BasicIdentifierNode) {
         v.visitAssignNode((BasicIdentifierNode) identifierNode,
                     expressionNode);
      } else {
         v.visitAssignNode((ArrayElementIdentifierNode) identifierNode,
               expressionNode);
      }
   }

}
