
package micromips.processor;

public class MemoryAccess {
    
    private static long MEM_WB_LMD = 0;
    private static long MEM_WB_IR = 0;
    private static long MEM_WB_ALU_OUTPUT = 0;
    
    
    public static void setIR(long IR){
        MemoryAccess.MEM_WB_IR = IR;
    }
    
    public static void setALUOutput(long ALUOutput){
        MemoryAccess.MEM_WB_ALU_OUTPUT = ALUOutput;
    }
    
    public static long getLMD(){
        return MemoryAccess.MEM_WB_LMD;
    }
    
    public static long getIR(){
        return MemoryAccess.MEM_WB_IR;
    }
    
    public static long getALUOutput(){
        return MemoryAccess.MEM_WB_ALU_OUTPUT;
    }
    
    public static void memoryAccess(){
        
    }
    
}
