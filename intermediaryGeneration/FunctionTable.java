package intermediaryGeneration;

import intermediaryGeneration.instructions.Instruction;

import intermediaryGeneration.instructions.basic.MoveInstruction;
import intermediaryGeneration.instructions.basic.PopInstruction;
import intermediaryGeneration.instructions.globalDeclarations.DataSectionInstruction;
import intermediaryGeneration.instructions.globalDeclarations.DeclareStringInstruction;
import intermediaryGeneration.instructions.globalDeclarations.ExternInstruction;
import intermediaryGeneration.instructions.globalDeclarations.TextSectionInstruction;
import intermediaryGeneration.instructions.logical.PushInstruction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import ast.Type;

public class FunctionTable {
   private LinkedList<Instruction> data;
   private LinkedList<String> dataDeclaredSoFar;
   private LinkedList<Instruction> text;
   private LinkedList<Function> functions;

   public FunctionTable() throws FileNotFoundException {
      functions = new LinkedList<Function>();
      dataDeclaredSoFar = new LinkedList<String>();
      data = new LinkedList<Instruction>();
      declareDataSection();
      text = new LinkedList<Instruction>();
      declareTextSection();

   }

   public Function lookUpFunction(String functionName) {
      Iterator<Function> iterator = functions.iterator();

      while (iterator.hasNext()) {
         Function currentFunction = iterator.next();
         if (currentFunction.getLabel() == functionName) {
            return currentFunction;
         }
      }
      return null;
   }

   private void declareDataSection() {
      data.addLast(new DataSectionInstruction());
      data.addLast(new DeclareStringInstruction("integer_printf",
            "%i", false));
      data.addLast(new DeclareStringInstruction("string", "%s",
            false));
      data.addLast(new DeclareStringInstruction("char_printf", "%c",
            false));
      data.addLast(new DeclareStringInstruction("char_scanf", "%c",
            false));
      data.addLast(new DeclareStringInstruction("integer_scanf",
            "%i", false));
   }

   private void declareTextSection() {
      text.addLast(new TextSectionInstruction());
   }

   public void addFrontFunction(Function function) {
      functions.addFirst(function);
   }

   public void addFunction(Function function) {
      functions.add(function);
   }

   public void addData(DeclareStringInstruction instruction) {
      data.addLast(instruction);
      dataDeclaredSoFar.add(instruction.getString());

   }

   public Type getFunctionReturnType(String functionName) {
      Iterator<Function> iterator = functions.iterator();
      Function currentFunction;
      while (iterator.hasNext()) {
         currentFunction = iterator.next();
         String label = currentFunction.getLabel();
         if (label != null && label.equals(functionName)) {
            return currentFunction.getReturnType();
         }
      }

      return null;

   }

   public void optimise(LinkedList<Instruction> instructions) {
      removeNeutralisedInstructions(instructions);
   }
   
   /**
    *  removeNeutralisedInstructions - Optimisation Function
    *  Remove adjacent instructions that cancel each other out
    *  E.g. push eax, pop eax
    *  Processes the instruction list repeatedly until after one 
    *  full iteration, no change has been made.
    */
   
   public void removeNeutralisedInstructions(LinkedList<Instruction> instructions) {
      Instruction first;
      Instruction next;
      int index;
      boolean changeMade = true;

      while (changeMade == true) {
         changeMade = false;
         index = 0;
         // Iterate over list
         while (index < instructions.size()-1) {
            first = instructions.get(index);
            next = instructions.get(index+1);
            if ((first instanceof PushInstruction) && 
                                            (next instanceof PopInstruction)) {
               
               if (((PushInstruction)first).getOperand().
                                 equals(((PopInstruction)next).getOperand())) {
                  // Two adjacent instructions cancel each other out, remove
                  instructions.remove(index);
                  instructions.remove(index);
                  changeMade = true;
                  break;
               }
            } else if ((first instanceof PopInstruction) &&
                                           (next instanceof PushInstruction)) {
               if (((PopInstruction)first).getOperand().
                                equals(((PushInstruction)next).getOperand())) {
                  // Two adjacent instructions cancel each other out, remove
                  instructions.remove(index);
                  instructions.remove(index);
                  changeMade = true;
                  break;
               }
            } else if ((first instanceof MoveInstruction) && 
                                           (next instanceof MoveInstruction)) {
               if (((MoveInstruction)first).getOperand1().
                                equals(((MoveInstruction)next).getOperand2())
                     && (((MoveInstruction)first).getOperand2().
                              equals(((MoveInstruction)next).getOperand1()))) {
                  // Two adjacent instructions cancel each other out, remove
                  instructions.remove(index);
                  instructions.remove(index);
                  changeMade = true;
                  break;
               }
            }
            index++;
         }
      }
   }

   public void writeToFile(String fileName) throws IOException {
      
      LinkedList<Instruction> allInstructions = new LinkedList<Instruction>();

      addExternalDeclarations(allInstructions);
      allInstructions.addAll(data);
      allInstructions.addAll(text);
      
      for (Function f : functions) {
         allInstructions.addAll(f.getInstructions());
      }
      
      // OPTIMISE
      optimise(allInstructions);
      
      CodeWriter cw = new CodeWriter();
      for (Instruction instr : allInstructions) {
         cw.writeLineToFile(instr.toString());
      }

   }

   private void addExternalDeclarations(LinkedList<Instruction> instructions) {
      instructions.addLast(new ExternInstruction("printf"));
      instructions.addLast(new ExternInstruction("scanf"));
      instructions.addLast(new ExternInstruction("malloc"));
      instructions.addLast(new ExternInstruction("fflush"));
      instructions.addLast(new ExternInstruction("free"));
   }

   {

   }
}
