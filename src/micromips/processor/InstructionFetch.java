package micromips.processor;

import models.Storage;
import utilities.UtilityFunctions;

public class InstructionFetch extends InstructionSuperClass{
//   public static boolean hasOldValue = false;

   public int IF_ID_IR = 0;
   public int IF_ID_NPC = 0;
   public int PC = 0x1000;

   public boolean hasJumped   = false;
   
   private long calA = 0x0;
   private long calB = 0x0;

   private boolean hasChangedA = false;
   private boolean hasChangedB = false;

   private InstructionFetch() {}
   
   public InstructionFetch getInstance(){
      return (InstructionFetch) super.getInstance(new InstructionFetch(), CYCLE_NAME.IF);
   }
   
   public void loadValues() {
      String binaryIR = UtilityFunctions.to32BitBinString(getInstance().IF_ID_IR);
      if (hasChangedA) {
         calA = Storage.getRegisterValue(Integer.parseInt(binaryIR.substring(6, 11), 2));
      }
      if (hasChangedB) {
         calB = Storage.getRegisterValue(Integer.parseInt(binaryIR.substring(11, 16), 2));      }
      super.hasOldValue = false;
   }

   public void forward(int loc, long val) {
      if (loc == 0) {
         calA = val;
         hasChangedA = true;
      } else {
         calB = val;
         hasChangedB = true;
      }
   }

   public static void fetch() {
      int oldPC = InstructionFetch.PC;

      String binaryIR = UtilityFunctions.to32BitBinString(InstructionFetch.IF_ID_IR);
      final String opCode = binaryIR.substring(0, 6);
      
      hasJumped = false;
      switch (opCode) {
         case "110010": //BC
            InstructionFetch.IF_ID_NPC = InstructionFetch.PC += (Long.parseLong(binaryIR.substring(6, 32), 2) * 4);
            hasJumped = true;
            break;
         case "001000": //BEQC
            //Perform look ahead
            if (calA == calB) {
               InstructionFetch.IF_ID_NPC = InstructionFetch.PC += (Long.parseLong(binaryIR.substring(16, 32), 2) * 4);
               hasJumped = true;
            } else {
               InstructionFetch.IF_ID_NPC = InstructionFetch.PC += 0x4;
            }
            break;
         default:
            InstructionFetch.IF_ID_NPC = InstructionFetch.PC += 0x4;
            break;
      }

      InstructionFetch.IF_ID_IR = Storage.codeLoadWord(oldPC);
      hasChangedA = hasChangedB = false;
   }

   public static void printContents() {
      System.out.println("--INSTRUCTION FETCH--");
      System.out.println("IF/ID.IR: " + UtilityFunctions.to32BitHexString(InstructionFetch.IF_ID_IR));
      System.out.println("IF/ID.NPC: " + UtilityFunctions.to64BitHexString(InstructionFetch.IF_ID_NPC));
      System.out.println("PC: " + UtilityFunctions.to64BitHexString(InstructionFetch.PC));
   }
}
