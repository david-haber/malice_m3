package intermediaryGeneration.instructions.systemcalls;

import intermediaryGeneration.instructions.Instruction;

public class KernelCallInstruction extends Instruction {

   public KernelCallInstruction() {
   }

   @Override
   public String toString() {
      return "mov eax, 1 \n" + "int 80H \n";
   }

}
