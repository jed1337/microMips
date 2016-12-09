
package utilities;

public class Test { 
    
   
    
    public static void main(String args[]){
        
        System.out.println(
                String.format("%08x", 
                        UtilityFunctions.getOpCode("LD", new int[]{0x1, 0x1000, 0x2})));
       System.out.println("Util test!");        
    }
}
