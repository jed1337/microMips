package utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {

   private final String c       = "\\s*,\\s*";   //comma
   private final String word    = "(\\w+?)";                 
   private final String reg     = "([rR$]\\d{1,2}?)";             
   private final String comment = "(\\s*;.*)?";              
   
   public void test(){
      String[] inputs = {
         "  label:    OR    R4, R31,R5    ;Test",
         " DADDU     $1, R2, r3 ",
         "               slt r0, r0, r0;            newline",
         "NOP"
      };

      Pattern pattern = Pattern.compile("\\s*(\\w+:\\s+)?(\\w+)\\s+"+reg+c+reg+c+reg+comment);
//      Pattern pattern = Pattern.compile("\\s*(\\w+:\\s+)?(\\w+)");
      for (String input : inputs) {
         Matcher matcher = pattern.matcher(input);
         if(matcher.find()){
            for(int j=0; j<=matcher.groupCount();j++){
               System.out.printf("Group %d = '%s'\n", j, matcher.group(j));
            }
         }else{
            System.out.println("Not found");
         }
         System.out.println("");
      }
   }
}
