package intermediaryGeneration.instructions.basic;

import intermediaryGeneration.instructions.Instruction;

public class MoveInstruction extends Instruction {

   private String to;
   private String from;

   public MoveInstruction(String to, String from) {
      this.to = to;
      this.from = from;
   }

   @Override
   public String toString() {
      return "mov " + to + ", " + from;
   }

   public Object getOperand1() {
      return this.to;
   }

   public Object getOperand2() {
      return this.from;
   }

}
