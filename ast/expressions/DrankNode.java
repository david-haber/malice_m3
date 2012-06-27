package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public class DrankNode extends PostUnaryOpNode {

   public DrankNode(IdentifierNode identifier) {
      super(identifier);
   }

   @Override
   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      if (this.getChildExpression() instanceof BasicIdentifierNode) {
         v.visitDrankNode((BasicIdentifierNode) this.getChildExpression());
      } else {
         v.visitDrankNode((ArrayElementIdentifierNode) this
               .getChildExpression());
      }

   }

}
