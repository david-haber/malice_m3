package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class GlobalDeclarationInstruction extends Instruction {
   private String label;

   public GlobalDeclarationInstruction(String label) {
      this.label = label;
   }

   @Override
   public String toString() {
      return "global " + label;
   }

}
