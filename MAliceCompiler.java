import java.io.FileInputStream;

import ast.*;
import semanticAnalyzer.SemanticAnalyser;
import intermediaryGeneration.*;
import parser.*;
/*
 * AUTHORS: osw09, dh909, mtf09
 *
 * MAliceCompiler.java
 * This is the main class of the MAlice compiler
 * 
 * Arguments:
 *      [-filename]
 */

public class MAliceCompiler {
   public final static String DEFAULT_FILE_NAME = "output.asm";

   public static void main(String[] args) {

      try {

         FileInputStream inputStream = new FileInputStream(args[0]);
         // Trigger parser, returns ProgramNode - root of AST
         MAlice parser = new MAlice(inputStream);
         ProgramNode root = parser.parseProgram(); // = AST

         // Semantic Analysis
         SemanticAnalyser semanticAnalyser = new SemanticAnalyser();
         root.acceptVisitor(semanticAnalyser);

         // Create new visitor and generate Assembly Instructions
         CGVisitor visitor = new CGVisitor();
         root.acceptVisitor(visitor);
         FunctionTable functionTable = visitor.getFunctionTable();

         functionTable.writeToFile("output.asm");

      } catch (Exception e) {
         e.printStackTrace();
         System.exit(255);
      }
   }
}
