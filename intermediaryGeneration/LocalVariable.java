package intermediaryGeneration;

import ast.Type;

public class LocalVariable {
   private String name;
   private String location;
   private Type type;
   private boolean isArray;

   public LocalVariable(String name, Type type, String location, boolean isArray) {
      super();
      this.name = name;
      this.type = type;
      this.location = location;
      this.isArray = isArray;
   }

   public String getName() {
      return name;
   }

   public Type getType() {
      return type;
   }

   public String getLocation() {
      return location;
   }

   public boolean isArray() {
      return isArray;
   }

}
