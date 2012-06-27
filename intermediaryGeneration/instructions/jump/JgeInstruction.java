package intermediaryGeneration.instructions.jump;

public class JgeInstruction extends JumpInstruction {

   public JgeInstruction(String operand) {
      super(operand);
   }

   @Override
   public String toString() {
      return "jge " + getLabel();
   }

}
