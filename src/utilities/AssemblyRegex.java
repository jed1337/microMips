package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private final String LABEL     = "\\s*(\\w+:\\s+)";
   private final String OPT_LABEL = LABEL+"?";   // optional label
   private final String WORD      = "(\\w+)";
   private final String DATA_WORD = "(\\.\\w+)";
   private final String OP_CODE   = WORD+"\\s+";
   private final String REG       = "([rR]\\d{1,2})";
   private final String COMMA     = "\\s*,\\s*";
   private final String COMMENT   = "(\\s*;.*)?";
   
   private final String OP = "\\s*\\(\\s*"; //open parenthesis
   private final String CP = "\\s*\\)\\s*"; //close parenthesis
   
   public final Pattern R_TYPE     = Pattern.compile(OPT_LABEL+OP_CODE+REG+COMMA+REG+COMMA+REG+COMMENT);
   public final Pattern LOAD_STORE = Pattern.compile(OPT_LABEL+OP_CODE+REG+COMMA+WORD+OP+REG+CP+COMMENT);
   public final Pattern BNE        = Pattern.compile(OPT_LABEL+OP_CODE+REG+COMMA+REG+COMMA+WORD+COMMENT);
   public final Pattern BC         = Pattern.compile(OPT_LABEL+OP_CODE+WORD+COMMENT);
   public final Pattern DATA       = Pattern.compile(LABEL+DATA_WORD+"\\s+"+WORD);
   
   private Matcher matcher;
   
   public void test(){
   }
   
   public List<String> getMatchedGroups(Pattern pattern, String input){
      matcher = pattern.matcher(input);
      if(matcher.find()){
         int gc = matcher.groupCount();
         List<String> sGroups = new ArrayList<>();
         for (int i = 1; i <= gc; i++) {
            String mgi =matcher.group(i);
            sGroups.add(mgi==null? null:mgi.trim());
         }
         return sGroups;
      }
      return null;
   }
   
   private void printResults(Pattern pattern, String[] inputs){
      System.out.println("\n---"+pattern.toString()+"---");
      for (String input : inputs) {
         matcher = pattern.matcher(input);
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
