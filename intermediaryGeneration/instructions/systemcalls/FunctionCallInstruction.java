package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class FunctionCallInstruction extends Instruction {

   private String operand;

   public FunctionCallInstruction(String operand) {
      this.operand = operand;
   }

   @Override
   public String toString() {
      return "call " + operand;
   }

}
