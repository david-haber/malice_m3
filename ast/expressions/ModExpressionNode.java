package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public class ModExpressionNode extends BinaryNumericalExpressionNode {

   public ModExpressionNode(NumericalExpressionNode lExp,
         NumericalExpressionNode rExp) {
      super(lExp, rExp);
   }

   @Override
   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      v.visitModExpressionNode(getLeftChildExpression(),
            getRightChildExpression());
   }

}