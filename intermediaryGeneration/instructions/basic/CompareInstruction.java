package intermediaryGeneration.instructions.basic;

import intermediaryGeneration.instructions.Instruction;

public class CompareInstruction extends Instruction {

   String operand;
   String operand2;

   public CompareInstruction(String operand, String operand2) {
      this.operand = operand;
      this.operand2 = operand2;
   }

   @Override
   public String toString() {
      return "cmp " + operand + "," + operand2;
   }

}