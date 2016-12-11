package micromips.processor;

public class Test {
   public static void main(String[] args) {
//        Storage.dataStoreDouble(0x3000, 0x5544332211EFCDABL);
//        Storage.dataStoreDouble(0x3008, 0x8899AABBCCDDEEFFL);
//        System.out.println(String.format("%016x", Storage.dataLoadDouble(0x3006)));

      Scheduler scheduler = new Scheduler();
      scheduler.setInput(
         "BEQC r11, r12, label      " +"\n"+
         "LD r1, 1000(r3)	" +"\n"+
         "SD r7, 1000(r9)	" +"\n"+
         " NOP 		" +"\n"+
         "label: DADDIU r1, r2, 1000			" +"\n"
      );
      scheduler.applyModifications();
      scheduler.setOpcodes();
   }
}
