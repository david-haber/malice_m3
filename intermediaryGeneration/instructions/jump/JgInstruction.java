package intermediaryGeneration.instructions.jump;

public class JgInstruction extends JumpInstruction {

   public JgInstruction(String operand) {
      super(operand);
   }

   @Override
   public String toString() {
      return "jg " + getLabel();
   }

}
