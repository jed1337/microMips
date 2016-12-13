package micromips.processor;

import models.Storage;
import utilities.UtilityFunctions;

public class Writeback {

   private static int regNum = -1;
   private static String val = "N/A";

   public static void writeback() {
      regNum = -1;
      val = "N/A";

      String binaryIR = UtilityFunctions.to32BitBinString(MemoryAccess.MEM_WB_IR);
      switch (binaryIR.substring(0, 6)) {
         case "000000":
            switch (binaryIR.substring(26, 32)) {
               case "100110": //XOR                        
               case "101111": //DSUBU                        
               case "101010": //SLT
                  Storage.storeRegisterValue(Integer.parseInt(binaryIR.substring(16, 21), 2), MemoryAccess.MEM_WB_ALU_OUTPUT);
                  regNum = Integer.parseInt(binaryIR.substring(16, 21), 2);
                  val = UtilityFunctions.to64BitHexString(MemoryAccess.MEM_WB_ALU_OUTPUT);
                  break;
               case "000000": //NOP
                  break;
               default:
                  break;
            }
            break;
         case "001000": //BEQC                
            break;
         case "011001": //DADDIU
            Storage.storeRegisterValue(Integer.parseInt(binaryIR.substring(11, 16), 2), MemoryAccess.MEM_WB_ALU_OUTPUT);
            regNum = Integer.parseInt(binaryIR.substring(11, 16), 2);
            val = UtilityFunctions.to64BitHexString(MemoryAccess.MEM_WB_ALU_OUTPUT);
            break;
         case "110111": //LD
            Storage.storeRegisterValue(Integer.parseInt(binaryIR.substring(11, 16), 2), MemoryAccess.MEM_WB_LMD);
            regNum = Integer.parseInt(binaryIR.substring(11, 16), 2);
            val = UtilityFunctions.to64BitHexString(MemoryAccess.MEM_WB_LMD);
            break;
         case "111111": //SD                
            break;
         default:
            break;
      }
      MemoryAccess.hasOldValue = true;
   }

   public static void printContents() {
      System.out.println("--WRITEBACK--");
      if (regNum < 0) {
         System.out.println("Rn: " + val);
      } else {
         System.out.println("R" + regNum + ": " + val);
      }
   }
}
