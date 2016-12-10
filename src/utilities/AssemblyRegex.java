package utilities;

import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private static final Pattern OP     = Pattern.compile("(\\s*\\(\\s*)"); //open parenthesis
   private static final Pattern CP     = Pattern.compile("(\\s*\\)\\s*)"); //close parenthesis
   private static final Pattern COMMA  = Pattern.compile("(,)");
   private static final Pattern WORD   = Pattern.compile("(\\w+)");

   private static final Pattern LABEL  = Pattern.compile("\\s*(\\w+:\\s+)");
   private static final Pattern OPCODE = Pattern.compile("(\\w+)");
   private static final Pattern REG    = Pattern.compile("([rR]\\d{1,2})");
   
   private String input;
   private String origInput;
   private Matcher matcher;
   private final EnumMap<INSTRUCTION_CONTENTS, String> instructionContents 
      = new EnumMap<>(INSTRUCTION_CONTENTS.class);
   
   public AssemblyRegex(String input){
      this.input = input;
      this.origInput = input;
      test();
   }

   private void test() {
      setAndRemoveComments();
      setAndRemove(LABEL, INSTRUCTION_CONTENTS.LABEL, false);
      setAndRemove(OPCODE, INSTRUCTION_CONTENTS.OPCODE);
      
      INSTRUCTION instruction = INSTRUCTION.getInstruction(instructionContents.get(INSTRUCTION_CONTENTS.OPCODE));
      if (instruction == null) {
         System.err.println("INVALID");
         //add mips exception here
      }
      else{
         System.out.println("Instruction is: "+instruction);
         switch(instruction){
            case XOR:
            case DSUBU:
            case SLT:
               setAndRemove(REG , INSTRUCTION_CONTENTS.RD);
               remove(COMMA);
               setAndRemove(REG , INSTRUCTION_CONTENTS.RS);
               remove(COMMA);
               setAndRemove(REG , INSTRUCTION_CONTENTS.RT);
               break;
            case NOP:
               break;
            case LD:
            case SD:
               setAndRemove(REG  , INSTRUCTION_CONTENTS.RT);
               remove(COMMA);
               setAndRemove(WORD , INSTRUCTION_CONTENTS.IMM);
               remove(OP);
               setAndRemove(REG  , INSTRUCTION_CONTENTS.RS);
               remove(CP);
               break;
            case BEQC:
               setAndRemove(REG , INSTRUCTION_CONTENTS.RS);
               remove(COMMA);
               setAndRemove(REG , INSTRUCTION_CONTENTS.RT);
               remove(COMMA);
               setAndRemove(WORD , INSTRUCTION_CONTENTS.IMM);
               break;
            case DADDIU:
               setAndRemove(REG , INSTRUCTION_CONTENTS.RT);
               remove(COMMA);
               setAndRemove(REG , INSTRUCTION_CONTENTS.RS);
               remove(COMMA);
               setAndRemove(WORD , INSTRUCTION_CONTENTS.IMM);
               break;
            case BC:
               setAndRemove(WORD, INSTRUCTION_CONTENTS.IMM);
               break;
         }
         //Add mips exception if input still has contents
      }
      if(!input.equals("")){
         System.err.println("Extraeneous input: "+input);
      }
   }
   
   public void setAndRemoveComments(){
      int sIndex = this.input.indexOf(';');
      if(sIndex != -1){
         instructionContents.put(INSTRUCTION_CONTENTS.COMMENT, this.input.substring(sIndex).trim());
         this.input = this.input.substring(0, sIndex).trim();
      }
   }
   
   private void remove(Pattern pattern){
      setAndRemove(pattern, null);
   }
   
   private void setAndRemove(Pattern pattern, INSTRUCTION_CONTENTS content) {
      setAndRemove(pattern, content, true);
   }

   private void setAndRemove(Pattern pattern, INSTRUCTION_CONTENTS content, boolean required) {
      matcher = pattern.matcher(this.input);
      if(matcher.find()){
         if(matcher.start(1)!=0){
            //add mips error for has extraeneous input somewhere
            System.err.println("Extraeneous input: "
               +this.input.substring(0, matcher.start(1)));
         }
         if(content != null){ //From the remove instruction. Most likely violates OOP
            instructionContents.put(content, matcher.group(1).trim());
         }
         this.input = this.input.substring(matcher.end(1));
      }else if(required){
         //add mips error for missing item
         System.err.println("You missed "+pattern);
      }
      this.input = input.trim();
   }

   public EnumMap<INSTRUCTION_CONTENTS, String> getInstructionContents() {
      return instructionContents;
   }
}
