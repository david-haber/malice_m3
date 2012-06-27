package intermediaryGeneration.instructions.jump;

import intermediaryGeneration.instructions.Instruction;

public abstract class JumpInstruction extends Instruction {

   private String dstLabel;

   public JumpInstruction(String dstLabel) {
      this.dstLabel = dstLabel;
   }

   public String getLabel() {
      return dstLabel;
   }

}
