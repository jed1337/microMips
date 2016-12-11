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

    public static int getOpCode(INSTRUCTION inType, int[] args) {

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
    
}
