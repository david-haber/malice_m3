package ast.functions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.basic.Node;
import ast.expressions.ExpressionNode;

public class ParameterNode implements Node {

   private ExpressionNode value;

   public ParameterNode(ExpressionNode value) {
      this.value = value;
   }

   public Node getValue() {
      return this.value;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      value.acceptVisitor(v);
   }

}
