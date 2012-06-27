package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class GlobalDeclaration extends Instruction {
   private String label;

   public GlobalDeclaration(String label) {
      this.label = label;
   }

   @Override
   public String toString() {
      return "global" + label;
   }

}
