package intermediaryGeneration;

import java.util.Hashtable;

import ast.Type;

public class LocalVariables implements Cloneable {

   private Hashtable<String, LocalVariable> localVariables;

   public LocalVariables() {
      localVariables = new Hashtable<String, LocalVariable>();
   }

   public void addVariable(String varName, String location, Type type,
         boolean isArray) {

      localVariables.put(varName, new LocalVariable(varName, type, location,
            isArray));
   }

   public String getVariableLocation(String varName) {
      return localVariables.get(varName).getLocation();
   }

   public Type getVariableType(String varName) {
      return localVariables.get(varName).getType();
   }

   public boolean isArray(String varName) {
      return localVariables.get(varName).isArray();
   }

   public Object clone() {
      try {
         LocalVariables cloned = (LocalVariables) super.clone();
         cloned.localVariables = 
            (Hashtable<String, LocalVariable>) localVariables.clone();
         return cloned;
      } catch (CloneNotSupportedException e) {
         System.out.println(e);
         return null;
      }
   }
}
