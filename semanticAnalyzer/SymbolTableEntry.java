package semanticAnalyzer;

import ast.Type;

public class SymbolTableEntry {

   private Type type;
   private boolean isInitialized;
   private boolean isArray;

   public SymbolTableEntry(Type type) {
      super();
      this.type = type;
      this.isInitialized = false;
   }

   public SymbolTableEntry(Type type, Boolean isArray) {
      super();
      this.type = type;
      this.isInitialized = false;
      this.isArray = isArray;
   }

   public Type getType() {
      return type;
   }

   public void setType(Type type) {
      this.type = type;
   }

   public boolean isInitialized() {
      return isInitialized;
   }

   public boolean isArray() {
      return this.isArray;
   }

   public void initialize(boolean isInitialized) {
      this.isInitialized = isInitialized;
   }

}
