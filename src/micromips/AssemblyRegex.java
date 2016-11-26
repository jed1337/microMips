package micromips;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   public void test(){
//      String text = "    OR    R4, R31,R5    ";
      String text = "  allah:    OR    R4, R31,R5    ";
      
      String sc       = "\\s*,\\s*";   //Space comma
      String word     = "(\\w+?)";
      String register = "([rR$]\\d{1,2}?)";
      
//      Pattern pattern = Pattern.compile("\\s*""(\\w+?)""\\s+""(\\w+?)"sc"(\\w+?)"sc+"(\\w+)");
//      Pattern pattern = Pattern.compile("\\s*(\\w+:\\s+)?(\\w+)\\s+"+register+sc+register+sc+register);
      Pattern pattern = Pattern.compile("\\s*(\\w+:\\s+)?(\\w+)");
      Matcher matcher = pattern.matcher(text);
      
      if(matcher.find()){
         for(int i=0; i<=matcher.groupCount();i++){
            System.out.printf("Group %d = '%s'\n", i, matcher.group(i));
         }
      }else{
         System.out.println("Not found");
      }
   }
}
