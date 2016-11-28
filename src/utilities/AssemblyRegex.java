package utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private final String label    = "\\s*(\\w+:\\s+)";
   private final String optLabel = label+"?";   // optional label
   private final String word     = "(\\w+)";
   private final String opCode   = word+"\\s+";
   private final String reg      = "([rR]\\d{1,2})";
   private final String comma    = "\\s*,\\s*";
   private final String comment  = "(\\s*;.*)?";
   
   private final String op      = "\\s*\\(\\s*"; //open parenthesis
   private final String cp      = "\\s*\\)\\s*"; //close parenthesis
   
   private final Pattern rType      = Pattern.compile(optLabel+opCode+reg+comma+reg+comma+reg+comment);
   private final Pattern loadStore  = Pattern.compile(optLabel+opCode+reg+comma+word+op+reg+cp+comment);
   private final Pattern data       = Pattern.compile(label+"\\."+word+"\\s+"+word);
   
   public void test(){
      printResults(data, new String[]{
         "b: .byte 0x23",
         "   s:    .word16   1337",
         "word: .word32     0xABCDEF01",
         "asdf64: .word64 1"
      });
      
//      printResults(rType, new String[]{
//         "  label:    O2R    R4, R31,R5    ;Test",
//         "               slt r0, r0, r0;            newline",
//         "NOP",
//         "  label: extra or r1, r2, r3",     //Are captured. Should not be.
//         "or r1, r2, r3, extra"
//      });
      
//      printResults(loadStore, new String[]{
//         " label:       ld r1, 2000(r0)   ;Comment",
//         "sd   ,                 r3,2000(r2);",
//         " sd r3      , label           (r1)",
//         "ld,r1,0(r0);comment"
//      });
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
