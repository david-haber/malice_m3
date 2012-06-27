package intermediaryGeneration;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;

import intermediaryGeneration.instructions.arithmetic.*;
import intermediaryGeneration.instructions.basic.*;
import intermediaryGeneration.instructions.globalDeclarations.*;
import intermediaryGeneration.instructions.jump.*;
import intermediaryGeneration.instructions.logical.*;
import intermediaryGeneration.instructions.systemcalls.*;

import ast.*;
import ast.expressions.*;
import ast.functions.*;
import ast.statements.*;
import semanticAnalyzer.*;
import visitor.Visitor;

public class CGVisitor implements Visitor {

   private RegisterAllocator registerAllocator;
   private FunctionTable functionTable;
   private Function currentFunction;

   public CGVisitor() throws FileNotFoundException {
      functionTable = new FunctionTable();
      registerAllocator = new RegisterAllocator();
   }

   public FunctionTable getFunctionTable() {
      return functionTable;
   }

   // Public visitor methods //

   @Override
   public void visitAssignNode(ArrayElementIdentifierNode ident,
         ExpressionNode expression) throws SemanticException {
      // Get the location of the identifier. Basic identifiers have names,
      // but array identifiers have name and array location.
      // Take the expression value from the stack, and perform the assignment.

      String arrayName = ident.getArrayIdentifierNode().getName();
      String startOfArray = currentFunction.getVariableLocation(arrayName);
      Type arrayElementType = currentFunction.getVariableType(arrayName);
      NumericalExpressionNode arrayIndex = ident.getArrayIndexNode();

      arrayIndex.acceptVisitor(this);
      // Array index value is on top of the stack.

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction
            .addInstructionToBack(new SubInstruction(r1.getName(), "1"));
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + startOfArray + "]"));

      // r1 holds array index.
      // r2 holds array address

      if (arrayElementType != Type.LETTER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4")); // r1 holds array offset
      }
      currentFunction.addInstructionToBack(new AddInstruction(r1.getName(), r2
            .getName()));
      boolean eaxWasInUse = false;
      if (r1.getName() == "eax") {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         eaxWasInUse = true;
      }
      expression.acceptVisitor(this);
      if (expression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }

      // expression is evaluated and the answer is on top of the stack

      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      if (eaxWasInUse) {
         currentFunction.addInstructionToBack(new PopInstruction("eax"));
      }
      currentFunction.addInstructionToBack(new MoveInstruction("["
            + r1.getName() + "]", r2.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   public void visitAssignNode(BasicIdentifierNode ident,
         ExpressionNode expression) throws SemanticException {

      expression.acceptVisitor(this);
      if (expression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      // Expression's value is on top of the stack.
      String identName = ident.getName();
      String identLocation = currentFunction.getVariableLocation(identName);
      Type identType = currentFunction.getVariableType(identName);

      Register r1 = registerAllocator.getNextRegister();

      if (identType == Type.LETTER) {
         currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
               "[" + "esp" + "]"));
         currentFunction.addInstructionToBack(new AddInstruction("esp", "1"));
         currentFunction.addInstructionToBack(new MoveInstruction("["
               + identLocation + "]", r1.getByteName()));
      } else {
         currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
         currentFunction.addInstructionToBack(new MoveInstruction("["
               + identLocation + "]", r1.getName()));
      }

      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitBitAndExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {
      e1.acceptVisitor(this);
      if (e1 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      e2.acceptVisitor(this);
      if (e2 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }

      // Expression's value is on top of the stack.

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new AndInstruction(r1.getName(), r2
            .getName()));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);
   }

   @Override
   public void visitBitCompNode(NumericalExpressionNode expression)
         throws SemanticException {

      expression.acceptVisitor(this);
      if (expression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      // Expression's value is on top of the stack.
      Register currentRegister = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(currentRegister
            .getName()));
      currentFunction.addInstructionToBack(new NotInstruction(currentRegister
            .getName()));
      currentFunction.addInstructionToBack(new PushInstruction(currentRegister
            .getName()));

      registerAllocator.returnRegister(currentRegister);

   }

   @Override
   public void visitBitOrExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {

      Register register1 = registerAllocator.getNextRegister();
      Register register2 = registerAllocator.getNextRegister();

      e1.acceptVisitor(this);
      if (e1 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      e2.acceptVisitor(this);
      if (e2 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      // Expression's value is on top of the stack.

      currentFunction.addInstructionToBack(new PopInstruction(register1
            .getName()));
      currentFunction.addInstructionToBack(new PopInstruction(register2
            .getName()));
      currentFunction.addInstructionToBack(new OrInstruction(register1
            .getName(), register2.getName()));
      currentFunction.addInstructionToBack(new PushInstruction(register1
            .getName()));

      registerAllocator.returnRegister(register2);
      registerAllocator.returnRegister(register1);

   }

   @Override
   public void visitBitXorExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws SemanticException {

      Register register1 = registerAllocator.getNextRegister();
      Register register2 = registerAllocator.getNextRegister();

      e1.acceptVisitor(this);
      if (e1 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      e2.acceptVisitor(this);
      if (e2 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      // Expression's value is on top of the stack.
      currentFunction.addInstructionToBack(new PopInstruction(register1
            .getName()));
      currentFunction.addInstructionToBack(new PopInstruction(register2
            .getName()));
      currentFunction.addInstructionToBack(new XorInstruction(register1
            .getName(), register2.getName()));
      currentFunction.addInstructionToBack(new PushInstruction(register1
            .getName()));

      registerAllocator.returnRegister(register2);
      registerAllocator.returnRegister(register1);

   }

   @Override
   public void visitBoolAndExpressionNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {

      /*
       * and r1, r2 cmp r1, 1 jge _true _false: mov r1, 0 jmp _and _true: mov
       * r1, 1 _and: push r1; return 1 if true, 0 if false
       */

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      String _true = LabelGenerator.getLabel("_true");
      String _false = LabelGenerator.getLabel("_false");
      String _and = LabelGenerator.getLabel("_and");

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }

      /*
       * Lazy evaluation - if leftChildExpression evaluates to 0 then jump to
       * false, else evaluate as normal.
       */

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JeInstruction(_false));

      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new AndInstruction(r1.getName(), r2
            .getName()));

      // r1 has the result of bitwise and.

      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new JgeInstruction(_true));
      currentFunction.addInstructionToBack(new LabelInstruction(_false));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_and));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_and));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitBoolOrExpressionNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {

      /*
       * or r1, r2 cmp r1, 1 jge _true mov r1, 0 jmp _or _true: mov r1, 1 _or:
       * push r1; return 1 if true, 0 if false
       */
      String _true = LabelGenerator.getLabel("_true");
      String _or = LabelGenerator.getLabel("_or");

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }

      /*
       * Lazy evaluation - if leftChildExpression evaluates to 1 then jump to
       * true, else evaluate as normal.
       */

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new JgeInstruction(_true));

      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new OrInstruction(r1.getName(), r2
            .getName()));

      // r1 has the result of bitwise or.

      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new JgeInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_or));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_or));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitCharacterLiteralNode(char constantChar) {
      currentFunction.addInstructionToBack(new PushInstruction("'"
            + Character.toString(constantChar) + "'"));
   }

   @Override
   public void visitComparisonEQNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {

      /*
       * cmp r1, r2 je _true mov r1, 0 jmp _equals _true: mov r1, 1 _equals:
       * push r1
       */

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      // Expression's value is on top of the stack
      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      String _true = LabelGenerator.getLabel("_true");
      String _equals = LabelGenerator.getLabel("_equals");

      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            r2.getName()));
      currentFunction.addInstructionToBack(new JeInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_equals));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_equals));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitComparisonGTENode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {

      /*
       * cmp r1, r2 jge _true mov r1, 0 jmp _gte _true: mov r1, 1 _gte: push r1
       */

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }

      Register r1 = registerAllocator.getNextRegister(); // r1 = left child
      Register r2 = registerAllocator.getNextRegister(); // r2 = right child

      // return true if left >= right

      String _true = LabelGenerator.getLabel("_true");
      String _gte = LabelGenerator.getLabel("_gte");

      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            r2.getName()));
      currentFunction.addInstructionToBack(new JgeInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_gte));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_gte));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitComparisonGTNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {

      /*
       * cmp r1, r2 jg _true mov r1, 0 jmp _gt _true: mov r1, 1 _gt: push r1
       */

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }

      Register r1 = registerAllocator.getNextRegister(); // r1 = left child
      Register r2 = registerAllocator.getNextRegister(); // r2 = right child

      String _true = LabelGenerator.getLabel("_true");
      String _gt = LabelGenerator.getLabel("_gt");

      // return true if left > right

      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      // r2 = right child
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      // r1 = left child
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            r2.getName()));
      currentFunction.addInstructionToBack(new JgInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_gt));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_gt));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitComparisonLTENode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {

      /*
       * cmp r1, r2 jle _true mov r1, 0 jle _lte _true: mov r1, 1 _lte: push r1
       */

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }

      Register r1 = registerAllocator.getNextRegister(); // r1 = left child
      Register r2 = registerAllocator.getNextRegister(); // r2 = right child

      String _true = LabelGenerator.getLabel("_true");
      String _lte = LabelGenerator.getLabel("_lte");

      // return true if left <= right

      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            r2.getName()));
      currentFunction.addInstructionToBack(new JleInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_lte));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_lte));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitComparisonLTNode(NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {

      /*
       * cmp r1, r2 jl _true mov r1, 0 jl _lt _true: mov r1, 1 _lt: push r1
       */

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }

      Register r1 = registerAllocator.getNextRegister(); // r1 = left child
      Register r2 = registerAllocator.getNextRegister(); // r2 = right child

      String _true = LabelGenerator.getLabel("_true");
      String _lt = LabelGenerator.getLabel("_lt");

      // return true if left < right

      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            r2.getName()));
      currentFunction.addInstructionToBack(new JlInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_lt));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_lt));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitComparisonNOTEQNode(
         NonCharExpressionNode leftChildExpression,
         NonCharExpressionNode rightChildExpression) throws SemanticException {

      /*
       * cmp r1, r2 je _true mov r1, 1 jl _ne _true: mov r1, 0 _ne: push r1
       */

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }

      Register r1 = registerAllocator.getNextRegister(); // r1 = left child
      Register r2 = registerAllocator.getNextRegister(); // r2 = right child

      String _true = LabelGenerator.getLabel("_true");
      String _ne = LabelGenerator.getLabel("_ne");

      // return true if left != right

      currentFunction.addInstructionToBack(new PopInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            r2.getName()));
      currentFunction.addInstructionToBack(new JneInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new JmpInstruction(_ne));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new LabelInstruction(_ne));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitDivExpressionNode(
         NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException {

      Register eax = registerAllocator.getSpecificRegister("eax");
      Register edx = registerAllocator.getSpecificRegister("edx");
      Register r1 = registerAllocator.getNextRegister();

      boolean eaxWasInUse = false;
      boolean edxWasInUse = false;

      // Check eax and edx not in use.

      if (eax == null) {
         eaxWasInUse = true;
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      if (edx == null) {
         edxWasInUse = true;
         currentFunction.addInstructionToBack(new PushInstruction("edx"));
      }

      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }

      currentFunction.addInstructionToBack(new MoveInstruction("edx", "0"));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new MoveInstruction("eax", r1
            .getName()));

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new DivInstruction(r1.getName()));
      // eax now holds quotient
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "eax"));
      registerAllocator.returnRegister(r1);
      // Restore registers
      if (edxWasInUse) {
         currentFunction.addInstructionToBack(new PopInstruction("edx"));
      } else {
         registerAllocator.returnRegister(edx);
      }
      if (eaxWasInUse) {
         currentFunction.addInstructionToBack(new PopInstruction("eax"));
      } else {
         registerAllocator.returnRegister(eax);
      }
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

   }

   @Override
   public void visitEitherNode(NonCharExpressionNode condition,
         StatementListNode ifBody, StatementListNode elseBody)
         throws SemanticException {

      /*
       * _if: cmp r1, 1 jne _else ; ifBody jmp _endif _else: ; elseBody _endif:
       */

      currentFunction.startNewScope();

      condition.acceptVisitor(this); // Truth value on top of stack.
      if (condition instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      Register r1 = registerAllocator.getNextRegister();

      String _if = LabelGenerator.getLabel("_if");
      String _else = LabelGenerator.getLabel("_else");
      String _endif = LabelGenerator.getLabel("_endif");

      currentFunction.addInstructionToBack(new LabelInstruction(_if));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            "1"));
      registerAllocator.returnRegister(r1);
      currentFunction.addInstructionToBack(new JneInstruction(_else));
      ifBody.acceptVisitor(this); // Get ifBody instructions.
      currentFunction.addInstructionToBack(new JmpInstruction(_endif));
      currentFunction.addInstructionToBack(new LabelInstruction(_else));
      elseBody.acceptVisitor(this); // Get elseBody instructions.
      currentFunction.addInstructionToBack(new LabelInstruction(_endif));

      currentFunction.endScope();

   }

   @Override
   public void visitEventuallyNode(NonCharExpressionNode conditionNode,
         StatementListNode statList) throws SemanticException {

      /*
       * _eventually: ; condition code pop r1 ; get condition cmp r1, 1 je
       * _endloop ; if condition true, end loop ; statements jmp _eventually
       * _endloop:
       */

      currentFunction.startNewScope();

      Register r1 = registerAllocator.getNextRegister();
      String _eventually = LabelGenerator.getLabel("_eventually");
      String _endloop = LabelGenerator.getLabel("_endloop");

      currentFunction.addInstructionToBack(new LabelInstruction(_eventually));
      conditionNode.acceptVisitor(this); // Evaluate the condition
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            "1"));
      registerAllocator.returnRegister(r1);
      currentFunction.addInstructionToBack(new JeInstruction(_endloop));
      statList.acceptVisitor(this); // Get statements.
      currentFunction.addInstructionToBack(new JmpInstruction(_eventually));
      currentFunction.addInstructionToBack(new LabelInstruction(_endloop));

      currentFunction.endScope();

   }

   @Override
   public void visitFoundNode(ExpressionNode expression)
         throws SemanticException {
      // Value should be on the stack.
      // Pop it into eax for the return value.

      expression.acceptVisitor(this);
      if (expression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      currentFunction.addInstructionToBack(new PopInstruction("eax"));
      currentFunction.addInstructionToBack(new JmpInstruction(currentFunction
            .getEndLabel())); // End program.

   }

   @Override
   public void visitIdentifierNode(String identName) {

      // Get the value of the identifier and put it on the stack.

      String identLocation = currentFunction.getVariableLocation(identName);
      boolean isArray = currentFunction.variableIsArray(identName);
      Type identType = currentFunction.getVariableType(identName);

      Register r1 = registerAllocator.getNextRegister();

      if (isArray) {
         currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
               "[" + identLocation + "]")); // r1 contains address of array.
         currentFunction
               .addInstructionToBack(new PushInstruction(r1.getName()));
         // Address of array on the stack.
      } else {
         if (identType == Type.STRING) {
            currentFunction.addInstructionToBack(new MoveInstruction(r1
                  .getName(), "[" + identLocation + "]")); // r1 contains
            // address of string.
            currentFunction.addInstructionToBack(new PushInstruction(r1
                  .getName())); // Address of string on the stack.
         }
         if (identType == Type.NUMBER) {
            currentFunction.addInstructionToBack(new MoveInstruction(r1
                  .getName(), "[" + identLocation + "]")); // r1 contains value
            currentFunction.addInstructionToBack(new PushInstruction(r1
                  .getName()));
         }
         if (identType == Type.LETTER) {
            currentFunction.addInstructionToBack(new MoveInstruction(r1
                  .getByteName(), "[" + identLocation + "]")); // r1 contains
            // value
            currentFunction.addInstructionToBack(new PushInstruction(r1
                  .getName()));
         }

      }

      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitIntegerLiteralNode(int integer) {
      currentFunction.addInstructionToBack(new PushInstruction("" + integer));

   }

   @Override
   public void visitLookingGlassFunctionNode(String name, Type argType,
         StatementListNode functionBody) throws SemanticException,
         FileNotFoundException {

      currentFunction = new Function(name, argType);
      functionTable.addFunction(currentFunction);

      currentFunction.addArgument("it", Type.NUMBER, true);
      String itLocation = currentFunction.getVariableLocation("it");

      functionBody.acceptVisitor(this);
      currentFunction.addInstructionToBack(new MoveInstruction("eax", "["
            + itLocation + "]"));
   }

   @Override
   public void visitMainNode(StatementListNode statListNode)
         throws SemanticException {
      statListNode.acceptVisitor(this);
   }

   @Override
   public void visitMinusExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {

      e1.acceptVisitor(this);
      if (e1 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      e2.acceptVisitor(this);
      if (e2 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      // PRE The evaluated values of e1, and e2 are sat on top of the stack
      // Pop the two values off the stack, subtract them together and push onto
      // the stack

      Register leftOperand = registerAllocator.getNextRegister();
      Register rightOperand = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(rightOperand
            .getName()));
      currentFunction.addInstructionToBack(new PopInstruction(leftOperand
            .getName()));
      currentFunction.addInstructionToBack(new SubInstruction(leftOperand
            .getName(), rightOperand.getName()));
      currentFunction.addInstructionToBack(new PushInstruction(leftOperand
            .getName()));

      registerAllocator.returnRegister(rightOperand);
      registerAllocator.returnRegister(leftOperand);

   }

   @Override
   public void visitMultExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      e1.acceptVisitor(this);
      if (e1 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      e2.acceptVisitor(this);
      if (e2 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      // PRE The evaluated values of e1, and e2 are sat on top of the stack
      // Pop the two values off the stack, mult them together and push onto
      // the stack

      Register register1 = registerAllocator.getNextRegister();
      Register register2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(register1
            .getName()));
      currentFunction.addInstructionToBack(new PopInstruction(register2
            .getName()));
      currentFunction.addInstructionToBack(new MulInstruction(register2
            .getName(), register1.getName()));
      currentFunction.addInstructionToBack(new PushInstruction(register2
            .getName()));

      registerAllocator.returnRegister(register2);
      registerAllocator.returnRegister(register1);
   }

   @Override
   public void visitNotBoolExpression(NonCharExpressionNode expression)
         throws SemanticException {

      /*
       * cmp r1, 1 jge _false mov r1, 1 jmp _true _false: mov r1, 0 _true: push
       * r1; return 1 if true, 0 if false
       */

      expression.acceptVisitor(this);
      if (expression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }

      Register r1 = registerAllocator.getNextRegister();

      String _true = LabelGenerator.getLabel("_true");
      String _false = LabelGenerator.getLabel("_false");

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new CompareInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new JgeInstruction(_false));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "1"));
      currentFunction.addInstructionToBack(new JmpInstruction(_true));
      currentFunction.addInstructionToBack(new LabelInstruction(_false));
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "0"));
      currentFunction.addInstructionToBack(new LabelInstruction(_true));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r1);
   }

   @Override
   public void visitPerhapsNode(NonCharExpressionNode condition,
         StatementListNode ifBody, ElseNode elseBody) throws SemanticException {

      if (elseBody instanceof StatementListNode) {
         /**
          * elseBody could be a StatementListNode in which case we have an if,
          * else structure.
          */

         /*
          * _if: cmp r1, 1 jne _else ; ifBody jmp _endif _else: ; elseBody
          * _endif:
          */

         currentFunction.startNewScope();

         Register r1 = registerAllocator.getNextRegister();

         String _if = LabelGenerator.getLabel("_if");
         String _else = LabelGenerator.getLabel("_else");
         String _endif = LabelGenerator.getLabel("_endif");

         currentFunction.addInstructionToBack(new LabelInstruction(_if));
         condition.acceptVisitor(this); // Truth value on top of stack.
         if (condition instanceof RoomCallNode) {
            currentFunction.addInstructionToBack(new PushInstruction("eax"));
            // Expression's value is on top of the stack
         }
         currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
         currentFunction.addInstructionToBack(new CompareInstruction(r1
               .getName(), "1"));
         registerAllocator.returnRegister(r1);
         currentFunction.addInstructionToBack(new JneInstruction(_else));
         ifBody.acceptVisitor(this); // Get ifBody instructions.
         currentFunction.addInstructionToBack(new JmpInstruction(_endif));
         currentFunction.addInstructionToBack(new LabelInstruction(_else));
         elseBody.acceptVisitor(this); // Get elseBody instructions.
         currentFunction.addInstructionToBack(new LabelInstruction(_endif));

         currentFunction.endScope();

      }

      else if (elseBody instanceof PerhapsNode) {
         /**
          * or it could be a PerhapsNode, in which case we have an a) if, else
          * if structure b) if, else if, else s
          **/

         /*
          * _if1: cmp r1, 1 jne _endif ; ifBody _endif: _if2: ......
          */

         currentFunction.startNewScope();

         Register r1 = registerAllocator.getNextRegister();

         String _if = LabelGenerator.getLabel("_if");
         String _else = LabelGenerator.getLabel("_else");
         String _endPerhaps = LabelGenerator.getLabel("_endPerhaps");

         currentFunction.addInstructionToBack(new LabelInstruction(_if));
         condition.acceptVisitor(this); // Truth value on top of stack.
         if (condition instanceof RoomCallNode) {
            currentFunction.addInstructionToBack(new PushInstruction("eax"));
            // Expression's value is on top of the stack
         }
         currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
         currentFunction.addInstructionToBack(new CompareInstruction(r1
               .getName(), "1"));
         registerAllocator.returnRegister(r1);
         currentFunction.addInstructionToBack(new JneInstruction(_else));
         ifBody.acceptVisitor(this); // Get ifBody instructions.
         currentFunction.addInstructionToBack(new JmpInstruction(_endPerhaps));
         currentFunction.addInstructionToBack(new LabelInstruction(_else));
         elseBody.acceptVisitor(this);
         currentFunction
               .addInstructionToBack(new LabelInstruction(_endPerhaps));

         currentFunction.endScope();

      }

      else {
         /**
          * otherwise, ElseNode is null and we have a simple if statement.
          */

         /*
          * _if1: cmp r1, 1 jne _endif ; ifBody _endif:
          */

         currentFunction.startNewScope();

         Register r1 = registerAllocator.getNextRegister();

         String _if = LabelGenerator.getLabel("_if");
         String _endif = LabelGenerator.getLabel("_endif");

         currentFunction.addInstructionToBack(new LabelInstruction(_if));
         condition.acceptVisitor(this); // Truth value on top of stack.
         if (condition instanceof RoomCallNode) {
            currentFunction.addInstructionToBack(new PushInstruction("eax"));
            // Expression's value is on top of the stack
         }
         currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
         currentFunction.addInstructionToBack(new CompareInstruction(r1
               .getName(), "1"));
         registerAllocator.returnRegister(r1);
         currentFunction.addInstructionToBack(new JneInstruction(_endif));
         ifBody.acceptVisitor(this); // Get ifBody instructions.
         currentFunction.addInstructionToBack(new LabelInstruction(_endif));

         currentFunction.endScope();

      }

   }

   @Override
   public void visitPlusExpressionNode(NumericalExpressionNode e1,
         NumericalExpressionNode e2) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      e1.acceptVisitor(this);
      if (e1 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      e2.acceptVisitor(this);
      if (e2 instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack
      }
      // PRE The evaluated values of e1, and e2 are sat on top of the stack
      // Pop the two values off the stack, add them together and push onto
      // the stack

      // ASSEMBLY CODE
      Register register1 = registerAllocator.getNextRegister();
      Register register2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(register1
            .getName()));
      currentFunction.addInstructionToBack(new PopInstruction(register2
            .getName()));
      currentFunction.addInstructionToBack(new AddInstruction(register1
            .getName(), register2.getName()));
      currentFunction.addInstructionToBack(new PushInstruction(register1
            .getName()));

      registerAllocator.returnRegister(register2);
      registerAllocator.returnRegister(register1);
      // END ASSEMBLY CODE

   }

   @Override
   public void visitProgramNode(MainNode mainNode,
         LinkedList<FunctionNode> functionList) throws SemanticException,
         FileNotFoundException {

      Iterator<FunctionNode> iterator = functionList.iterator();
      while (iterator.hasNext()) {
         FunctionNode functionNode = iterator.next();
         // Create a new function entry based on function type.

         // Then defer to acceptVisitor.
         functionNode.acceptVisitor(this);
      }

      currentFunction = new Function("main", null);
      functionTable.addFrontFunction(currentFunction);
      mainNode.acceptVisitor(this);

   }

   public void visitRoomCallNode(String functionName,
         LinkedList<ParameterNode> params) throws SemanticException {

      // Push parameters onto the stack, right-to-left.
      // Call function.
      // Return value in eax.

      registerAllocator.pushRegistersInUse(currentFunction);

      int argumentsTotalBytes = 0;

      Iterator<ParameterNode> paramIterator = params.iterator();
      while (paramIterator.hasNext()) {
         if (paramIterator.next().getValue() instanceof CharacterLiteralNode) {
            argumentsTotalBytes += 1;
         } else {
            argumentsTotalBytes += 4;
         }
      }

      // registerAllocator.pushRegistersInUse(currentFunction);

      Iterator<ParameterNode> iterator = params.descendingIterator();

      while (iterator.hasNext()) {
         ParameterNode param = iterator.next();
         param.acceptVisitor(this); // Push each param onto stack.
         if (param.getValue() instanceof RoomCallNode) {
            currentFunction.addInstructionToBack(new PushInstruction("eax"));
         }
      }

      currentFunction.addInstructionToBack(new FunctionCallInstruction(
            functionName));
      currentFunction.addInstructionToBack(new AddInstruction("esp", ""
            + argumentsTotalBytes)); // Clear parameters from the stack.

      registerAllocator.popRegistersInUse(currentFunction);

   }

   @Override
   public void visitRoomFunctionNode(String name,
         LinkedList<ArgumentDefinitionNode> arguments, Type returnType,
         StatementListNode functionBody) throws FileNotFoundException,
         SemanticException {
      currentFunction = new Function(name, returnType);
      functionTable.addFunction(currentFunction);

      // Set up a room function definition.
      // Add all arguments and instructions.

      Iterator<ArgumentDefinitionNode> iterator = arguments.iterator();

      while (iterator.hasNext()) {
         ArgumentDefinitionNode currentArgument = iterator.next();
         String varName = currentArgument.getArgName();
         Type type = currentArgument.getType();
         boolean isArray = currentArgument.isSpider();
         currentFunction.addArgument(varName, type, isArray);
      }

      functionBody.acceptVisitor(this);

   }

   @Override
   public void visitStatementListNode(StatementNode statement,
         StatementListNode rest) throws SemanticException {
      statement.acceptVisitor(this);
      if (rest != null) {
         rest.acceptVisitor(this);
      }
   }

   @Override
   public void visitStatementNode(StatementNode statement)
         throws SemanticException {
      statement.acceptVisitor(this);
   }

   public void visitSpokeNode(ExpressionNode expression)
         throws SemanticException {
      registerAllocator.pushRegistersInUse(currentFunction);
      expression.acceptVisitor(this); // Pushes value onto stack.
      Type identType;

      // Room Call

      if (expression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         RoomCallNode roomCallNode = (RoomCallNode) expression;
         String functionName = roomCallNode.getRoomFunctionName();
         if (functionName != null) {
            identType = functionTable.getFunctionReturnType(functionName);
            if (identType == Type.LETTER) {
               currentFunction
                     .addInstructionToBack(new PrintCharInstructions());
            } else if (identType == Type.NUMBER) {
               currentFunction.addInstructionToBack(new PrintIntInstructions());
            } else if (identType == Type.STRING) {
               currentFunction
                     .addInstructionToBack(new PrintStringInstructions());
            }
         }
      }

      // Identifiers

      else if (expression instanceof IdentifierNode) {
         BasicIdentifierNode ident;
         if (expression instanceof BasicIdentifierNode) {
            ident = (BasicIdentifierNode) expression;
         } else {
            ident = ((ArrayElementIdentifierNode) expression)
                  .getArrayIdentifierNode();
         }
         String identName = ident.getName();
         identType = currentFunction.getVariableType(identName);
         if (identType == Type.LETTER) {
            currentFunction.addInstructionToBack(new PrintCharInstructions());
         } else if (identType == Type.NUMBER) {
            currentFunction.addInstructionToBack(new PrintIntInstructions());
         } else if (identType == Type.STRING) {
            currentFunction.addInstructionToBack(new PrintStringInstructions());
         }
      }

      // Literals

      else if (expression instanceof StringLiteralNode) {
         // The address to the string (4 bytes) will be on the stack.
         currentFunction.addInstructionToBack(new PrintStringInstructions());
      }

      else if (expression instanceof IntegerLiteralNode) {
         // An integer value (4 bytes) will be on the stack.
         currentFunction.addInstructionToBack(new PrintIntInstructions());
      }

      else if (expression instanceof CharacterLiteralNode) {
         // A character value (1 byte) will be on the stack.
         currentFunction.addInstructionToBack(new PrintCharInstructions());
      }

      else if (expression instanceof NonCharExpressionNode) {
         currentFunction.addInstructionToBack(new PrintIntInstructions());
      }

      // Others

      registerAllocator.popRegistersInUse(currentFunction);

   }

   @Override
   public void visitAteNode(BasicIdentifierNode ident) {
      // PRE The identifier is correctly placed in the symbol table and
      // is of an incremental type

      // ASSEMBLY CODE
      String identName = ident.getName();

      Register currentRegister = registerAllocator.getNextRegister();

      String identLocation = currentFunction.getVariableLocation(identName);

      currentFunction.addInstructionToBack(new MoveInstruction(currentRegister
            .getName(), "[" + identLocation + "]"));
      currentFunction.addInstructionToBack(new IncInstruction(currentRegister
            .getName()));
      currentFunction.addInstructionToBack(new MoveInstruction("["
            + identLocation + "]", currentRegister.getName()));

      registerAllocator.returnRegister(currentRegister);
      // END ASSEMBLY CODE

   }

   @Override
   public void visitAteNode(ArrayElementIdentifierNode ident)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException {
      // Put array index on the stack.
      // Get name of the array.
      // Calculate address of the array element.
      // Increment the value at that address.

      ident.getArrayIndexNode().acceptVisitor(this); // Array index is now on
      // top of stack.
      String arrayName = ident.getArrayIdentifierNode().getName();
      String arrayLocation = currentFunction.getVariableLocation(arrayName);
      Type arrayType = currentFunction.getVariableType(arrayName);

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      // Array index in r1.
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + arrayLocation + "]")); // Array location in r2.
      if (arrayType != Type.LETTER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4"));
      }
      currentFunction.addInstructionToBack(new AddInstruction(r1.getName(), r2
            .getName())); // Array[Index] pointer in r1.
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + r1.getName() + "]")); // r2 has value of Array[Index]
      currentFunction.addInstructionToBack(new IncInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new MoveInstruction("["
            + r1.getName() + "]", r2.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitDrankNode(BasicIdentifierNode ident) {
      // ASSEMBLY CODE
      String identName = ident.getName();
      Register currentRegister = registerAllocator.getNextRegister();

      String identLocation = currentFunction.getVariableLocation(identName);

      currentFunction.addInstructionToBack(new MoveInstruction(currentRegister
            .getName(), "[" + identLocation + "]"));
      currentFunction.addInstructionToBack(new DecInstruction(currentRegister
            .getName()));
      currentFunction.addInstructionToBack(new MoveInstruction("["
            + identLocation + "]", currentRegister.getName()));

      registerAllocator.returnRegister(currentRegister);
      // END ASSEMBLY CODE
   }

   @Override
   public void visitDrankNode(ArrayElementIdentifierNode ident)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException {

      // Put array index on the stack.
      // Get name of the array.
      // Calculate address of the array element.
      // Decrement the value at that address.

      ident.getArrayIndexNode().acceptVisitor(this); // Array index is now on
      // top of stack.
      String arrayName = ident.getArrayIdentifierNode().getName();
      String arrayLocation = currentFunction.getVariableLocation(arrayName);
      Type arrayType = currentFunction.getVariableType(arrayName);

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      // Array index in r1.
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + arrayLocation + "]")); // Array location in r2.
      if (arrayType != Type.LETTER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4"));
      }
      currentFunction.addInstructionToBack(new AddInstruction(r1.getName(), r2
            .getName())); // Array[Index] pointer in r1.
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + r1.getName() + "]")); // r2 has value of Array[Index]
      currentFunction.addInstructionToBack(new DecInstruction(r2.getName()));
      currentFunction.addInstructionToBack(new MoveInstruction("["
            + r1.getName() + "]", r2.getName()));

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitLookingGlassCallNode(String functionName,
         BasicIdentifierNode identifier) {

      Register r1 = registerAllocator.getNextRegister();
      Register eax = registerAllocator.getSpecificRegister("eax");
      boolean eaxWasInUse = false;

      String identName = identifier.getName();
      String identLocation = currentFunction.getVariableLocation(identName);

      if (eax == null) {
         // Check eax wasnt in use.
         // If it was, then push it.
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         eaxWasInUse = true;
      }

      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "[" + identLocation + "]"));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));
      registerAllocator.returnRegister(r1);
      currentFunction.addInstructionToBack(new FunctionCallInstruction(
            functionName));
      currentFunction.addInstructionToBack(new AddInstruction("esp", "4"));
      currentFunction.addInstructionToBack(new MoveInstruction("["
            + identLocation + "]", "eax"));

      if (eaxWasInUse) {
         // If was in use before the function call, then get the latest value.
         currentFunction.addInstructionToBack(new PopInstruction("eax"));
      } else {
         registerAllocator.returnRegister(eax);
      }

   }

   @Override
   public void visitLookingGlassCallNode(String functionName,
         ArrayElementIdentifierNode identifier) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {

      // Get array index.
      // Get the array name.
      // Get the array location.
      // Calculate the address of the array element.
      // Push the array elements value onto the stack.
      // Call the function on it.
      // Return the modified value (eax) to the location of the array element.

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();
      Register eax = registerAllocator.getSpecificRegister("eax");
      boolean eaxWasInUse = false;

      String arrayName = identifier.getArrayIdentifierNode().getName();
      String arrayLocation = currentFunction.getVariableLocation(arrayName);
      Type arrayType = currentFunction.getVariableType(arrayName);

      identifier.getArrayIndexNode().acceptVisitor(this); // Array index is now
      // on top of stack.

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      // Array index in r1.
      currentFunction
            .addInstructionToBack(new SubInstruction(r1.getName(), "1"));

      if (arrayType != Type.LETTER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4"));
      }

      if (eax == null) {
         // Check eax wasnt in use.
         // If it was, then push it.
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         eaxWasInUse = true;
      }

      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + arrayLocation + "]")); // Array location in r2.
      currentFunction.addInstructionToBack(new AddInstruction(r1.getName(), r2
            .getName())); // Array[Index] pointer in r1.
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + r1.getName() + "]")); // Value of Array[Index] in r2.
      currentFunction.addInstructionToBack(new PushInstruction(r2.getName()));
      registerAllocator.returnRegister(r2);
      currentFunction.addInstructionToBack(new FunctionCallInstruction(
            functionName));
      currentFunction.addInstructionToBack(new MoveInstruction("["
            + r1.getName() + "]", "eax"));
      registerAllocator.returnRegister(r1);

      if (eaxWasInUse) {
         // If was in use before the function call, then get the latest value.
         currentFunction.addInstructionToBack(new PopInstruction("eax"));
      } else {
         registerAllocator.returnRegister(eax);
      }

   }

   @Override
   public void visitWhatWasNode(BasicIdentifierNode identifierNode) {

      registerAllocator.pushRegistersInUse(currentFunction);

      String identName = identifierNode.getName();
      Type identType = currentFunction.getVariableType(identName);
      String location = currentFunction.getVariableLocation(identName);

      Register r1 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new LeaInstruction(r1.getName(), "["
            + location + "]"));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      if (identType == Type.STRING) {
         currentFunction.addInstructionToBack(new ScanStringInstructions());
      }
      if (identType == Type.NUMBER) {
         currentFunction.addInstructionToBack(new ScanIntInstructions());
      }
      if (identType == Type.LETTER) {
         currentFunction.addInstructionToBack(new ScanCharInstructions());
      }

      registerAllocator.returnRegister(r1);

      registerAllocator.popRegistersInUse(currentFunction);

   }

   @Override
   public void visitWhatWasNode(ArrayElementIdentifierNode identifierNode)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException {
      registerAllocator.pushRegistersInUse(currentFunction);

      String arrayName = identifierNode.getArrayIdentifierNode().getName();
      String startOfArray = currentFunction.getVariableLocation(arrayName);
      Type arrayElementType = currentFunction.getVariableType(arrayName);
      NumericalExpressionNode arrayIndex = identifierNode.getArrayIndexNode();

      arrayIndex.acceptVisitor(this); // Array index value is on top of the
      // stack.

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      // r1 holds array index.
      currentFunction
            .addInstructionToBack(new SubInstruction(r1.getName(), "1"));
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + startOfArray + "]")); // r2 holds array address

      if (arrayElementType == Type.LETTER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "1")); // r1 holds array offset
         currentFunction.addInstructionToBack(new AddInstruction(r1.getName(),
               r2.getName())); // r1 holds ident location.
         currentFunction
               .addInstructionToBack(new PushInstruction(r1.getName()));
         // Push address of ident onto stack.
         currentFunction.addInstructionToBack(new ScanCharInstructions());
      }
      if (arrayElementType == Type.NUMBER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4")); // r1 holds array offset
         currentFunction.addInstructionToBack(new AddInstruction(r1.getName(),
               r2.getName())); // r1 holds ident location.
         currentFunction
               .addInstructionToBack(new PushInstruction(r1.getName()));
         // Push address of ident onto stack.
         currentFunction.addInstructionToBack(new ScanIntInstructions());
      }
      if (arrayElementType == Type.STRING) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4")); // r1 holds array offset
         currentFunction.addInstructionToBack(new AddInstruction(r1.getName(),
               "[" + r2.getName() + "]")); // r1 holds ident location.
         currentFunction
               .addInstructionToBack(new PushInstruction(r1.getName()));
         // Push address of ident onto stack.
         currentFunction.addInstructionToBack(new ScanStringInstructions());
      }

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

      registerAllocator.popRegistersInUse(currentFunction);
   }

   @Override
   public void visitDeclareArrayNode(BasicIdentifierNode ident,
         NumericalExpressionNode index, Type type) throws SemanticException {

      index.acceptVisitor(this); // Number of elements on top of stack.
      String arrayName = ident.getName();

      currentFunction.addLocalVariable(arrayName, type, true);
      String arrayLocation = currentFunction.getVariableLocation(arrayName);
      // Holds the address of the array.

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      // r1 holds number of elements.
      currentFunction.addInstructionToBack(new LeaInstruction(r2.getName(), "["
            + arrayLocation + "]"));

      if (type == Type.LETTER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "1")); // r1 holds array size
         registerAllocator.pushRegistersInUse(currentFunction);
         currentFunction
               .addInstructionToBack(new PushInstruction(r1.getName()));
         // Push number of bytes onto stack.
         currentFunction.addInstructionToBack(new FunctionCallInstruction(
               "malloc"));
         currentFunction.addInstructionToBack(new AddInstruction("esp", "4"));
         registerAllocator.popRegistersInUse(currentFunction);
         currentFunction.addInstructionToBack(new MoveInstruction("["
               + r2.getName() + "]", "eax")); // Now holds address allocated by
         // malloc
      } else {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4")); // r1 holds array size
         registerAllocator.pushRegistersInUse(currentFunction);
         currentFunction
               .addInstructionToBack(new PushInstruction(r1.getName()));
         // Push number of bytes onto stack.
         currentFunction.addInstructionToBack(new FunctionCallInstruction(
               "malloc"));
         currentFunction.addInstructionToBack(new AddInstruction("esp", "4"));
         registerAllocator.popRegistersInUse(currentFunction);
         currentFunction.addInstructionToBack(new MoveInstruction("["
               + r2.getName() + "]", "eax")); // Now holds address allocated by
         // malloc
      }
      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitDeclareBasicNode(BasicIdentifierNode ident, Type type) {

      String identName = ident.getName();
      currentFunction.addLocalVariable(identName, type, false);
      String location = currentFunction.getVariableLocation(identName);
      if (type == Type.NUMBER) {
         currentFunction.addInstructionToBack(new MoveInstruction("dword ["
               + location + "]", "0")); // initialse integers as 0
      }

   }

   @Override
   public void visitStringLiteralNode(String constant) {

      constant = constant.replace("\\n", '"' + ",0Dh, 0Ah," + '"');
      // Replace with assembly new line.

      String label = LabelGenerator.getLabel("string");
      functionTable.addData(new DeclareStringInstruction(label, constant));

      currentFunction.addInstructionToBack(new PushInstruction(label));
      // address of string now on the stack.

   }

   @Override
   public void visitArrayElementIdentifierNode(
         BasicIdentifierNode arrayIdentifierNode,
         NumericalExpressionNode arrayIndexNode) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {

      arrayIndexNode.acceptVisitor(this); // Array index is now on top of stack.
      String arrayName = arrayIdentifierNode.getName();
      String arrayLocation = currentFunction.getVariableLocation(arrayName);
      Type arrayType = currentFunction.getVariableType(arrayName);

      Register r1 = registerAllocator.getNextRegister();
      Register r2 = registerAllocator.getNextRegister();

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      // Array index in r1.
      currentFunction
            .addInstructionToBack(new SubInstruction(r1.getName(), "1"));
      if (arrayType != Type.LETTER) {
         currentFunction.addInstructionToBack(new MulInstruction(r1.getName(),
               "4")); // Array offset in r1.
      }
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + arrayLocation + "]")); // Array location in r2.
      currentFunction.addInstructionToBack(new AddInstruction(r1.getName(), r2
            .getName())); // Array[Index] pointer in r1.
      currentFunction.addInstructionToBack(new MoveInstruction(r2.getName(),
            "[" + r1.getName() + "]")); // r2 has value of Array[Index]
      currentFunction.addInstructionToBack(new PushInstruction(r2.getName()));
      // Value of array now on the stack.

      registerAllocator.returnRegister(r2);
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitModExpressionNode(
         NumericalExpressionNode leftChildExpression,
         NumericalExpressionNode rightChildExpression)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException {

      Register eax = registerAllocator.getSpecificRegister("eax");
      Register edx = registerAllocator.getSpecificRegister("edx");
      Register r1 = registerAllocator.getNextRegister();

      boolean eaxWasInUse = false;
      boolean edxWasInUse = false;

      // Check eax and edx not in use.

      if (eax == null) {
         eaxWasInUse = true;
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
      }
      if (edx == null) {
         edxWasInUse = true;
         currentFunction.addInstructionToBack(new PushInstruction("edx"));
      }

      rightChildExpression.acceptVisitor(this);
      if (rightChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack.
      }

      leftChildExpression.acceptVisitor(this);
      if (leftChildExpression instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack.
      }

      currentFunction.addInstructionToBack(new MoveInstruction("edx", "0"));
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new MoveInstruction("eax", r1
            .getName()));

      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new DivInstruction(r1.getName()));
      // eax now holds remainder.
      currentFunction.addInstructionToBack(new MoveInstruction(r1.getName(),
            "edx"));
      // Restore registers
      if (edxWasInUse) {
         currentFunction.addInstructionToBack(new PopInstruction("edx"));
      } else {
         registerAllocator.returnRegister(edx);
      }
      if (eaxWasInUse) {
         currentFunction.addInstructionToBack(new PopInstruction("eax"));
      } else {
         registerAllocator.returnRegister(eax);
      }
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));

      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitNegateIntegerNode(NumericalExpressionNode numExpressionNode)
         throws TypeMatchException, IdentifierNotDeclaredException,
         SemanticException {
      // Use negate instruction.
      // Push the negated value back on the stack.
      numExpressionNode.acceptVisitor(this);
      if (numExpressionNode instanceof RoomCallNode) {
         currentFunction.addInstructionToBack(new PushInstruction("eax"));
         // Expression's value is on top of the stack.
      }
      Register r1 = registerAllocator.getNextRegister();
      currentFunction.addInstructionToBack(new PopInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new NegInstruction(r1.getName()));
      currentFunction.addInstructionToBack(new PushInstruction(r1.getName()));
      registerAllocator.returnRegister(r1);

   }

   @Override
   public void visitArgumentDefinitionNode(String argName, Type argType,
         boolean isSpider) throws SemanticException {
      // nothing to do
   }
}
