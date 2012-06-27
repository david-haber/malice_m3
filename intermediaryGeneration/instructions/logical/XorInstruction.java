package intermediaryGeneration.instructions.logical;

import intermediaryGeneration.instructions.Instruction;

public class XorInstruction extends Instruction {

   String operand1;
   String operand2;

   public XorInstruction(String operand1, String operand2) {
      this.operand1 = operand1;
      this.operand2 = operand2;
   }

   @Override
   public String toString() {
      return "xor " + operand1 + "," + operand2;
   }

}
