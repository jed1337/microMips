package utilities;

import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyRegex {
   private static final Pattern P_OP     = Pattern.compile("(\\s*\\(\\s*)"); //open parenthesis
   private static final Pattern P_CP     = Pattern.compile("(\\s*\\)\\s*)"); //close parenthesis
   private static final Pattern P_COMMA  = Pattern.compile("(,)");
   private static final Pattern P_WORD   = Pattern.compile("(\\w+)");
   private static final Pattern P_IMM    = Pattern.compile("(\\d{1,4})");

   private static final Pattern P_LABEL  = Pattern.compile("\\s*(\\w+:\\s+)");
   private static final Pattern P_OPCODE = Pattern.compile("(\\w+)");
   private static final Pattern P_REG    = Pattern.compile("([rR]\\d{1,2})");
   
   private String input;
   private String origInput;
   private Matcher matcher;
   private final EnumMap<INSTRUCTION_CONTENTS, String> instructionContents 
      = new EnumMap<>(INSTRUCTION_CONTENTS.class);
   
   public AssemblyRegex(String input){
      this.input = input.replaceAll("\t", "").trim();
      this.origInput = this.input;

      setAndRemoveComments();
      setAndRemove(P_LABEL, INSTRUCTION_CONTENTS.LABEL, false);
      setAndRemove(P_OPCODE, INSTRUCTION_CONTENTS.OPCODE);
      
      INSTRUCTION_TYPE instruction = INSTRUCTION_TYPE.getInstructionType(instructionContents.get(INSTRUCTION_CONTENTS.OPCODE));
      if (instruction == null) {
         System.err.println("INVALID");
         Errors.addParsingError(origInput, "The opcode is invalid");
      }
      else{
         System.out.println("Instruction is: "+instruction);
         switch(instruction){
            case XOR:
            case DSUBU:
            case SLT:
               setAndRemove(P_REG , INSTRUCTION_CONTENTS.RD);
               remove(P_COMMA);
               setAndRemove(P_REG , INSTRUCTION_CONTENTS.RS);
               remove(P_COMMA);
               setAndRemove(P_REG , INSTRUCTION_CONTENTS.RT);
               break;
            case NOP:
               break;
            case LD:
            case SD:
               setAndRemove(P_REG  , INSTRUCTION_CONTENTS.RT);
               remove(P_COMMA);
               setAndRemove(P_IMM , INSTRUCTION_CONTENTS.IMM);
               remove(P_OP);
               setAndRemove(P_REG  , INSTRUCTION_CONTENTS.RS);
               remove(P_CP);
               break;
            case BEQC:
               setAndRemove(P_REG , INSTRUCTION_CONTENTS.RS);
               remove(P_COMMA);
               setAndRemove(P_REG , INSTRUCTION_CONTENTS.RT);
               remove(P_COMMA);
               setAndRemove(P_WORD , INSTRUCTION_CONTENTS.IMM);
               break;
            case DADDIU:
               setAndRemove(P_REG , INSTRUCTION_CONTENTS.RT);
               remove(P_COMMA);
               setAndRemove(P_REG , INSTRUCTION_CONTENTS.RS);
               remove(P_COMMA);
               setAndRemove(P_IMM , INSTRUCTION_CONTENTS.IMM);
               break;
            case BC:
               setAndRemove(P_WORD, INSTRUCTION_CONTENTS.IMM);
               break;
         }
      }
      if(!this.input.isEmpty()){
         Errors.addParsingError(origInput, "Contains extraeneous input");
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
            Errors.addParsingError(origInput, "Extraeneous input: "+this.input.substring(0, matcher.start(1)));
         }
         if(content != null){ //From the remove instruction. Most likely violates OOP
            instructionContents.put(content, matcher.group(1).trim());
         }
         this.input = this.input.substring(matcher.end(1));
      }else if(required){
         Errors.addParsingError(origInput, "Missing a paraneter");
      }
      this.input = input.trim();
   }

   public EnumMap<INSTRUCTION_CONTENTS, String> getInstructionContents() {
      return instructionContents;
   }
}
