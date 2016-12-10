package utilities;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private static final String LABEL     = "\\s*(\\w+:\\s+)";
   private static final String WORD      = "(\\w+)";
   private static final String OPCODE    = WORD+"\\s+";
   private static final String REG       = "([rR]\\d{1,2})";
   private static final String COMMA     = "\\s*,\\s*";
   private static final String COMMENT   = "(\\s*;.*)?";
   
   private static final String OP = "\\s*\\(\\s*"; //open parenthesis
   private static final String CP = "\\s*\\)\\s*"; //close parenthesis
   
   public static final Pattern R_TYPE     = Pattern.compile(LABEL+OPCODE+REG+COMMA+REG+COMMA+REG+COMMENT);
   public static final Pattern LOAD_STORE = Pattern.compile(LABEL+OPCODE+REG+COMMA+WORD+OP+REG+CP+COMMENT);
   public static final Pattern BNE        = Pattern.compile(LABEL+OPCODE+REG+COMMA+REG+COMMA+WORD+COMMENT);
   public static final Pattern BC         = Pattern.compile(LABEL+OPCODE+WORD+COMMENT);
   
   public static final Pattern P_LABEL = Pattern.compile(LABEL);
   public static final Pattern P_OPCODE = Pattern.compile(OPCODE);
   public static final Pattern P_REG = Pattern.compile(REG);
   
   private String input;
   private String origInput;
   private Matcher matcher;
   private final HashMap<INSTRUCTION_CONTENTS, String> iContents = new HashMap<>();
   
   
   public AssemblyRegex(String input){
      this.input = input;
      this.origInput = input;
      test();
   }

   private void test() {
      setAndRemoveComments();
      setAndRemove(P_LABEL, INSTRUCTION_CONTENTS.LABEL);
      setAndRemove(P_OPCODE, INSTRUCTION_CONTENTS.OPCODE);
      
      INSTRUCTION instruction = INSTRUCTION.getInstruction(iContents.get(INSTRUCTION_CONTENTS.OPCODE));
      if (instruction == null) {
         System.out.println("INVALID");
         //add mips exception here
      }
      else{
         switch(instruction){
            case XOR:
            case DSUBU:
            case SLT:
               System.out.println("R TYPE");
               setAndRemove(P_REG, INSTRUCTION_CONTENTS.RD);
               setAndRemove(P_REG, INSTRUCTION_CONTENTS.RS);
               setAndRemove(P_REG, INSTRUCTION_CONTENTS.RT);
               break;
            case NOP:
               System.out.println("NOP");
               break;
            case LD:
            case SD:
               
               System.out.println("MEM");
               break;
            case BEQC:
            case DADDIU:
               System.out.println("IMM");
               break;
            case BC:
               System.out.println("JTYPE");
               break;
         }
         //Add mips exception if input still has contents
      }
      
      System.out.println(input);
   }
   
   public void setAndRemoveComments(){
      int sIndex = this.input.indexOf(';');
      iContents.put(INSTRUCTION_CONTENTS.COMMENT, this.input.substring(sIndex));
      if(sIndex != -1){
         this.input = this.input.substring(0, sIndex).trim();
      }
   }
   
   private void setAndRemove(Pattern pattern, INSTRUCTION_CONTENTS content) {
      matcher = pattern.matcher(this.input);
      if(matcher.find()){
         iContents.put(content, matcher.group(1).trim());
         this.input = this.input.substring(matcher.end(1));
      }
      this.input = input.trim();
   }

}
