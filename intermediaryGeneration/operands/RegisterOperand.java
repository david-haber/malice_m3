package intermediaryGeneration.operands;

import intermediaryGeneration.Register;

public class RegisterOperand implements Operand {

   Register register;

   public RegisterOperand(Register register) {
      super();
      this.register = register;
   }

   public Register getRegister() {
      return register;
   }

   @Override
   public String toString() {
      return register.getName();
   }
}
