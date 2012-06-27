package intermediaryGeneration.instructions.basic;

import intermediaryGeneration.instructions.Instruction;
import ast.Type;

public class DeclareInstruction extends Instruction {

   String varName;
   Type type;

   public DeclareInstruction(String varName, Type type) {
      this.varName = varName;
      this.type = type;
   }

   @Override
   public String toString() {
      if (type.equals(Type.NUMBER)) {
         return varName + " resd 1";
      } else if (type.equals(Type.LETTER)) {
         return varName + " resb 1";
      }
      return null;
   }

}
