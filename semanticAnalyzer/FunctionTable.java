package semanticAnalyzer;

import java.util.Hashtable;
import java.util.LinkedList;

import ast.Type;
import ast.functions.ArgumentDefinitionNode;
import ast.statements.RoomCallNode;

public class FunctionTable {

   Hashtable<String, FunctionTableEntry> functionList;

   public FunctionTable() {
      functionList = new Hashtable<String, FunctionTableEntry>();
   }

   public void addRoomFunction(String name,
         LinkedList<ArgumentDefinitionNode> arguments, Type returnType)
         throws MultipleDeclarationException {
      FunctionTableEntry newEntry = new FunctionTableEntry(arguments,
            returnType);
      if (functionList.containsKey(name)) {
         throw new MultipleDeclarationException(name);
      }
      functionList.put(name, newEntry);
   }

   public void addLookingFunction(String name, Type argType)
         throws MultipleDeclarationException {
      if (functionList.containsKey(name)) {
         throw new MultipleDeclarationException(name);
      }
      functionList.put(name, new FunctionTableEntry(argType));
   }

   public Type getReturnType(RoomCallNode roomCall)
         throws IdentifierNotDeclaredException {
      isDefined(roomCall.getRoomFunctionName());
      return functionList.get(roomCall.getRoomFunctionName()).getType();
   }

   public Type getReturnType(String lookingFunctionName)
         throws IdentifierNotDeclaredException {
      isDefined(lookingFunctionName);
      return functionList.get(lookingFunctionName).getType();
   }

   public void isDefined(String functionName)
         throws IdentifierNotDeclaredException {
      if (!functionList.containsKey(functionName)) {
         throw new IdentifierNotDeclaredException(functionName);
      }
   }

   public LinkedList<ArgumentDefinitionNode> getArgs(String functionName)
         throws IdentifierNotDeclaredException {
      isDefined(functionName);
      return functionList.get(functionName).getArgs();
   }

}
