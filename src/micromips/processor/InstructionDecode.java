package micromips.processor;

import models.Storage;
import utilities.UtilityFunctions;

public class InstructionDecode {

   public static long ID_EX_A = 0;
   public static long ID_EX_B = 0;
   public static long ID_EX_IMM = 0;
   public static int ID_EX_IR = 0;
   public static int ID_EX_NPC = 0;

   public static void loadValues() {
      if (!InstructionFetch.hasJumped) {
         InstructionDecode.ID_EX_IR = InstructionFetch.IF_ID_IR;
         InstructionDecode.ID_EX_NPC = InstructionFetch.IF_ID_NPC;
      }
   }

   public static void decode() {
      String binaryIR = UtilityFunctions.to32BitBinString(InstructionDecode.ID_EX_IR);
      InstructionDecode.ID_EX_A = Storage.getRegisterValue(Integer.parseInt(binaryIR.substring(6, 11), 2));
      InstructionDecode.ID_EX_B = Storage.getRegisterValue(Integer.parseInt(binaryIR.substring(11, 16), 2));
      InstructionDecode.ID_EX_IMM = UtilityFunctions.toSignedExtendedImmediate(Integer.parseInt(binaryIR.substring(16, 32), 2));
   }

   public static void printContents() {
      System.out.println("--INSTRUCTION DECODE--");
      System.out.println("ID/EX.IR: " + UtilityFunctions.to32BitHexString(InstructionDecode.ID_EX_IR));
      System.out.println("ID/EX.NPC: " + UtilityFunctions.to64BitHexString(InstructionDecode.ID_EX_NPC));
      System.out.println("ID/EX.A: " + UtilityFunctions.to64BitHexString(InstructionDecode.ID_EX_A));
      System.out.println("ID/EX.B: " + UtilityFunctions.to64BitHexString(InstructionDecode.ID_EX_B));
      System.out.println("ID/EX.IMM: " + UtilityFunctions.to64BitHexString(InstructionDecode.ID_EX_IMM));
   }

}
