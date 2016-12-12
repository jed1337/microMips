
package micromips.processor;

import models.Storage;
import utilities.UtilityFunctions;

public class MemoryAccess {
    
    public static long MEM_WB_LMD = 0;
    public static int MEM_WB_IR = 0;
    public static long MEM_WB_ALU_OUTPUT = 0;
    
    public static long calVal = 0x0;
    
    public static void memoryAccess(){
        
        MemoryAccess.MEM_WB_ALU_OUTPUT = Execution.EX_MEM_ALU_OUTPUT;
        MemoryAccess.MEM_WB_IR = Execution.EX_MEM_IR;
        String binaryIR = UtilityFunctions.to32BitBinString(MemoryAccess.MEM_WB_IR);
        switch(binaryIR.substring(0,6)){            
            case "110111": //LD
                MemoryAccess.MEM_WB_LMD = Storage.dataLoadDouble((int)MemoryAccess.MEM_WB_ALU_OUTPUT);
                break;
            case "111111": //SD
                Storage.dataStoreDouble((int)MemoryAccess.MEM_WB_ALU_OUTPUT, Execution.EX_MEM_B);
                break;
            default:
                break;
        }       
        
    }
    
    public static void printContents(){        
        System.out.println("--MEMORY ACCESS--");
        System.out.println("MEM/WB.IR: " + UtilityFunctions.to32BitHexString(MemoryAccess.MEM_WB_IR));
        System.out.println("MEM/WB.LMD: " + UtilityFunctions.to64BitHexString(MemoryAccess.MEM_WB_LMD));
        System.out.println("MEM/WB.ALUOutput: " + UtilityFunctions.to64BitHexString(MemoryAccess.MEM_WB_ALU_OUTPUT));
    }
    
}
