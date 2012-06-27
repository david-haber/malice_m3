package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public abstract class PostUnaryOpNode extends UnaryNumericalExpressionNode {

   private IdentifierNode identifier;

   public PostUnaryOpNode(IdentifierNode identifier) {
      this.identifier = identifier;
   }

   public IdentifierNode getChildExpression() {
      return identifier;
   }

   public void setChildExpression(IdentifierNode expNode) {
      this.identifier = expNode;
   }

   public abstract void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

}
