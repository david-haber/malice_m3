package ast.functions;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

import ast.Type;
import ast.statements.StatementListNode;

public class RoomFunctionNode extends FunctionNode {

   private String name;
   private LinkedList<ArgumentDefinitionNode> arguments;
   private Type returnType;
   private StatementListNode functionBody;

   public RoomFunctionNode(String name,
         LinkedList<ArgumentDefinitionNode> arguments, Type returnType,
         StatementListNode functionBody) {
      this.name = name;
      this.arguments = arguments;
      this.returnType = returnType;
      this.functionBody = functionBody;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException,
         FileNotFoundException {

      v.visitRoomFunctionNode(name, arguments, returnType, functionBody);

   }

}
