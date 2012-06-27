package ast.statements;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;
import ast.expressions.*;

public class LookingGlassCallNode extends StatementNode {

   private String functionName;
   private IdentifierNode identifier;

   public LookingGlassCallNode(IdentifierNode identifier, String functionName) {
      this.identifier = identifier;
      this.functionName = functionName;
   }

   @Override
   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      if (identifier instanceof BasicIdentifierNode) {
         v.visitLookingGlassCallNode(functionName,
               (BasicIdentifierNode) identifier);
      } else {
         v.visitLookingGlassCallNode(functionName,
               (ArrayElementIdentifierNode) identifier);
      }
   }

}
