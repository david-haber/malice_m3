package semanticAnalyzer;

import java.util.Hashtable;

import ast.*;

public class SymbolTable {

   private Hashtable<String, SymbolTableEntry> table;
   private SymbolTable parent;

   public SymbolTable() {
      this.table = new Hashtable<String, SymbolTableEntry>();
      this.parent = null;
   }

   public SymbolTable(SymbolTable old) {

      this.table = new Hashtable<String, SymbolTableEntry>();
      this.parent = old;
   }

   public void addParent(SymbolTable parent) {
      this.parent = parent;
   }

   public Hashtable<String, SymbolTableEntry> getTable() {
      return this.table;
   }

   public void reset() {
      this.table.clear();
   }

   public void loseParent() {
      this.parent = null;
   }

   public Type getType(String identifier) throws SemanticException {
      SymbolTableEntry symTab = isDeclared(identifier);
      return symTab.getType();
   }

   public SymbolTableEntry isDeclared(String identifier)
         throws IdentifierNotDeclaredException {
      if (!table.containsKey(identifier)) {
         if (parent == null) {
            throw new IdentifierNotDeclaredException(identifier);
         } else {
            return parent.isDeclared(identifier);
         }
      }
      return table.get(identifier);
   }

   public SymbolTable getParent() {
      return parent;
   }

   public boolean isDeclaredArray(String identifier, Type type)
         throws IdentifierNotDeclaredException {
      SymbolTableEntry entry = isDeclared(identifier);
      return entry.isArray();
   }

   public void addBasicIdentifier(String identifier, Type type)
         throws MultipleDeclarationException {
      if (table.containsKey(identifier)) {
         throw new MultipleDeclarationException(identifier);
      }
      table.put(identifier, new SymbolTableEntry(type));
   }

   public void addArrayIdentifier(String identifier, Type type)
         throws MultipleDeclarationException {
      if (table.containsKey(identifier)) {
         throw new MultipleDeclarationException(identifier);
      }
      table.put(identifier, new SymbolTableEntry(type, true));
   }

   public void initializeIdentifier(String identifier)
         throws IdentifierNotDeclaredException {
      SymbolTableEntry symEntry = isDeclared(identifier);
      symEntry.initialize(true);
   }

   public void clear() {
      table.clear();
   }

   public void isInitialised(String identName) throws SemanticException {
      SymbolTableEntry symEntry = isDeclared(identName);
      if (!symEntry.isInitialized()) {
         throw new IdentifierNotInitializedException(identName);
      }
   }

}
