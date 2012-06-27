package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class ScanCharInstructions extends Instruction {

   @Override
   public String toString() {
      return "push char_scanf \n" + "call scanf \n" + "add esp, 8 \n";
   }

}
