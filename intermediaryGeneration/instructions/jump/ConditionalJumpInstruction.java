package intermediaryGeneration.instructions.jump;

public abstract class ConditionalJumpInstruction extends JumpInstruction {

   public ConditionalJumpInstruction(String dstLabel) {
      super(dstLabel);
   }

}
