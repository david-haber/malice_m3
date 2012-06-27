package intermediaryGeneration.instructions.jump;

public class JleInstruction extends JumpInstruction {

   public JleInstruction(String operand) {
      super(operand);
   }

   @Override
   public String toString() {
      return "jle " + getLabel();
   }

}
