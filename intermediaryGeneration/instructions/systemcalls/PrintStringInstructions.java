package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class PrintStringInstructions extends Instruction {

   @Override
   public String toString() {
      return "push string" + "\n" + "call printf \n" + "add esp, 8 \n"
            + "push 0 \n" + "call fflush \n" + "add esp, 4 \n";
   }

}
