package intermediaryGeneration;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import intermediaryGeneration.instructions.Instruction;
import intermediaryGeneration.instructions.arithmetic.AddInstruction;
import intermediaryGeneration.instructions.arithmetic.SubInstruction;
import intermediaryGeneration.instructions.basic.LabelInstruction;
import intermediaryGeneration.instructions.basic.MoveInstruction;
import intermediaryGeneration.instructions.basic.PopInstruction;
import intermediaryGeneration.instructions.globalDeclarations.GlobalDeclarationInstruction;
import intermediaryGeneration.instructions.logical.PushInstruction;
import intermediaryGeneration.instructions.systemcalls.FunctionCallInstruction;
import intermediaryGeneration.instructions.systemcalls.KernelCallInstruction;
import intermediaryGeneration.instructions.systemcalls.RetInstruction;
import ast.Type;

public class Function {

   private String label;
   private Type returnType; // Type of the returned value.
   private LocalVariables localVariables; // List of local variables.
   private LinkedList<Instruction> bodyInstructions; // Instructions.
   private int localVariableByteSize; // Offset for base pointer local vars.
                                      // (ebp-x)
   private int parameterOffset; // Offset for parameters (ebp+x)
   private Stack<LocalVariables> scopeStack;
   private LinkedList<String> arrays;
   private String endLabel;

   public Function(String label, Type returnType) throws FileNotFoundException {
      super();
      this.label = label;
      this.returnType = returnType;
      localVariables = new LocalVariables();
      bodyInstructions = new LinkedList<Instruction>();
      localVariableByteSize = 0; // start at ebp
      parameterOffset = 8; // starts at ebp+8
      scopeStack = new Stack<LocalVariables>();
      endLabel = LabelGenerator.getLabel("end");
      arrays = new LinkedList<String>();
   }

   public Type getReturnType() {
      return returnType;
   }

   public String getLabel() {
      return label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public void addInstructionToBack(Instruction instruction) {
      // Puts instruction at the bottom of instruction list.
      bodyInstructions.addLast(instruction);
   }

   public void addInstructionToFront(Instruction instruction) {
      // Puts instruction at the top of instruction list.
      bodyInstructions.addFirst(instruction);
   }

   public void addLocalVariable(String identName, Type type, boolean isArray) {
      // Add to symbol table.
      int size;
      if (type == Type.LETTER && !isArray) {
         size = 1; // WRONG
      } else {
         size = 4;
      }
      localVariableByteSize += size;
      localVariables.addVariable(identName, ("ebp-" + localVariableByteSize),
            type, isArray);
      if (isArray) {
         arrays.add(identName);
      }
   }

   public void addLocalVariable(String identName, Type type, String location,
         boolean isArray) {
      // Add to symbol table.
      localVariables.addVariable(identName, location, type, isArray);
   }

   public void addArgument(String identName, Type type, boolean isArray) {
      // Add a parameter.
      localVariables.addVariable(identName, ("ebp+" + parameterOffset), type,
            isArray);
      int size;
      if (type == Type.LETTER) {
         size = 1;
      } else {
         size = 4;
      }
      parameterOffset += size;
   }

   public void startNewScope() {
      scopeStack.push((LocalVariables) localVariables.clone());
   }

   public void endScope() {
      localVariables = scopeStack.pop();
   }

   public String getVariableLocation(String identName) {
      // Return the address of the variable i.e. ebp-4
      return localVariables.getVariableLocation(identName);
   }

   public Type getVariableType(String identName) {
      return localVariables.getVariableType(identName);
   }

   public boolean variableIsArray(String identName) {
      return localVariables.isArray(identName);
   }

   public String getEndLabel() {
      return endLabel;
   }

   public LinkedList<Instruction> getInstructions() throws FileNotFoundException {

      LinkedList<Instruction> returnInstructions = new LinkedList<Instruction>();

      // Set up stack frame.
      allocateStackFrame(returnInstructions);

      // Add instruction body.
      returnInstructions.addAll(bodyInstructions);
      returnInstructions.addFirst(new GlobalDeclarationInstruction(
            label));

      returnInstructions.addLast(new LabelInstruction(endLabel));
      deAllocateStackFrame(returnInstructions);

      if (label != "main") {
         returnInstructions.addLast(new RetInstruction());
      } else {
         exitKernelCall(0, returnInstructions);
      }

      return returnInstructions;
   }

   private void allocateStackFrame(LinkedList<Instruction> instructions) {
      instructions.addFirst(new SubInstruction("esp", ""
            + localVariableByteSize));
      instructions.addFirst(new MoveInstruction("ebp", "esp"));
      instructions.addFirst(new PushInstruction("ebp\n"));
      instructions.addFirst(new LabelInstruction(label));
   }

   private void deAllocateStackFrame(LinkedList<Instruction> instructions) {
      Iterator<String> iterator = arrays.iterator();
      while (iterator.hasNext()) {
         instructions.addLast(new PushInstruction("dword " + "["
               + localVariables.getVariableLocation(iterator.next()) + "]"));
         instructions.addLast(new FunctionCallInstruction("free"));
         instructions.addLast(new AddInstruction("esp", "4"));
      }
      instructions.addLast(new AddInstruction("esp", ""
            + localVariableByteSize));
      instructions.addLast(new PopInstruction("ebp"));
   }

   private void exitKernelCall(int code, LinkedList<Instruction> instructions) {
      instructions.addLast(new MoveInstruction("ebx", "" + code));
      instructions.addLast(new KernelCallInstruction());
   }

}
