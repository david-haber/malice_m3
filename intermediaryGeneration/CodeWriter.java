package intermediaryGeneration;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 This class writes given strings to a file
 */

public class CodeWriter {

   private static File outFile;
   private static DataOutputStream dos;

   // Constructors
   public CodeWriter() throws FileNotFoundException {
      outFile = new File("output.asm");
      dos = new DataOutputStream(new FileOutputStream(outFile));
   }

   public CodeWriter(String fileName) throws FileNotFoundException {
      outFile = new File(fileName);
      dos = new DataOutputStream(new FileOutputStream(outFile));
   }

   public void writeToFile(String s) {
      try {
         dos.writeBytes(s);
      } catch (IOException e) {
         System.out.println("Error: Writing to file.");
      }
   }

   public void writeLineToFile(String line) {
      try {
         dos.writeBytes(line + "\n");
      } catch (IOException e) {
         System.out.println("Error: Writing to file.");
      }
   }

   public void closeWriter() throws IOException {
      dos.close();
   }

}
