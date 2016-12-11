package utilities;

public enum INSTRUCTION_TYPE {
   XOR("XOR", new int[]{16, 6, 11}, new int[]{5, 5, 5}),
   DSUBU("DSUBU", new int[]{16, 6, 11}, new int[]{5, 5, 5}),
   SLT("SLT", new int[]{16, 6, 11}, new int[]{5, 5, 5}),
   NOP("NOP", new int[]{}, new int[]{}),
   BEQC("BEQC", new int[]{6, 11, 16}, new int[]{5, 5, 16}),
   LD("LD", new int[]{11, 16, 6}, new int[]{5, 16, 5}),
   SD("SD", new int[]{11, 16, 6}, new int[]{5, 16, 5}),
   DADDIU("DADDIU", new int[]{11, 6, 16}, new int[]{5, 5, 16}),
   BC("BC", new int[]{6}, new int[]{26});

   private final String opCode;
   private final int[] argPos;
   private final int[] argLengths;

   public static INSTRUCTION_TYPE getInstructionType(String instruction) {
      for (INSTRUCTION_TYPE i : values()) {
         if (i.opCode.equalsIgnoreCase(instruction)) {
            return i;
         }
      }
      return null;
   }

   private INSTRUCTION_TYPE(String opCode, int[] argPos, int[] argLengths) {
      this.opCode = opCode;
      this.argPos = argPos;
      this.argLengths = argLengths;
   }

   public int getInstructionFormat() {
      switch (this) {
         case XOR:
            return 0x26;
         case DSUBU:
            return 0x2F;
         case SLT:
            return 0x2A;
         case NOP:
            return 0x0;
         case BEQC:
            return 0x20000000;
         case LD:
            return 0xDC000000;
         case SD:
            return 0xFC000000;
         case DADDIU:
            return 0x64000000;
         case BC:
            return 0xC8000000;
         default:
            return 0;
      }
   }

   public int[] getArgPos() {
      return this.argPos;
   }

   public int[] getArgLengths() {
      return this.argLengths;
   }
}
