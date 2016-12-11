package micromips.processor;

import utilities.UtilityFunctions;

public class Execution {
    
    public static long EX_MEM_ALU_OUTPUT = 0;
    public static boolean EX_MEM_COND = false;
    public static int EX_MEM_IR = 0;
    public static long EX_MEM_B = 0;
    
    public static void execute(){
        
        Execution.EX_MEM_B = InstructionDecode.ID_EX_B;
        Execution.EX_MEM_IR = InstructionDecode.ID_EX_IR;
        
        String binaryIR = UtilityFunctions.to32BitBinString(Execution.EX_MEM_IR);     
        switch(binaryIR.substring(0,6)){
            case "000000":
                switch(binaryIR.substring(26, 32)){
                    case "100110": //XOR
                        Execution.EX_MEM_ALU_OUTPUT = InstructionDecode.ID_EX_A ^ InstructionDecode.ID_EX_B;
                        Execution.EX_MEM_COND = false;
                        break;
                    case "101111": //DSUBU
                        Execution.EX_MEM_ALU_OUTPUT = InstructionDecode.ID_EX_A - InstructionDecode.ID_EX_B;
                        Execution.EX_MEM_COND = false;
                        break;
                    case "101010": //SLT
                        if(InstructionDecode.ID_EX_A < InstructionDecode.ID_EX_B){
                            Execution.EX_MEM_ALU_OUTPUT = 0x1;
                        }
                        else{
                            Execution.EX_MEM_ALU_OUTPUT = 0x0;
                        }
                        Execution.EX_MEM_COND = false;
                        break;
                    case "000000": //NOP
                        break;
                    default:
                        break;
                }
                break;
            case "001000": //BEQC
                Execution.EX_MEM_ALU_OUTPUT = InstructionDecode.ID_EX_NPC + (InstructionDecode.ID_EX_IMM * 4);
                if(InstructionDecode.ID_EX_A == InstructionDecode.ID_EX_B){
                    Execution.EX_MEM_COND = true;
                }
                else{
                    Execution.EX_MEM_COND = false;
                }
                break;
            case "011001": //DADDIU
            case "110111": //LD
            case "111111": //SD
                Execution.EX_MEM_ALU_OUTPUT = InstructionDecode.ID_EX_A + InstructionDecode.ID_EX_IMM;
                Execution.EX_MEM_COND = false;
                break;
            default:
                break;
        }        
    }
    
    public static void printContents(){       
        System.out.println("--EXECUTION--");
        System.out.println("EX/MEM.IR: " + UtilityFunctions.to32BitHexString(Execution.EX_MEM_IR));
        System.out.println("EX/MEM.ALUOutput: " + UtilityFunctions.to64BitHexString(Execution.EX_MEM_ALU_OUTPUT));
        System.out.println("EX/MEM.COND: " + Execution.EX_MEM_COND);
        System.out.println("EX/MEM.B: " + UtilityFunctions.to64BitHexString(Execution.EX_MEM_B));
    }
    
}
