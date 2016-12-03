
package utilities;

public class Test { 
    
   
    
    public static void main(String args[]){
        
        System.out.println(
                Integer.toHexString(
                        UtilityFunctions.getOpCode("LD", new int[]{0x1, 0x1000, 0x2})));
        
    }
}
