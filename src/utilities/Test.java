
package utilities;

public class Test { 
    public static void main(String args[]){
        
       int opcode = UtilityFunctions.getOpCode(INSTRUCTION_TYPE.LD, new int[]{0x1, 0x1000, 0x2});
       System.out.println(UtilityFunctions.to32BitHexString(opcode));
       System.out.println("Util test!");
    }
}
