package intermediaryGeneration.instructions.logical;

import intermediaryGeneration.instructions.Instruction;

public class PushInstruction extends Instruction {

   private String operand;

   public PushInstruction(String operand) {
      this.operand = operand;
   }

   public String getOperand() {
      return this.operand;
   }

   @Override
   public String toString() {
      return "push " + this.operand;
   }

}
