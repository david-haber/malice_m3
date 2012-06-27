package intermediaryGeneration.instructions.arithmetic;

import intermediaryGeneration.instructions.Instruction;

public class SubInstruction extends Instruction {
   String leftOperand;
   String rightOperand;

   public SubInstruction(String leftOperand, String rightOperand) {
      this.leftOperand = leftOperand;
      this.rightOperand = rightOperand;
   }

   @Override
   public String toString() {
      return "sub " + leftOperand + "," + rightOperand;
   }

}
