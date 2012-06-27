package intermediaryGeneration.instructions.basic;

import intermediaryGeneration.instructions.Instruction;

public class PopInstruction extends Instruction {

   private String operand;

   public PopInstruction(String operand) {
      this.operand = operand;
   }

   public String getOperand() {
      return this.operand;
   }

   @Override
   public String toString() {
      return "pop " + this.operand;
   }

}
