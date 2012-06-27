package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.Type;
import ast.expressions.BasicIdentifierNode;
import ast.expressions.NumericalExpressionNode;

public class DeclareArrayNode extends DeclareNode {

   private BasicIdentifierNode identifierNode; // Not an array piece
   private NumericalExpressionNode size; // The size of the array
   private Type type; // The type of all the array elements

   public DeclareArrayNode(BasicIdentifierNode identifierNode,
         NumericalExpressionNode numericalExpressionNode, Type type) {
      this.identifierNode = identifierNode;
      this.size = numericalExpressionNode;
      this.type = type;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitDeclareArrayNode(identifierNode, size, type);
   }

}
