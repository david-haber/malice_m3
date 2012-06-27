package intermediaryGeneration.instructions.jump;

public class JeInstruction extends JumpInstruction {

   public JeInstruction(String operand) {
      super(operand);
   }

   @Override
   public String toString() {
      return "je " + getLabel();
   }

}
