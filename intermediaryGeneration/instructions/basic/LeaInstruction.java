package intermediaryGeneration.instructions.basic;

import intermediaryGeneration.instructions.Instruction;

public class LeaInstruction extends Instruction {

   private String op1;
   private String op2;

   public LeaInstruction(String op1, String op2) {
      this.op1 = op1;
      this.op2 = op2;
   }

   @Override
   public String toString() {
      return "lea " + op1 + ", " + op2;
   }

}
