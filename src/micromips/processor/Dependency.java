package micromips.processor;

import java.util.ArrayList;
import utilities.UtilityFunctions;

public class Dependency {
   private final int regNum;
   private final CYCLE_NAME cycleName;

   private Dependency(int regNum, CYCLE_NAME cycleName) {
      this.regNum = regNum;
      this.cycleName = cycleName;
   }
   
   public static ArrayList<Dependency> getDependencies(int instruction) {
      ArrayList<Dependency> dependencies = new ArrayList<>();
      String binaryIR = UtilityFunctions.to32BitBinString(instruction);
      
      final String opcode = binaryIR.substring(0, 6);
      final String regA = binaryIR.substring(6, 11);
      final String regB = binaryIR.substring(11, 16);
      
      switch (opcode) {
         case "000000":
            switch (binaryIR.substring(26, 32)) {
               case "100110": //XOR                        
               case "101111": //DSUBU
               case "101010": //SLT
                  dependencies.add(new Dependency(Integer.parseInt(regA, 2), CYCLE_NAME.EX));
                  dependencies.add(new Dependency(Integer.parseInt(regB, 2), CYCLE_NAME.EX));
                  break;
               case "000000": //NOP
                  break;
            }
            break;
         //Untested since we haven't tested on branches yet
         case "001000": //BEQC   
            dependencies.add(new Dependency(Integer.parseInt(regA, 2), CYCLE_NAME.ID));
            dependencies.add(new Dependency(Integer.parseInt(regB, 2), CYCLE_NAME.ID));                  
            break;
         case "110010": //BC
            break;
         case "011001": //DADDIU
         case "110111": //LD
            dependencies.add(new Dependency(Integer.parseInt(regA, 2), CYCLE_NAME.EX));
            break;
         case "111111": //SD
            dependencies.add(new Dependency(Integer.parseInt(regA, 2), CYCLE_NAME.EX));
            dependencies.add(new Dependency(Integer.parseInt(regB, 2), CYCLE_NAME.MEM));
            break;
         default:
            System.err.println("Dependency has an unknown instruction "+instruction+" Opcode: "+opcode);
      }
      return dependencies;
   }

   public int getRegNum() {
      return regNum;
   }

   public CYCLE_NAME getCycleName() {
      return cycleName;
   }
}
