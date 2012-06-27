package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public class NegateIntegerNode extends UnaryNumericalExpressionNode {

   private NumericalExpressionNode numExpressionNode;

   public NegateIntegerNode(NumericalExpressionNode numExpressionNode) {
      this.numExpressionNode = numExpressionNode;
   }

   @Override
   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      v.visitNegateIntegerNode(numExpressionNode);
   }

}
