package intermediaryGeneration.instructions.jump;

public class JlInstruction extends JumpInstruction {

   public JlInstruction(String operand) {
      super(operand);
   }

   @Override
   public String toString() {
      return "jl " + getLabel();
   }

}
