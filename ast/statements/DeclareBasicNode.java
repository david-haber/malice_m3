package ast.statements;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.Type;
import ast.expressions.BasicIdentifierNode;

public class DeclareBasicNode extends DeclareNode {

   private BasicIdentifierNode identifierNode;
   private Type type;

   public DeclareBasicNode(BasicIdentifierNode identifier, Type type) {
      this.identifierNode = identifier;
      this.type = type;
   }

   public void setName(BasicIdentifierNode identifier) {
      this.identifierNode = identifier;
   }

   public void setType(Type type) {
      this.type = type;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitDeclareBasicNode(identifierNode, type);

   }

}
