package utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private final String label   = "(\\w+:\\s+)?";
   private final String word    = "(\\w+?)";
   private final String opCode  = word+"\\s+";
   private final String reg     = "([rR]\\d{1,2})";
   private final String comma   = "\\s*,\\s*";
   private final String comment = "(\\s*;.*)?";
   
   private final String op      = "\\s*\\(\\s*"; //open parenthesis
   private final String cp      = "\\s*\\)\\s*"; //close parenthesis
   
   private final Pattern rType      = Pattern.compile("\\s*"+label+opCode+reg+comma+reg+comma+reg+comment);
   private final Pattern loadStore  = Pattern.compile("\\s*"+label+opCode+reg+comma+word+op+reg+cp+comment);
   
   public void test(){
//      printResults(rType, new String[]{
//         "  label:    O2R    R4, R31,R5    ;Test",
//         "               slt r0, r0, r0;            newline",
//         "NOP"
//      });
      
      printResults(loadStore, new String[]{
         " label:       ld r1, 2000(r0)   ;Comment",
         "sd   ,                 r3,2000(r2);",
         " sd r3      , label           (r1)",
         "ld,r1,0(r0);comment"
      });
   }
   
   private void printResults(Pattern pattern, String[] inputs){
      System.out.println("\n---"+pattern.toString()+"---");
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
