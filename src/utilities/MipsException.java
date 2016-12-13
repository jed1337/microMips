package utilities;

public class MipsException extends Exception {

    public MipsException(String message) {
        super(message);
    }

    public MipsException(String invalidInstruction, String message) {
        super(String.format("Instruction: '%s'\nMessage: '%s'", invalidInstruction, message));
    }
}
