package intermediaryGeneration.operands;

public class ImmediateOperand implements Operand {

   int integer;

   public ImmediateOperand(int integer) {
      super();
      this.integer = integer;
   }

   public int getInteger() {
      return integer;
   }

   public void setInteger(int integer) {
      this.integer = integer;
   }

   @Override
   public String toString() {
      return "" + integer;
   }
}
