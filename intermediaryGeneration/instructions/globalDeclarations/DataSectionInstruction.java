package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class DataSectionInstruction extends Instruction {
   private String data;

   public DataSectionInstruction() {
      data = "SECTION .data";
   }

   @Override
   public String toString() {
      return data;
   }

}
