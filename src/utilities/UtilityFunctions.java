package utilities;

public class UtilityFunctions {

    private static int replaceBits(int value, int replacedValue, int pos, int numBits) {
        //int represent = Integer.SIZE - Integer.numberOfLeadingZeros(replacedValue);

        int newPos = 32 - (pos + numBits);

        value &= ~((~0 << (32 - numBits)) >>> pos);

        int bit_mask = replacedValue << newPos;
        value = (value & (~bit_mask)) | (replacedValue << newPos);

        return value;
    }

    public static int getOpCode(INSTRUCTION_TYPE inType, int[] args) {

//        INSTRUCTION in = INSTRUCTION.getInstruction(in);
        int[] posArray = inType.getArgPos();
        int[] lengthArray = inType.getArgLengths();
        int opCode = inType.getInstructionFormat();

        if (args.length == posArray.length) {
            for (int i = 0; i < args.length; i++) {
                opCode = replaceBits(opCode, args[i], posArray[i], lengthArray[i]);
            }
        }

        return opCode;
    }
    
    public static String to32BitBinString(int val){
        String s = Integer.toBinaryString(val);
        int numPadding = 32 - s.length();
        while(numPadding > 0){
            s = '0' + s;
            numPadding--;
        }
        
        return s;
    }
    
    public static long toSignedExtendedImmediate(int val){
        String s = Integer.toBinaryString(val);
        int numPadding = 16 - s.length();
        char pad = '0';
        while(numPadding > 0){
            s = pad + s;
            numPadding--;
        }
        numPadding = 64 - s.length();
        if(s.charAt(0) == '1'){
            pad = '1';
        }
        while(numPadding > 0){
            s = pad + s;
            numPadding--;
        }
        return Long.parseUnsignedLong(s, 2);
    }
    
    public static String to32BitHexString(int val){
        return String.format("%08x", val).toUpperCase();
    }
    
    public static String to64BitHexString(int val){
        return String.format("%016x", (long)val).toUpperCase();
    }

    public static String to64BitHexString(long val){
        return String.format("%016x", val).toUpperCase();
    }
    
}
