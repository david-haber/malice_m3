package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public class AteNode extends PostUnaryOpNode {

   public AteNode(IdentifierNode identifier) {
      super(identifier);
   }

   @Override
   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      // TODO instanceof??
      if (this.getChildExpression() instanceof BasicIdentifierNode) {
         v.visitAteNode((BasicIdentifierNode) this.getChildExpression());
      } else {
         v.visitAteNode((ArrayElementIdentifierNode) this.getChildExpression());
      }

   }

}