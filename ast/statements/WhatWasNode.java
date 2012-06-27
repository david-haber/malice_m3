package ast.statements;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;
import ast.expressions.*;

public class WhatWasNode extends StatementNode {

   IdentifierNode identifierNode;

   public WhatWasNode(IdentifierNode identifierNode) {
      this.identifierNode = identifierNode;
   }

   @Override
   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      if (identifierNode instanceof BasicIdentifierNode) {
         v.visitWhatWasNode((BasicIdentifierNode) identifierNode);
      } else {
         v.visitWhatWasNode((ArrayElementIdentifierNode) identifierNode);
      }
   }

}
