package utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private final String LABEL     = "\\s*(\\w+:\\s+)";
   private final String OPT_LABEL = LABEL+"?";   // optional label
   private final String WORD      = "(\\w+)";
   private final String OP_CODE   = WORD+"\\s+";
   private final String REG       = "([rR]\\d{1,2})";
   private final String COMMA     = "\\s*,\\s*";
   private final String COMMENT   = "(\\s*;.*)?";
   
   private final String OP = "\\s*\\(\\s*"; //open parenthesis
   private final String CP = "\\s*\\)\\s*"; //close parenthesis
   
   private final Pattern R_TYPE     = Pattern.compile(OPT_LABEL+OP_CODE+REG+COMMA+REG+COMMA+REG+COMMENT);
   private final Pattern LOAD_STORE = Pattern.compile(OPT_LABEL+OP_CODE+REG+COMMA+WORD+OP+REG+CP+COMMENT);
   private final Pattern BNE        = Pattern.compile(OPT_LABEL+OP_CODE+REG+COMMA+REG+COMMA+WORD+COMMENT);
   private final Pattern BC         = Pattern.compile(OPT_LABEL+OP_CODE+WORD+COMMENT);
   private final Pattern DATA       = Pattern.compile(LABEL+"\\."+WORD+"\\s+"+WORD);
   
   public void test(){
     printResults(BC, new String[]{
        "label: bc labelTo ;Comment",
        "label: bc labelTo;Comment",
        "bc labl;Comment",
        "bc invalid, invalid2",
        "bc invalid, invalid2;comment",
        "invalid"
     });      
      
//      printResults(bne, new String[]{
//         "label: bne   r1, r2, label2  ;comment",
//         "    bne r1,r2,destination;comment  ",
//         "bne r1, r2, destination ;comment",
//         "invalid"
//      });
//      printResults(data, new String[]{
//         "b: .byte 0x23",
//         "   s:    .word16   1337",
//         "word: .word32     0xABCDEF01",
//         "asdf64: .word64 1"
//      });
      
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
