package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

public class BitCompNode extends UnaryNumericalExpressionNode {

   NumericalExpressionNode expressionNode;

   public BitCompNode(NumericalExpressionNode expressionNode) {
      this.expressionNode = expressionNode;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitBitCompNode(expressionNode);
   }

}
