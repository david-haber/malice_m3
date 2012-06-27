package intermediaryGeneration.instructions.arithmetic;

import intermediaryGeneration.instructions.Instruction;

public class AddInstruction extends Instruction {

   String operand1;
   String operand2;

   public AddInstruction(String operand1, String operand2) {
      this.operand1 = operand1;
      this.operand2 = operand2;
   }

   @Override
   public String toString() {
      return "add " + operand1 + "," + operand2;
   }

}
