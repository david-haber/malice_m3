package intermediaryGeneration.instructions.arithmetic;

import intermediaryGeneration.instructions.Instruction;

public class DivInstruction extends Instruction {

   String operand1;

   public DivInstruction(String operand1) {
      this.operand1 = operand1;

   }

   @Override
   public String toString() {
      return "idiv " + operand1;
   }

}
