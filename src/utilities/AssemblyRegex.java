package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private static final String LABEL     = "\\s*(\\w+:\\s+)";
   private static final String WORD      = "(\\w+)";
   private static final String DATA_WORD = "(\\.\\w+)";
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
   
   private Matcher matcher;

   private HashMap<INSTRUCTION_CONTENTS, String> iContents;
   
//   private String comments;
//   private String label;
//   private String opcode;
   
   public void test(){
      iContents = new HashMap<>();
      String input = "label:  opcode   r1, r2, r3  ;Test";
      String origInput = input;
      
      input = setAndRemoveComments(input);
      input = setAndRemoveLabel(input);
      input = setAndRemoveOpcode(input);
      
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
               System.out.println("RTYPE");
            case NOP:
               System.out.println("NOP");
            case LD:
            case SD:
               System.out.println("MEM");
            case BEQC:
            case DADDIU:
               System.out.println("IMM");
            case BC:
               System.out.println("JTYPE");
         }
      }
      
      System.out.println(input);
   }
   
   public String setAndRemoveComments(String input){
      int sIndex = input.indexOf(';');
      iContents.put(INSTRUCTION_CONTENTS.COMMENT, input.substring(sIndex));
      return sIndex==-1? input : input.substring(0, sIndex).trim();
   }
   
   public String setAndRemoveLabel(String input){
      return setAndRemove(input, P_LABEL, INSTRUCTION_CONTENTS.LABEL);
   }
   
   public String setAndRemoveOpcode(String input){
      return setAndRemove(input, P_OPCODE, INSTRUCTION_CONTENTS.OPCODE);
   }
   
   private String setAndRemove(String input, Pattern pattern, INSTRUCTION_CONTENTS content) {
      matcher = pattern.matcher(input);
      if(matcher.find()){
         iContents.put(content, matcher.group(1).trim());
         input = input.substring(matcher.end(1));
      }
      return input.trim();
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
