package micromips.processor;

import models.Storage;
import utilities.UtilityFunctions;

public class Test {
   public static void main(String[] args) {
//        Storage.dataStoreDouble(0x3000, 0x5544332211EFCDABL);
//        Storage.dataStoreDouble(0x3008, 0x8899AABBCCDDEEFFL);
//        System.out.println(String.format("%016x", Storage.dataLoadDouble(0x3006)));

      Scheduler scheduler = new Scheduler();
      /*
      scheduler.setInput(
         "BEQC r11, r12, label      " +"\n"+
         "LD r1, 1000(r3)	" +"\n"+
         "SD r7, 1000(r9)	" +"\n"+
         " NOP 		" +"\n"+
         "label: DADDIU r1, r2, 1000			" +"\n"
      );
      scheduler.applyModifications();
      scheduler.setOpcodes();
      */
      
      Storage.storeRegisterValue(1, 0x2);
      Storage.storeRegisterValue(2, 0x8);
      Storage.storeRegisterValue(3, 0x4);
      Storage.storeRegisterValue(4, 0x5);
      Storage.storeRegisterValue(5, 0x8);
      Storage.storeRegisterValue(6, 0x1);      
      Storage.storeRegisterValue(8, 0x4);
      
      Storage.dataStoreDouble(0x3000, 0x8967452301EFCDABL);
      Storage.dataStoreDouble(0x3008, 0x5544332211EFCDABL);
      
      scheduler.setInput("LD R1, 3000(R2) \n DADDIU R3, R0, 0003\n DSUBU R5,R1,R3 \n SD R5, 3000(R7) \n");
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
   }
}
