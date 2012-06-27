package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class ScanIntInstructions extends Instruction {

   @Override
   public String toString() {
      return "push integer_scanf \n" + "call scanf \n" + "add esp, 8 \n";

   }

}
