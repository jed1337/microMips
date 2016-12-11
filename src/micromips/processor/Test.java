package micromips.processor;

public class Test {
   public static void main(String[] args) {
//        Storage.dataStoreDouble(0x3000, 0x5544332211EFCDABL);
//        Storage.dataStoreDouble(0x3008, 0x8899AABBCCDDEEFFL);
//        System.out.println(String.format("%016x", Storage.dataLoadDouble(0x3006)));

      Scheduler scheduler = new Scheduler();
      scheduler.setInput(
         "XOR r1, r2, r3	" +"\n"+
         "DSUBU r4, r5, r6      " +"\n"+
         "SLT r7, r8, r9	" +"\n"+
         "NOP 			" +"\n"+
         "label: NOP 		" +"\n"
      );
   }
}
