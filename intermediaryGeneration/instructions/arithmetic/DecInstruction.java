package intermediaryGeneration.instructions.arithmetic;

import intermediaryGeneration.instructions.Instruction;

public class DecInstruction extends Instruction {

   String operand;

   public DecInstruction(String operand) {
      this.operand = operand;
   }

   @Override
   public String toString() {
      return "dec " + operand;

   }

}
