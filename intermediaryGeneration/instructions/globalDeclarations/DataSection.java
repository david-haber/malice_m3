package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class DataSection extends Instruction {
   private String data;

   public DataSection() {
      data = "SECTION .data";
   }

   @Override
   public String toString() {
      return data;
   }

}
