package micromips.processor;

public class Execution {
    
    private static long EX_MEM_ALU_OUTPUT = 0;
    private static boolean EX_MEM_COND = false;
    private static long EX_MEM_IR = 0;
    private static long EX_MEM_B = 0;
    
    public static void setIR(long IR){
        Execution.EX_MEM_IR = IR;
    }
    
    public static void setB(long B){
        Execution.EX_MEM_B = B;
    }
    
    public static long getALUOutput(){
        return Execution.EX_MEM_ALU_OUTPUT;
    }
    
    public static boolean getCond(){
        return Execution.EX_MEM_COND;
    }
    
    public static long getIR(){
        return Execution.EX_MEM_IR;
    }
    
    public static long getB(){
        return Execution.EX_MEM_B;
    }
    
    public static void execute(){
        
    }
    
}
