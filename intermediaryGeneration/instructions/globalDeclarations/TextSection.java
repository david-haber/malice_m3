package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class TextSection extends Instruction {
   private String text;

   public TextSection() {
      text = "SECTION .text";
   }

   @Override
   public String toString() {
      return text;
   }

}
