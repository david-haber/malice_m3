package intermediaryGeneration.operands;

public class CharOperand implements Operand {

   char character;

   public CharOperand(char character) {
      this.character = character;
   }

   public char getCharacter() {
      return character;
   }

   public void setCharacter(char character) {
      this.character = character;
   }

   @Override
   public String toString() {
      return "" + character;
   }
}
