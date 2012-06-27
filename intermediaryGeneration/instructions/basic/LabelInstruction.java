package intermediaryGeneration.instructions.basic;

import intermediaryGeneration.instructions.Instruction;

public class LabelInstruction extends Instruction {
   private String label;

   public LabelInstruction(String label) {
      super();
      this.label = label;
   }

   public String getLabel() {
      return label;
   }

   @Override
   public String toString() {
      return "\n" + label + ":";
   }

}
