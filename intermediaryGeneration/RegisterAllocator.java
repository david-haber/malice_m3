package intermediaryGeneration;

import intermediaryGeneration.instructions.basic.PopInstruction;
import intermediaryGeneration.instructions.logical.PushInstruction;

import java.util.Iterator;
import java.util.LinkedList;

public class RegisterAllocator {

   private LinkedList<Register> registers;
   private LinkedList<Register> registersInUse;

   public RegisterAllocator() {

      registers = new LinkedList<Register>();
      registersInUse = new LinkedList<Register>();

      registers.push(new Register("esi"));
      registers.push(new Register("edi"));
      registers.push(new Register("edx", "dx", "dl"));
      registers.push(new Register("ecx", "cx", "cl"));
      registers.push(new Register("ebx", "bx", "bl"));
      // registers.push(new Register("eax","ax","al"));

   }

   public Register getNextRegister() {
      Register toReturn = registers.pop();
      registers.remove(toReturn);
      registersInUse.add(toReturn);
      return toReturn;

   }

   public void returnRegister(Register register) {
      registersInUse.remove(register);
      registers.push(register);
   }

   public void pushRegistersInUse(Function currentFunction) {
      Iterator<Register> iterator = registersInUse.iterator();

      while (iterator.hasNext()) {
         currentFunction.addInstructionToBack(new PushInstruction(iterator
               .next().getName()));
      }
   }

   public void popRegistersInUse(Function currentFunction) {
      Iterator<Register> iterator = registersInUse.descendingIterator();

      while (iterator.hasNext()) {
         currentFunction.addInstructionToBack(new PopInstruction(iterator
               .next().getName()));
      }
   }

   public Register getSpecificRegister(String registerName) {
      Register returnRegister = lookUpRegister(registerName);
      if (returnRegister != null) {
         registers.remove(returnRegister);
         registersInUse.add(returnRegister);
      }
      return returnRegister;
   }

   private Register lookUpRegister(String name) {
      for (int i = 0; i < registers.size(); i++) {
         if (registers.get(i).getName().equals(name)) {
            return registers.get(i);
         }
      }
      return null;
   }

}