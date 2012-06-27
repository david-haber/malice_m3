package intermediaryGeneration.operands;

import intermediaryGeneration.Register;

public class RegisterNegativeOffsetOperand implements Operand {

   Register register;
   int offset;

   public RegisterNegativeOffsetOperand(Register register, int offset) {
      super();
      this.register = register;
      this.offset = offset;
   }

   public Register getRegister() {
      return register;
   }

   @Override
   public String toString() {
      return register.getName() + "-" + offset;
   }
}
