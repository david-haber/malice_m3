package intermediaryGeneration.instructions.globalDeclarations;

import intermediaryGeneration.instructions.Instruction;

public class DeclareStringInstruction extends Instruction {
   private String label;
   private String string;
   private boolean newline;

   public DeclareStringInstruction(String label, String string) {
      super();
      this.label = label;
      this.string = string;
      newline = false;
   }

   public DeclareStringInstruction(String label, String string, boolean newline) {
      super();
      this.label = label;
      this.string = string;
      this.newline = newline;
   }

   public String getLabel() {
      return label;
   }

   public String getString() {
      return string;
   }

   @Override
   public String toString() {
      if (newline) {
         return label + ": " + "db " + '"' + string + '"' + ", 10 , 0";
      } else {
         return label + ": " + "db " + '"' + string + '"' + ", 0";
      }
   }

}
