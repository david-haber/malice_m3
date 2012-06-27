package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class NotBooleanNode extends NumericalExpressionNode {

   private NonCharExpressionNode booleanExpressionNode;

   public NotBooleanNode(NonCharExpressionNode left) {
      this.booleanExpressionNode = left;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitNotBoolExpression(booleanExpressionNode);

   }

}
