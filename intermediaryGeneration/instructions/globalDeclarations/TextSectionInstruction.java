package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class TextSectionInstruction extends Instruction {
   private String text;

   public TextSectionInstruction() {
      text = "SECTION .text";
   }

   @Override
   public String toString() {
      return text;
   }

}
