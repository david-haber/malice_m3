package intermediaryGeneration.instructions.jump;

public class JneInstruction extends JumpInstruction {

   public JneInstruction(String operand) {
      super(operand);
   }

   @Override
   public String toString() {
      return "jne " + getLabel();
   }

}
