package micromips.processor;

public class InstructionDecode {
    
    private static long ID_EX_A = 0;
    private static long ID_EX_B = 0;
    private static long ID_EX_IMM = 0;
    private static long ID_EX_IR = 0;
    private static long ID_EX_NPC = 0;

    public static void setIR(long IR){
        InstructionDecode.ID_EX_IR = IR;
    }
    
    public static void setNPC(long NPC){
        InstructionDecode.ID_EX_NPC = NPC;
    }
    
    public static long getA(){
        return ID_EX_A;
    }
    
    public static long getB(){
        return ID_EX_B;
    }
    
    public static long getIMM(){
        return ID_EX_IMM;
    }
    
    public static long getIR(){
        return ID_EX_IR;
    }
    
    public static long getNPC(){
        return ID_EX_NPC;
    }
            
    public static void decode(){
        
    }
    
    
}
