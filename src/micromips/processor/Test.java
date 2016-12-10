package micromips.processor;

import models.Storage;

public class Test {

    public static void main(String[] args) {
        Storage.memoryStoreDouble(0x3000, 0x5544332211EFCDABL);
        Storage.memoryStoreDouble(0x3008, 0x8899AABBCCDDEEFFL);
        System.out.println(String.format("%016x", Storage.memoryLoadDouble(0x3006)));
    }

}
