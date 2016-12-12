package micromips.processor;

import utilities.UtilityFunctions;

public class Availability {
   private final int regNum;
   private final CYCLE_NAME cycleName;

   private Availability(int regNum, CYCLE_NAME cycleName) {
      this.regNum = regNum;
      this.cycleName = cycleName;
   }
   
   public static Availability getAvailability(int instruction) {
      String binaryIR = UtilityFunctions.to32BitBinString(instruction);
      
      final String opcode = binaryIR.substring(0, 6);
      final String regB = binaryIR.substring(11, 16);
      final String regRD = binaryIR.substring(16, 21);
      
      switch (opcode) {
         case "000000":
            switch (binaryIR.substring(26, 32)) {
               case "100110": //XOR                        
               case "101111": //DSUBU
               case "101010": //SLT
                  return new Availability(Integer.parseInt(regRD, 2), CYCLE_NAME.EX);
               case "000000": //NOP
                  break;
            }
            break;
         case "011001": //DADDIU                
            return new Availability(Integer.parseInt(regB, 2), CYCLE_NAME.EX);
         case "110111": //LD
            return new Availability(Integer.parseInt(regB, 2), CYCLE_NAME.MEM);
            
         case "001000": //BEQC   
         case "110010": //BC
         case "111111": //SD
            break;
         default:
            System.err.println("Availability  has an unknown instruction "+instruction+" Opcode: "+opcode);
      }
      return null;
   }

   public int getRegNum() {
      return regNum;
   }

   public CYCLE_NAME getCycleName() {
      return cycleName;
   }
}
