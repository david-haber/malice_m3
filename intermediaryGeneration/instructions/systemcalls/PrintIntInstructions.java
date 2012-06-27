package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class PrintIntInstructions extends Instruction {

   @Override
   public String toString() {
      return "push integer_printf \n" + "call printf \n" + "add esp, 8 \n"
            + "push 0 \n" + "call fflush \n" + "add esp, 4 \n";
   }

}
