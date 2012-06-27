package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class PrintCharInstructions extends Instruction {

   @Override
   public String toString() {
      return "push char_printf \n" + "call printf \n" + "add esp, 8 \n"
            + "push 0 \n" + "call fflush \n" + "add esp, 4 \n";

   }

}
