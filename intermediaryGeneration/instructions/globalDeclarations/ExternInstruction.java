package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class ExternInstruction extends Instruction {
   String label;

   public ExternInstruction(String label) {
      super();
      this.label = label;
   }

   @Override
   public String toString() {
      return "extern " + label;
   }
}
