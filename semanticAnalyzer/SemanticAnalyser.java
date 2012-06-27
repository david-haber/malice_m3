package semanticAnalyzer;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;

import visitor.Visitor;

import ast.MainNode;
import ast.Type;
import ast.expressions.*;
import ast.functions.*;
import ast.statements.*;

public class SemanticAnalyser implements Visitor {

   private SymbolTable symbolTable;
   private FunctionTable functionTable;

   public SemanticAnalyser() {
      symbolTable = new SymbolTable();
      functionTable = new FunctionTable();
   }

   public void visitArrayElementIdentifierNode(
         BasicIdentifierNode arrayIdentifierNode,
         NumericalExpressionNode arrayIndexNode) throws SemanticException {
   }

   public void checkDeclared(IdentifierNode ident)
   throws IdentifierNotDeclaredException {
      symbolTable.isDeclared(ident.getName());
   }

   public void checkBoolean(ExpressionNode expression) 
   throws SemanticException {
      // Valid Booleans are
      // BoolExpressionNodes
      // Identifiers that evaluate to a number (needs to be 0 or 1 or program
      // crashes)
      // A room function that returns a number
      Type expressionType;
      if (expression instanceof BoolExpressionNode) {
      } else if (expression instanceof IdentifierNode) {
         IdentifierNode ident = (IdentifierNode) expression;
         expressionType = symbolTable.getType(ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      } else if (expression instanceof RoomCallNode) {
         expressionType = functionTable
         .getReturnType((RoomCallNode) expression);
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      } else {
         throw new TypeMatchException();
      }
   }

   public void checkArgsMatch(String functionName,
         LinkedList<ParameterNode> params) throws SemanticException {
      // Check the parameters passed match those defined by our function
      LinkedList<ArgumentDefinitionNode> argDefs = functionTable
      .getArgs(functionName);
      for (int i = 0; i < params.size(); i++) {
         checkArgMatch(params.get(i), argDefs.get(i));
      }
      if (params.size() != argDefs.size()) {
         throw new InvalidFunctionCallException(functionName);
      }
   }

   public void checkArgMatch(ParameterNode parameter,
         ArgumentDefinitionNode argDef) throws SemanticException {
      if (argDef.isSpider()) {
         if (!(parameter.getValue() instanceof BasicIdentifierNode)) {
            throw new InvalidArgumentException("Argument is not an array");
         } else {
            BasicIdentifierNode identifier = (BasicIdentifierNode) parameter
            .getValue();
            if (!symbolTable.isDeclaredArray(identifier.getName(), argDef
                  .getType())) {
               throw new InvalidArgumentException("Argument is not an array");
            }
         }
      } else if (parameter.getValue() instanceof IdentifierNode) {
         IdentifierNode identifier = (IdentifierNode) parameter.getValue();
         if (symbolTable.getType(identifier.getName()) != argDef.getType()) {
            throw new InvalidArgumentException("Argument Type Mismatch");
         }
      } else if (parameter.getValue() instanceof RoomCallNode) {
         {
            RoomCallNode roomCall = (RoomCallNode) parameter.getValue();
            if (functionTable.getReturnType(roomCall) != argDef.getType()) {
               throw new InvalidArgumentException("Argument Type Mismatch");
            }
         }
      } else {
         if (argDef.getType() == Type.NUMBER) {
            if (parameter.getValue() instanceof NumericalExpressionNode) {
               parameter.acceptVisitor(this);
            } else {
               throw new InvalidArgumentException(
               "Argument Type Mismatch: Number expected");
            }
         } else if (argDef.getType() == Type.LETTER) {
            if (parameter.getValue() instanceof CharacterLiteralNode) {
               parameter.acceptVisitor(this);
            } else {
               throw new InvalidArgumentException(
               "Argument Type Mismatch: Letter expected");
            }
         } else if (argDef.getType() == Type.STRING) {
            if (parameter.getValue() instanceof StringLiteralNode) {
               parameter.acceptVisitor(this);
            } else {
               throw new InvalidArgumentException(
               "Argument Type Mismatch: String expected");
            }
         }
      }
   }

   public void checkInt(NonCharExpressionNode expression)
   throws SemanticException {
      Type expressionType;
      if (expression instanceof IdentifierNode) {
         IdentifierNode ident = (IdentifierNode) expression;
         expressionType = symbolTable.getType(ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      } else if (expression instanceof RoomCallNode) {
         expressionType = functionTable
         .getReturnType((RoomCallNode) expression);
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      // No other checks need to be performed on other numericalexpressions
   }

   public void checkPrintable(ExpressionNode expression) {
      // Neccessary?
   }

   public void checkValidAssignment(IdentifierNode ident,
         ExpressionNode expression) throws SemanticException {
      Type LHSexpressionType = symbolTable.getType(ident.getName());
      Type RHSexpressionType;
      // Check LHS is declared
      symbolTable.isDeclared(ident.getName());
      if (expression instanceof IdentifierNode) {
         // Check RHS is declared in symbol table
         IdentifierNode rhsident = (IdentifierNode) expression;
         symbolTable.isDeclared(rhsident.getName());
         // Check Types match in Bob became Claire
         RHSexpressionType = symbolTable.getType(rhsident.getName());
         if (LHSexpressionType != RHSexpressionType) {
            throw new TypeMatchException(LHSexpressionType, RHSexpressionType);
         }
      } else if (expression instanceof RoomCallNode) {
         // Check RoomFunctionCall is declared in function table
         // functionTable.isDefined((RoomCallNode) expression);
         // Check return type of RoomFunction equals LHS
         RHSexpressionType = functionTable
         .getReturnType((RoomCallNode) expression);
         if (LHSexpressionType != RHSexpressionType) {
            throw new TypeMatchException(LHSexpressionType, RHSexpressionType);
         }
      } else if (expression instanceof NumericalExpressionNode) {
         if (LHSexpressionType != Type.NUMBER) {
            throw new TypeMatchException();
         }
      } else if (expression instanceof CharacterLiteralNode) {
         if (LHSexpressionType != Type.LETTER) {
            throw new TypeMatchException();
         }
      } else {
         throw new TypeMatchException();
      }
   }

   public void visitAssignNode(BasicIdentifierNode ident,
         ExpressionNode expression) throws SemanticException {
      checkValidAssignment(ident, expression);
      symbolTable.initializeIdentifier(ident.getName());
      ident.acceptVisitor(this);
      expression.acceptVisitor(this);
   }

   public void visitAssignNode(ArrayElementIdentifierNode ident,
         ExpressionNode expression) throws SemanticException {
      checkValidAssignment(ident, expression);
      symbolTable.initializeIdentifier(ident.getName());
      ident.acceptVisitor(this);
      expression.acceptVisitor(this);
   }

   public void visitAteNode(BasicIdentifierNode ident) 
   throws SemanticException {
      symbolTable.isDeclared(ident.getName());
      if (symbolTable.getType(ident.getName()) != Type.NUMBER) {
         throw new TypeMatchException(symbolTable.getType(ident.getName()),
               Type.NUMBER);
      }
      ident.acceptVisitor(this);
   }

   public void visitAteNode(ArrayElementIdentifierNode ident)
   throws SemanticException {
      symbolTable.isDeclared(ident.getName());
      if (symbolTable.getType(ident.getName()) != Type.NUMBER) {
         throw new TypeMatchException(symbolTable.getType(ident.getName()),
               Type.NUMBER);
      }
      ident.acceptVisitor(this);
   }

   public void visitDrankNode(BasicIdentifierNode ident)
   throws SemanticException {
      symbolTable.isDeclared(ident.getName());
      if (symbolTable.getType(ident.getName()) != Type.NUMBER) {
         throw new TypeMatchException(symbolTable.getType(ident.getName()),
               Type.NUMBER);
      }
      ident.acceptVisitor(this);
   }

   public void visitDrankNode(ArrayElementIdentifierNode ident)
   throws SemanticException {
      symbolTable.isDeclared(ident.getName());
      if (symbolTable.getType(ident.getName()) != Type.NUMBER) {
         throw new TypeMatchException(symbolTable.getType(ident.getName()),
               Type.NUMBER);
      }
      ident.acceptVisitor(this);
   }

   public void visitBasicIdentifierNode(String name) throws KeyWordException {
      checkKeywords(name);
   }

   private void checkKeywords(String name) throws KeyWordException {
      for (Keywords keyword : Keywords.values()) {
         if (name.toUpperCase().equals(keyword.toString().toUpperCase())) {
            throw new KeyWordException(name);
         }
      }
   }

   public void visitBitAndExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      Type expressionType;
      if (e1 instanceof IdentifierNode) {
         IdentifierNode ident = (IdentifierNode) e1;
         expressionType = symbolTable.getType(ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      if (e2 instanceof IdentifierNode) {
         IdentifierNode e2ident = (IdentifierNode) e2;
         expressionType = symbolTable.getType(e2ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      e1.acceptVisitor(this);
      e2.acceptVisitor(this);
   }

   public void visitBitCompNode(NumericalExpressionNode expression)
   throws SemanticException {
      // Check expression evaluates
      Type expressionType;
      if (expression instanceof IdentifierNode) {
         IdentifierNode ident = (IdentifierNode) expression;
         expressionType = symbolTable.getType(ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      expression.acceptVisitor(this);
   }

   public void visitBitOrExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      // Check e1 & e2 evaluate to a number
      Type expressionType;
      if (e1 instanceof IdentifierNode) {
         IdentifierNode e1ident = (IdentifierNode) e1;
         expressionType = symbolTable.getType(e1ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      if (e2 instanceof IdentifierNode) {
         IdentifierNode e2ident = (IdentifierNode) e2;
         expressionType = symbolTable.getType(e2ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      e1.acceptVisitor(this);
      e2.acceptVisitor(this);
   }

   public void visitBitXorExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      // Check e1 & e2 evaluate to a number
      Type expressionType;
      if (e1 instanceof IdentifierNode) {
         IdentifierNode e1ident = (IdentifierNode) e1;
         expressionType = symbolTable.getType(e1ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      if (e2 instanceof IdentifierNode) {
         IdentifierNode e2ident = (IdentifierNode) e2;
         expressionType = symbolTable.getType(e2ident.getName());
         if (expressionType != Type.NUMBER) {
            throw new TypeMatchException(expressionType, Type.NUMBER);
         }
      }
      e1.acceptVisitor(this);
      e2.acceptVisitor(this);
   }

   public void visitBoolAndExpressionNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to bool
      checkBoolean(leftChildExpression);
      // **Lazy Evaluation** checkBoolean(rightChildExpression);
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitBoolOrExpressionNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to bool
      checkBoolean(leftChildExpression);
      // **LAZY EVALUATION - Therefore we don't check the RHS**
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitCharacterLiteralNode(char constantChar) {
      // Nothing?
   }

   public void visitComparisonEQNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to int
      // This type of node will then evaluate to a bool
      checkInt(leftChildExpression);
      checkInt(rightChildExpression);
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitComparisonGTENode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to int
      // This type of node will then evaluate to a bool
      checkInt(leftChildExpression);
      checkInt(rightChildExpression);
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitComparisonGTNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to int
      // This type of node will then evaluate to a bool
      checkInt(leftChildExpression);
      checkInt(rightChildExpression);
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitComparisonLTENode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to int
      // This type of node will then evaluate to a bool
      checkInt(leftChildExpression);
      checkInt(rightChildExpression);
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitComparisonLTNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to int
      // This type of node will then evaluate to a bool
      checkInt(leftChildExpression);
      checkInt(rightChildExpression);
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitComparisonNOTEQNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {
      // Check leftChild and rightChild evaluate to int
      // This type of node will then evaluate to a bool
      checkInt(leftChildExpression);
      checkInt(rightChildExpression);
      leftChildExpression.acceptVisitor(this);
      rightChildExpression.acceptVisitor(this);
   }

   public void visitDeclareBasicNode(BasicIdentifierNode ident, Type type)
   throws SemanticException {
      // Check ident is unique
      symbolTable.addBasicIdentifier(ident.getName(), type);
   }

   public void visitDeclareArrayNode(BasicIdentifierNode arrName,
         NumericalExpressionNode arrSize, Type arrType)
   throws SemanticException {
      symbolTable.addArrayIdentifier(arrName.getName(), arrType);
      symbolTable.initializeIdentifier(arrName.getName());
   }

   public void visitDivExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      // Check e1 and e2 are int
      checkInt(e1);
      checkInt(e2);
      e1.acceptVisitor(this);
      e2.acceptVisitor(this);
   }

   public void visitEitherNode(NonCharExpressionNode condition,
         StatementListNode ifBody, StatementListNode elseBody)
   throws SemanticException {
      // Check condition is bool
      checkBoolean(condition);
      condition.acceptVisitor(this);
      SymbolTable oldTable = symbolTable;
      symbolTable = new SymbolTable(symbolTable);
      ifBody.acceptVisitor(this);
      symbolTable = oldTable;
      if (elseBody != null) {
         oldTable = symbolTable;
         symbolTable = new SymbolTable(symbolTable);
         elseBody.acceptVisitor(this);
         symbolTable = oldTable;
      }
   }

   public void visitEventuallyNode(NonCharExpressionNode conditionNode,
         StatementListNode statList) throws SemanticException {
      // Check condition is bool
      checkBoolean(conditionNode);
      conditionNode.acceptVisitor(this);
      SymbolTable parentTable = symbolTable;
      symbolTable = new SymbolTable(symbolTable);
      statList.acceptVisitor(this);
      symbolTable = parentTable;
   }

   public void visitFoundNode(ExpressionNode expression)
   throws SemanticException {
      expression.acceptVisitor(this);
   }

   public void visitIdentifierNode(String identName) throws SemanticException {
      symbolTable.isDeclared(identName);
      symbolTable.isInitialised(identName);
   }

   public void visitIntegerLiteralNode(int integer) {
   }

   public void visitLookingGlassCallNode(String functionName,
         BasicIdentifierNode identifier) throws SemanticException {
      // Check functionName is defined, check Types match?
      // Should a LookingGlassCallNode store its name as an IndentifierNode
      symbolTable.isDeclared(identifier.getName());
      functionTable.isDefined(functionName);
      if (symbolTable.getType(identifier.getName()) != functionTable
            .getReturnType(functionName)) {
         throw new TypeMatchException(
               symbolTable.getType(identifier.getName()), functionTable
               .getReturnType(functionName));
      }
      identifier.acceptVisitor(this);
   }

   public void visitLookingGlassCallNode(String functionName,
         ArrayElementIdentifierNode identifier) throws SemanticException {
      symbolTable.isDeclared(identifier.getArrayIdentifierNode().getName());
      functionTable.isDefined(functionName);
      if (symbolTable.getType(
            identifier.getArrayIdentifierNode().getName()) != functionTable
            .getReturnType(functionName)) {
         throw new TypeMatchException(symbolTable.getType(identifier
               .getArrayIdentifierNode().getName()), functionTable
               .getReturnType(functionName));
      }
      identifier.acceptVisitor(this);
   }

   public void visitLookingGlassFunctionNode(String name, Type argType,
         StatementListNode functionBody) throws MultipleDeclarationException {
      // Add function name to list, incl type
      functionTable.addLookingFunction(name, argType);

   }

   public void visitMainNode(StatementListNode statListNode)
   throws SemanticException {
      // Nothing
      statListNode.acceptVisitor(this);
   }

   public void visitMinusExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      // check e1 and e2 evaluate to int
      checkInt(e1);
      checkInt(e2);
      e1.acceptVisitor(this);
      e2.acceptVisitor(this);
   }

   public void visitMultExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      // check e1 and e2 evaluate to int
      checkInt(e1);
      checkInt(e2);
      e1.acceptVisitor(this);
      e2.acceptVisitor(this);
   }

   public void visitNotBoolExpression(NonCharExpressionNode expression)
   throws SemanticException {
      // check expression evaluate to bool
      checkBoolean(expression);
      expression.acceptVisitor(this);
   }

   public void visitPerhapsNode(NonCharExpressionNode condition,
         StatementListNode ifBody, ElseNode elseBody) throws SemanticException {
      // check condition evaluates to condition
      checkBoolean(condition);
      condition.acceptVisitor(this);
      SymbolTable oldTable = symbolTable;
      symbolTable = new SymbolTable(symbolTable);
      ifBody.acceptVisitor(this);
      symbolTable = oldTable;
      if (elseBody != null) {
         oldTable = symbolTable;
         symbolTable = new SymbolTable(symbolTable);
         elseBody.acceptVisitor(this);
         symbolTable = oldTable;
      }
   }

   public void visitPlusExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      // check e1 and e2 evaluates to int
      checkInt(e1);
      checkInt(e2);
      e1.acceptVisitor(this);
      e2.acceptVisitor(this);

   }

   public void visitProgramNode(MainNode mainNode,
         LinkedList<FunctionNode> functionList) throws FileNotFoundException,
         SemanticException {
      // Important visit functions first, then main
      Iterator<FunctionNode> myIt = functionList.iterator();
      while (myIt.hasNext()) {
         symbolTable.reset();
         symbolTable.loseParent();
         myIt.next().acceptVisitor(this);
      }
      symbolTable.reset();
      symbolTable.loseParent();
      mainNode.acceptVisitor(this);
   }

   public void visitRoomCallNode(String functionName,
         LinkedList<ParameterNode> params) throws SemanticException {
      // May need to then visit params? Or no?
      checkArgsMatch(functionName, params);
   }

   public void visitRoomFunctionNode(String name,
         LinkedList<ArgumentDefinitionNode> arguments, Type returnType,
         StatementListNode functionBody) throws SemanticException {
      // RoomFunctionNode needs a name attribute
      // Make note of function name and its attributes, return type
      Iterator<ArgumentDefinitionNode> myIt = arguments.iterator();
      while (myIt.hasNext()) {
         ArgumentDefinitionNode arg = myIt.next();
         arg.acceptVisitor(this);

      }
      functionTable.addRoomFunction(name, arguments, returnType);
      functionBody.acceptVisitor(this);
   }

   public void visitSaidSpokeNode(ExpressionNode expression)
   throws SemanticException {
      // Check expression can be spoken
      checkPrintable(expression);
      expression.acceptVisitor(this);
   }

   public void visitStatementListNode(StatementNode statement,
         StatementListNode rest) throws SemanticException {
      statement.acceptVisitor(this);
      if (rest != null) {
         rest.acceptVisitor(this);
      }
   }

   public void visitStatementNode(StatementNode statement)
   throws SemanticException {
      statement.acceptVisitor(this);
   }

   public void visitWhatWasNode(BasicIdentifierNode identifierNode)
   throws SemanticException {
      symbolTable.initializeIdentifier(identifierNode.getName());
      identifierNode.acceptVisitor(this);
   }

   public void visitWhatWasNode(ArrayElementIdentifierNode identifier)
   throws SemanticException {
      symbolTable.isDeclared(identifier.getArrayIdentifierNode().getName());
      identifier.acceptVisitor(this);
   }

   public void visitSpokeNode(ExpressionNode expression)
   throws SemanticException {
      expression.acceptVisitor(this);
   }

   public void visitSaidNode(String s) {
   }

   @Override
   public void visitStringLiteralNode(String constant) {
   }

   public void visitModExpressionNode(
         NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) 
   throws SemanticException {
   }

   @Override
   public void visitNegateIntegerNode(NumericalExpressionNode numExpressionNode)
   throws TypeMatchException, IdentifierNotDeclaredException,
   SemanticException {
      checkInt(numExpressionNode);

   }

   public void visitArgumentDefinitionNode(String argName, Type argType,
         boolean isSpider) throws SemanticException {
      if (isSpider) {
         symbolTable.addArrayIdentifier(argName, argType);
      } else {
         symbolTable.addBasicIdentifier(argName, argType);
      }
      symbolTable.initializeIdentifier(argName);
   }

}