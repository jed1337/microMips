package micromips.processor;

import models.Storage;

public class Test {

   public static void main(String[] args) throws Exception {
      Storage.storeDouble(0x3001, 0x5544332211EFCDABL);
//      Storage.storeDouble(0x3008, 0x8899AABBCCDDEEFFL);
//      Storage.storeDouble(0x300A, 11);
      System.out.println("");
   }
}
