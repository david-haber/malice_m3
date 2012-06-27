package intermediaryGeneration.instructions.arithmetic;

import intermediaryGeneration.instructions.Instruction;

public class NegInstruction extends Instruction {

   String operand;

   public NegInstruction(String operand) {
      this.operand = operand;
   }

   @Override
   public String toString() {
      return "neg " + operand;

   }

}
