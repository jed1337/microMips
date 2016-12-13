package micromips.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import models.Instruction;
import models.Modification;
import models.Storage;
import utilities.AssemblyRegex;
import utilities.Errors;
import utilities.INSTRUCTION_TYPE;
import utilities.INSTRUCTION_CONTENTS;
import utilities.UtilityFunctions;
import static utilities.INSTRUCTION_CONTENTS.*;

public class Scheduler {

   private final ArrayList<Instruction> instructions;
   private final ArrayList<Modification> modifications;
   private final ArrayList<Integer> opcodes;

   private int endPC = 0x1000;

   public Scheduler() {
      instructions = new ArrayList<>();
      modifications = new ArrayList<>();
      opcodes = new ArrayList<>();
   }

   public void setInput(String jText) {
      for (String input : jText.split("\n")) {
         AssemblyRegex ar = new AssemblyRegex(input);
         if (!Errors.getParsingErrors().isEmpty()) {
            System.err.println(input + " has an error!");
         } else {
            createInstruction(ar.getInstructionContents());
         }
      }
   }

   public void applyModifications() {
      hasDuplicateLabel();
      for (Modification modification : modifications) {
         int mIndex = modification.getInstructionIndex();
         String mLabel = modification.getLabel();

         boolean labelExists = false;
         for (int i = 0; i < instructions.size(); i++) {
            String iLabel = instructions.get(i).getLabel();

            if (mLabel.equals(iLabel)) {
               labelExists = true;

               int offset = i - (mIndex + 1);
               instructions.get(modification.getInstructionIndex())
                       .setArguementAt(modification.getArgumentIndex(), offset);

               break;
            }
         }
         if (!labelExists) {
            Errors.addParsingError(mLabel, "Is nonexistent");
         }
      }
      System.out.println("Test end");
   }

   public void setOpcodes() {
      for (Instruction instruction : instructions) {
         opcodes.add(
                 UtilityFunctions.getOpCode(
                         instruction.getInstructionType(), instruction.getArguments()
                 ));
      }
   }

   public void storeOpcodes() {
      int addr = 0x1000;
      for (Integer i : opcodes) {
         Storage.codeStoreWord(addr, i);

         addr += 0x4;
      }
      endPC = addr;
   }

   private void hasDuplicateLabel() {
      List<String> iLabels = instructions.stream()
              .map(ins->ins.getLabel())
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

      int iDitinctSize = iLabels.stream()
              .distinct()
              .collect(Collectors.toList())
              .size();

      if (iLabels.size() != iDitinctSize) {
         Errors.addParsingError(Arrays.toString(iLabels.toArray()), "Contains duplicate labels");
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
            modifications.add(new Modification(instructions.size() - 1, ics.get(IMM), 2));
            //Add modification
            break;
         case BC:
            instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
                                             "0"
                                     )));
            modifications.add(new Modification(instructions.size() - 1, ics.get(IMM), 0));
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

   public ArrayList<Instruction> getInstructions() {
      return instructions;
   }

   public ArrayList<Modification> getModifications() {
      return modifications;
   }

   public ArrayList<Integer> getOpcodes() {
      return opcodes;
   }

   public boolean checkAndForward(int curCycle, int[] prevCycles, CYCLE_NAME curCycleName) {
      String opcode = UtilityFunctions.to32BitBinString(curCycle).substring(0, 6);
      Availability curCycleAvail = Availability.getAvailability(curCycle);
      
      for (int i = 0; i < prevCycles.length; i++) {
         int prevCycle = prevCycles[i];
         ArrayList<Dependency> prevCycleDepends = Dependency.getDependencies(prevCycle);
         
         final int dependencyIndex = getDependencyIndex(curCycleAvail, prevCycleDepends);
         if (dependencyIndex != -1) {
            Dependency prevCycleDepend = prevCycleDepends.get(dependencyIndex);
            System.out.println("DEPENDENCY " + curCycleName.name());
            //Check if the dependency is available
            CYCLE_NAME curCycleAvailName = curCycleAvail.getCycleName();
            CYCLE_NAME prevCycleDependName = prevCycleDepend.getCycleName();

            //Item is available
            if (isNeeded(prevCycleDependName, curCycleName, i)) {
               if (isAvailable(curCycleName, curCycleAvailName)) {
                  //Forward
                  if (opcode.equals("110111")) {  //Load
                     if (curCycleName == CYCLE_NAME.MEM) {
                        System.out.println("FORWARD MEM.LMD (MEM)");
                        Execution.forward(dependencyIndex, MemoryAccess.MEM_WB_LMD);
                     } else if (curCycleName == CYCLE_NAME.ID) {
                        System.out.println("FORWARD MEM.LMD (ID)");
                        InstructionFetch.forward(dependencyIndex, MemoryAccess.MEM_WB_LMD);
                     }
                  } else { //Not load
                     if (curCycleName == CYCLE_NAME.MEM) {
                        if (opcode.equals("111111")) {
                           MemoryAccess.forward(MemoryAccess.MEM_WB_ALU_OUTPUT);
                           System.out.println("FORWARD MEM.ALU (MEM)");
                        } else {
                           Execution.forward(dependencyIndex, MemoryAccess.MEM_WB_ALU_OUTPUT);

                           System.out.println("FORWARD MEM.ALU (EX)");
                        }

                     } else if (curCycleName == CYCLE_NAME.EX) {
                        Execution.forward(dependencyIndex, Execution.EX_MEM_ALU_OUTPUT);
                        System.out.println("FORWARD EX.ALU (EX)");
                     } else if (curCycleName == CYCLE_NAME.ID) {
                        InstructionFetch.forward(dependencyIndex, Execution.EX_MEM_ALU_OUTPUT);
                        System.out.println("FORWARD EX.ALU (ID)");
                     }
                  }
               } else {
                  //Stall
                  System.out.println("STALL");
                  return true;
               }
            }
         }
      }
      return false;
   }

   private boolean isNeeded(CYCLE_NAME prevCycleDependName, CYCLE_NAME curCycleName, int i) {
      return prevCycleDependName.getNum() == (curCycleName.getNum() - i);
   }

   private boolean isAvailable(CYCLE_NAME cnThis, CYCLE_NAME cnThat) {
//      return cnThis.isGreaterThanOrEqual(cnThat);
      return cnThis.isEqual(cnThat);
   }

   private int getDependencyIndex(Availability curCycleAvail, ArrayList<Dependency> prevCycleDepends) {
      if (curCycleAvail != null) {
         int curRegnum = curCycleAvail.getRegNum();
         for (int i = 0; i < prevCycleDepends.size(); i++) {
            int prevRegnum = prevCycleDepends.get(i).getRegNum();
            if (curRegnum == prevRegnum) {
               return i;
            }
         }
      }
      return -1;
   }

   public void runOneCycle() {

      Writeback.writeback();
      
      if (!checkAndForward(MemoryAccess.MEM_WB_IR, new int[]{
         Execution.EX_MEM_IR,
         InstructionDecode.ID_EX_IR,
         InstructionFetch.IF_ID_IR,
         0x0
      }, CYCLE_NAME.MEM)) {

         MemoryAccess.loadValues();
         MemoryAccess.memoryAccess();
         
         if (!checkAndForward(Execution.EX_MEM_IR, new int[]{
            InstructionDecode.ID_EX_IR,
            InstructionFetch.IF_ID_IR,
            0x0,
            MemoryAccess.MEM_WB_IR
         }, CYCLE_NAME.EX)) {

            Execution.loadValues();
            Execution.execute();
            if (!checkAndForward(InstructionDecode.ID_EX_IR, new int[]{
               InstructionFetch.IF_ID_IR,
               0x0,
               MemoryAccess.MEM_WB_IR,
               Execution.EX_MEM_IR
            }, CYCLE_NAME.ID)) {

               InstructionDecode.loadValues();
               InstructionDecode.decode();

               InstructionFetch.loadValues();
               if (InstructionFetch.PC < endPC) {
                  InstructionFetch.fetch();
               }
            }

         }
      }

      InstructionFetch.printContents();
      InstructionDecode.printContents();
      Execution.printContents();
      MemoryAccess.printContents();
      Writeback.printContents();

      System.out.println();
   }
}
