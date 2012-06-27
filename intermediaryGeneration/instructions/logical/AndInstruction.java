package intermediaryGeneration.instructions.logical;

import intermediaryGeneration.instructions.Instruction;

public class AndInstruction extends Instruction {

   String operand1;
   String operand2;

   public AndInstruction(String operand1, String operand2) {
      this.operand1 = operand1;
      this.operand2 = operand2;
   }

   @Override
   public String toString() {
      return "and " + operand1 + "," + operand2;
   }

}
