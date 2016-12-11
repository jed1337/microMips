package micromips.processor;

import models.Storage;
import utilities.UtilityFunctions;

public class InstructionFetch {

    public static int IF_ID_IR = 0;
    public static int IF_ID_NPC = 0;
    public static int PC = 0x1000;
    
    public static void fetch(){
        int oldPC = InstructionFetch.PC;
        
        String binaryIR = UtilityFunctions.to32BitBinString(InstructionFetch.IF_ID_IR);
        if(binaryIR.substring(0,6).equals("110010")){
            InstructionFetch.IF_ID_IR = InstructionFetch.PC += (Long.parseLong(binaryIR.substring(6,32), 2) * 4);
        }
        else if(binaryIR.substring(0,6).equals("001000")){
            if(Storage.getRegisterValue(Integer.parseInt(binaryIR.substring(6,11), 2))
                    == Storage.getRegisterValue(Integer.parseInt(binaryIR.substring(11,16), 2))){
                InstructionFetch.IF_ID_IR = InstructionFetch.PC += (Long.parseLong(binaryIR.substring(16,32), 2) * 4);
            }
            else{
                InstructionFetch.IF_ID_NPC = InstructionFetch.PC += 0x4;
            }
        }
        else{
            InstructionFetch.IF_ID_NPC = InstructionFetch.PC += 0x4;
        }
        
        InstructionFetch.IF_ID_IR = Storage.codeLoadWord(oldPC);        
    }

    public static void printContents(){        
        System.out.println("--INSTRUCTION FETCH--");
        System.out.println("IF/ID.IR: " + UtilityFunctions.to32BitHexString(InstructionFetch.IF_ID_IR));
        System.out.println("IF/ID.NPC: " + UtilityFunctions.to64BitHexString(InstructionFetch.IF_ID_NPC));
        System.out.println("PC: " + UtilityFunctions.to64BitHexString(InstructionFetch.PC));
    }
    
}
