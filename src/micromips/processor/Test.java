package micromips.processor;

import models.Storage;
import utilities.UtilityFunctions;

public class Test {
   public static void main(String[] args) {
//        Storage.dataStoreDouble(0x3000, 0x5544332211EFCDABL);
//        Storage.dataStoreDouble(0x3008, 0x8899AABBCCDDEEFFL);
//        System.out.println(String.format("%016x", Storage.dataLoadDouble(0x3006)));

      Scheduler scheduler = new Scheduler();
      
      // Storage.storeRegisterValue(1, 0x2);
      // Storage.storeRegisterValue(2, 0x8);
      // Storage.storeRegisterValue(3, 0x4);
      // Storage.storeRegisterValue(4, 0x5);
      // Storage.storeRegisterValue(5, 0x8);
      // Storage.storeRegisterValue(6, 0x1);
      // Storage.storeRegisterValue(8, 0x4);
      
      Storage.dataStoreDouble(0x3000, 0x8967452301EFCDABL);
      Storage.dataStoreDouble(0x3008, 0x5544332211EFCDABL);
      
      Storage.storeRegisterValue(2, 0x2);
      
      scheduler.setInput(
         "daddiu r1, r0, 2"             +"\n"
         +"beqc r1, r2, label"          +"\n"
         +"NOP"                         +"\n"
         +"NOP "                        +"\n"
         +"NOP "                        +"\n"
         +"label: DADDIU R6, r7, 8 "    +"\n"
      );
//      scheduler.setInput(
//         "bc label "+"\n"
//         +"DSUBU R5, R1, R3 "+"\n"
//         +"NOP "+"\n"
//         +"NOP "+"\n"
//         +"NOP "+"\n"
//         +"label: DADDIU R1, R2, 3 "+"\n"
//      );

      scheduler.applyModifications();
      scheduler.setOpcodes();
      scheduler.storeOpcodes();
      
      System.out.println("--Cycle 1--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 2--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 3--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 4--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 5--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 6--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 7--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 8--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 9--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 10--");
      scheduler.runOneCycle();
      System.out.println("--Cycle 11--");
      scheduler.runOneCycle();
      
      System.out.println(UtilityFunctions.to64BitHexString(Storage.dataLoadDouble(0x3000)));
      System.out.println(UtilityFunctions.to64BitHexString(Storage.dataLoadDouble(0x3008)));
   }
}
