package intermediaryGeneration.instructions.jump;

public class JmpInstruction extends JumpInstruction {

   public JmpInstruction(String operand) {
      super(operand);
   }

   @Override
   public String toString() {
      return "jmp " + getLabel();
   }

}
