package intermediaryGeneration;

public class Register {

   private String dwordName;
   private String wordName;
   private String byteName;

   public Register(String name) {
      super();
      this.dwordName = name;
   }

   public Register(String dwordName, String wordName) {
      super();
      this.dwordName = dwordName;
      this.wordName = wordName;
   }

   public Register(String dwordName, String wordName, String byteName) {
      super();
      this.dwordName = dwordName;
      this.wordName = wordName;
      this.byteName = byteName;
   }

   public String getName() {
      return dwordName;
   }

   public String getWordName() {
      return wordName;
   }

   public void setWordName(String wordName) {
      this.wordName = wordName;
   }

   public String getByteName() {
      return byteName;
   }

   public void setByteName(String byteName) {
      this.byteName = byteName;
   }

   public void setName(String name) {
      this.dwordName = name;
   }

}