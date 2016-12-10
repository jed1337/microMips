package micromips.processor;

import models.Storage;
import utilities.AssemblyRegex;

public class Test {

   public static void main(String[] args) {
      Storage.storeDouble(0x3000, 0x5544332211EFCDABL);
      Storage.storeDouble(0x3008, 0x8899AABBCCDDEEFFL);
      
      AssemblyRegex ar = new AssemblyRegex();
      ar.test();
   }
}
