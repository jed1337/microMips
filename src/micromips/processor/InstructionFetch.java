package micromips.processor;

public class InstructionFetch {

    private static int IF_ID_IR = 0;
    private static long IF_ID_NPC = 0;
    private static long PC = 0x1000;
    
    public static long getIR(){
        return IF_ID_IR;
    }
    
    public static long getNPC(){
        return IF_ID_NPC;
    }
    
    
    public static void fetch(){
        
    }
    

}
