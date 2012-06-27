package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class ScanStringInstructions extends Instruction {

   @Override
   public String toString() {
      return "push string \n" + "call scanf \n" + "add esp, 8 \n";
   }
}
