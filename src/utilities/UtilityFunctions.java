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

    public static int getOpCode(String instruction, int[] args) {

        INSTRUCTION in = INSTRUCTION.getInstruction(instruction);
        int[] posArray = in.getArgPos();
        int[] lengthArray = in.getArgLengths();
        int opCode = in.getInstructionFormat();

        if (args.length == posArray.length) {
            for (int i = 0; i < args.length; i++) {
                opCode = replaceBits(opCode, args[i], posArray[i], lengthArray[i]);
            }
        }

        return opCode;
    }
    
    public static String to32BitHexString(int val){
        return String.format("%08x", val);
    }
    
    public static String to64BitHexString(int val){
        return String.format("%016x", (long)val);
    }

    public static String to64BitHexString(long val){
        return String.format("%016x", val);
    }
    
}
