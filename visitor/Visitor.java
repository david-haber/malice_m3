package visitor;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import semanticAnalyzer.*;

import ast.*;
import ast.expressions.*;
import ast.functions.*;
import ast.statements.*;

public interface Visitor {

   public void visitProgramNode(MainNode mainNode,
         LinkedList<FunctionNode> functionList) throws SemanticException,
         FileNotFoundException;

   public void visitMainNode(StatementListNode statListNode)
         throws SemanticException;

   // *************************** StatementNodes ************************** //
   public void visitStatementNode(StatementNode statement)
         throws SemanticException; // each statement is a subclass

   // of 'Statement.java"
   public void visitStatementListNode(StatementNode statement,
         StatementListNode rest) throws SemanticException;

   public void visitAssignNode(ArrayElementIdentifierNode ident,
         ExpressionNode expression) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

   public void visitAssignNode(BasicIdentifierNode ident,
         ExpressionNode expression) throws SemanticException;

   public void visitDeclareArrayNode(BasicIdentifierNode ident,
         NumericalExpressionNode index, Type type) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

   public void visitDeclareBasicNode(BasicIdentifierNode ident, Type type)
         throws SemanticException;

   public void visitSpokeNode(ExpressionNode expression)
         throws SemanticException;

   public void visitRoomCallNode(String functionName,
         LinkedList<ParameterNode> params) throws SemanticException;

   public void visitLookingGlassCallNode(String functionName,
         BasicIdentifierNode identifier) throws SemanticException;

   public void visitLookingGlassCallNode(String functionName,
         ArrayElementIdentifierNode identifier) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

   public void visitEventuallyNode(NonCharExpressionNode conditionNode,
         StatementListNode statList) throws SemanticException;

   public void visitPerhapsNode(NonCharExpressionNode condition,
         StatementListNode ifBody, ElseNode elseBody) throws SemanticException;

   public void visitEitherNode(NonCharExpressionNode condition,
         StatementListNode ifBody, StatementListNode elseBody)
         throws SemanticException;

   public void visitWhatWasNode(BasicIdentifierNode identiferNode)
         throws SemanticException;

   public void visitWhatWasNode(ArrayElementIdentifierNode identiferNode)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException;

   public void visitFoundNode(ExpressionNode expression)
         throws SemanticException;

   public void visitAteNode(BasicIdentifierNode ident) throws SemanticException;

   public void visitAteNode(ArrayElementIdentifierNode ident)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException;

   public void visitDrankNode(BasicIdentifierNode ident)
         throws SemanticException;

   public void visitDrankNode(ArrayElementIdentifierNode ident)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException;

   // ******************************* Functions *************************** //

   public void visitRoomFunctionNode(String name,
         LinkedList<ArgumentDefinitionNode> arguments, Type returnType,
         StatementListNode functionBody) throws FileNotFoundException,
         SemanticException;

   public void visitLookingGlassFunctionNode(String name, Type argType,
         StatementListNode functionBody) throws SemanticException,
         FileNotFoundException;

   // **************************** ExpressionNodes ************************ //

   // LiteralExpressionNodes
   public void visitCharacterLiteralNode(char constantChar);

   public void visitIntegerLiteralNode(int integer);

   public void visitIdentifierNode(String identName) throws SemanticException;

   public void visitStringLiteralNode(String constant);

   public void visitArrayElementIdentifierNode(
         BasicIdentifierNode arrayIdentifierNode,
         NumericalExpressionNode arrayIndexNode) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

   // UnaryNumericalExpressionNodes
   public void visitBitCompNode(NumericalExpressionNode expression)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException;

   public void visitNegateIntegerNode(NumericalExpressionNode numExpressionNode)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException;

   // BinaryNumericalExpressionNodes
   public void visitBitAndExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

   public void visitBitOrExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException;

   public void visitBitXorExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException;

   public void visitPlusExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException;

   public void visitMinusExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException;

   public void visitDivExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException;

   public void visitModExpressionNode(
         NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression) throws SemanticException;

   public void visitMultExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException;

   // UnaryBoolExpressions
   public void visitNotBoolExpression(NonCharExpressionNode expression)
         throws SemanticException;

   // BinaryBoolExpressions
   public void visitBoolAndExpressionNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException;

   public void visitBoolOrExpressionNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException;

   // ComparisonNodes
   public void visitComparisonEQNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

   public void visitComparisonGTENode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException;

   public void visitComparisonGTNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException;

   public void visitComparisonLTENode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException;

   public void visitComparisonLTNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException;

   public void visitComparisonNOTEQNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException;

   public void visitArgumentDefinitionNode(String argName, Type argType,
         boolean isSpider) throws SemanticException;

}
