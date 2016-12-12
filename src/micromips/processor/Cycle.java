package micromips.processor;

public class Cycle {
    
    public static final String INSTRUCTION_FETCH = "IF";
    public static final String INSTRUCTION_DECODE = "ID";
    public static final String EXECUTION = "EX";
    public static final String MEMORY_ACCESS = "MEM";
    public static final String WRITE_BACK = "WB";
    public static final String STALL = "*";
    
    private final String cycleString;
    private final int instructionNo;
    
    public Cycle(String cycleString, int instructionNo){
        this.cycleString = cycleString;
        this.instructionNo = instructionNo;
    }
    
    public String getCycleString(){
        return this.cycleString;
    }
    
    public int getInstructionNo(){
        return this.instructionNo;
    }
    
}
