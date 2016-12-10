package models;

import java.nio.ByteBuffer;
import utilities.Errors;
import utilities.MipsException;

public class Storage {
   private static final long[] registers = new long[31];

   private final static byte[] codeSegment = new byte[8191];
   private final static byte[] dataSegment = new byte[8191];

   private final static int codeSegmentStart = 0x1000;
   private final static int codeSegmentEnd = 0x2FFF;

   private final static int dataSegmentStart = 0x3000;
   private final static int dataSegmentEnd = 0x4FFF;

   private static byte[] longToBytes(long x) {
      ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
      buffer.putLong(x);
      return buffer.array();
   }

   private static long bytesToLong(byte[] bytes) {
      ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
      buffer.put(bytes);
      buffer.flip();
      return buffer.getLong();
   }

   public static void storeRegisterValue(int loc, long val) {
      if (loc > 0 && loc < 32) {
         Storage.registers[loc - 1] = val;
      }
   }

   public static long getRegisterValue(int loc) {
      if (loc > 0 && loc < 32) {
         return Storage.registers[loc - 1];
      } else {
         return 0;
      }
   }   
   
   private static byte[] intToBytes(int x) {
      ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
      buffer.putInt(x);
      return buffer.array();
   }

   private static int bytesToInt(byte[] bytes) {
      ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
      buffer.put(bytes);
      buffer.flip();
      return buffer.getInt();
   }

   public static void memoryStoreDouble(int address, long val) {
      validateAddress(address);
      byte[] converted = Storage.longToBytes(val);

      for (int i = 0; i < converted.length; i++) {
         int currAddr = (address - dataSegmentStart) + (converted.length - 1) - i;
         dataSegment[currAddr] = converted[i];
         //System.out.println("At address " + Integer.toHexString(address + (converted.length - 1) - i) + ":" + String.format("%02x", converted[i]));
      }
   }

   public static long memoryLoadDouble(int address) {
      validateAddress(address);
      byte[] loaded = new byte[8];

      for (int i = 0; i < 8; i++) {
         int currAddr = (address - dataSegmentStart) + (loaded.length - 1) - i;
         loaded[i] = dataSegment[currAddr];
         //System.out.println("At address " + Integer.toHexString(address + (loaded.length - 1) - i) + ":" + String.format("%02x", loaded[i]));
      }

      return Storage.bytesToLong(loaded);
   }

   private static void validateAddress(int address) {
      if (address % 8 != 0) {
         Errors.addRuntimeError("Allignment error. " + address + " is not aligned to 8 bytes");
      }
      if (address < dataSegmentStart || address > (dataSegmentEnd - 0x8)) {
         Errors.addRuntimeError(address + " is an invalid access for the data segment.\n"
                 + "Valid values are from 0x3000 to 0x4FFF");
      }
   }

   public static void programStoreWord(int address, int val) {
      if (codeSegmentStart <= address && address <= (codeSegmentEnd - 0x4)) {
         byte[] converted = Storage.intToBytes(val);

         for (int i = 0; i < converted.length; i++) {
            int currAddr = (address - codeSegmentStart) + (converted.length - 1) - i;
            codeSegment[currAddr] = converted[i];
            //System.out.println("At address " + Integer.toHexString(address + (converted.length - 1) - i) + ":" + String.format("%02x", converted[i]));
         }
      }
   }

   public static int programLoadWord(int address) {
      if (codeSegmentStart <= address && address <= (codeSegmentEnd - 0x4)) {
         byte[] loaded = new byte[4];

         for (int i = 0; i < 4; i++) {
            int currAddr = (address - codeSegmentStart) + (loaded.length - 1) - i;
            loaded[i] = codeSegment[currAddr];
            //System.out.println("At address " + Integer.toHexString(address + (loaded.length - 1) - i) + ":" + String.format("%02x", loaded[i]));
         }

         return Storage.bytesToInt(loaded);
      } else {
         return 0;
      }
   }

}
