package intermediaryGeneration.instructions.arithmetic;

import intermediaryGeneration.instructions.Instruction;

public class IncInstruction extends Instruction {

   String operand;

   public IncInstruction(String operand) {
      this.operand = operand;
   }

   @Override
   public String toString() {
      return "inc " + operand;
   }

}
