package intermediaryGeneration.instructions.logical;

import intermediaryGeneration.instructions.Instruction;

public class NotInstruction extends Instruction {

   String operand;

   public NotInstruction(String operand) {
      this.operand = operand;
   }

   @Override
   public String toString() {
      return "not " + operand;
   }

}
