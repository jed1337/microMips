package micromips.processor;

import java.util.ArrayList;
import java.util.EnumMap;
import models.Instruction;
import models.Modification;
import utilities.AssemblyRegex;
import utilities.Errors;
import utilities.INSTRUCTION_TYPE;
import utilities.INSTRUCTION_CONTENTS;
import static utilities.INSTRUCTION_CONTENTS.*;

public class Scheduler {
   private final ArrayList<Instruction> instructions;
   private final ArrayList<Modification> modifications;

   public Scheduler(String jText) {
      instructions = new ArrayList<>();
      modifications = new ArrayList<>();

      for (String input : jText.split("\\n")) {
         AssemblyRegex ar = new AssemblyRegex(input);
         if (!Errors.getParsingErrors().isEmpty()) {
            //Stop excecution
         } else {
            createInstruction(ar.getInstructionContents());
         }
      }
   }

   private Instruction createInstruction(EnumMap<INSTRUCTION_CONTENTS, String> ics) {
      Instruction instruction = new Instruction(null, null, null);
      INSTRUCTION_TYPE inType = INSTRUCTION_TYPE.getInstructionType(ics.get(OPCODE));
      switch (inType) {
         case XOR:
         case DSUBU:
         case SLT:
            instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
               ics.get(RD),
               ics.get(RS),
               ics.get(RT)
            )));
            break;
         case NOP:
            instructions.add(new Instruction(inType, ics.get(LABEL), new int[]{}));
            break;
         case LD:
         case SD:
            instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
               ics.get(RT),
               ics.get(IMM),
               ics.get(RS)
            )));
            break;
         case DADDIU:
            instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
               ics.get(RT),
               ics.get(RS),
               ics.get(IMM)
            )));
            break;
         case BEQC:
            instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
               ics.get(RS),
               ics.get(RT),
               "0"
            )));
            modifications.add(new Modification(instructions.size()-1, ics.get(IMM), 3));
            //Add modification
            break;
         case BC:
            instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
               "0"
            )));
            modifications.add(new Modification(instructions.size()-1, ics.get(IMM), 1));
            break;
      }
      return instruction;
   }

   private int[] getInArgs(String... strings) {
      int[] args = new int[strings.length];
      for (int i = 0; i < strings.length; i++) {
         //A regsiter
         if (strings[i].toLowerCase().charAt(0) == 'r') {
            args[i] = Integer.parseInt(strings[i].substring(1));
            if (args[i] < 0 || args[i] > 31) {
               Errors.addParsingError("R" + strings[i] + " Is an invalid register", "Valid registers are from r0 to r31");
            }
         } //An immediate
         else {
            args[i] = Integer.parseInt(strings[i], 16);
         }
      }
      return args;
   }
}
