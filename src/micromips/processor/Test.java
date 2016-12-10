package micromips.processor;

import models.Storage;

public class Test {
    public static void main(String[] args) {
        Storage.dataStoreDouble(0x3000, 0x5544332211EFCDABL);
        Storage.dataStoreDouble(0x3008, 0x8899AABBCCDDEEFFL);
        System.out.println(String.format("%016x", Storage.dataLoadDouble(0x3006)));
    }
}
