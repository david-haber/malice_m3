package ast.statements;

import java.util.LinkedList;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

import ast.expressions.NumericalExpressionNode;
import ast.functions.ParameterNode;

public class RoomCallNode extends NumericalExpressionNode {
   private String functionName;
   private LinkedList<ParameterNode> parameters;

   public RoomCallNode(String functionName, LinkedList<ParameterNode> parameters) {
      this.functionName = functionName;
      this.parameters = parameters;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException {
      v.visitRoomCallNode(functionName, parameters);
   }

   public String getRoomFunctionName() {
      return this.functionName;
   }

}
