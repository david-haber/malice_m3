package intermediaryGeneration;

public class LabelGenerator {

   static private final String DEFAULTLABEL = "label";
   static private int labelNumber = 0;

   static public String getLabel() {
      String returnString = DEFAULTLABEL + "_" + labelNumber;
      labelNumber++;
      return returnString;
   }

   static public String getLabel(String label) {
      int n = labelNumber;
      labelNumber++;
      return label + "_" + n;
   }

}
