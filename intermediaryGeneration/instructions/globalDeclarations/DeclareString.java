package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class DeclareString extends Instruction {
   private String label;
   private String string;

   public DeclareString(String label, String string) {
      super();
      this.label = label;
      this.string = string;
   }

   @Override
   public String toString() {
      return label + ": " + "db " + string + ", 10, 0";
   }

}
