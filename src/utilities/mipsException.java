package utilities;

class mipsException extends Exception{
   private final String INVALID_INSTRUCTION;

   public mipsException(String invalidInstruction, String message) {
      super(message);
      this.INVALID_INSTRUCTION = invalidInstruction;
   }

   public mipsException(String invalidInstruction, String message, Throwable cause) {
      super(message, cause);
      this.INVALID_INSTRUCTION = invalidInstruction;
   }

   public mipsException(String invalidInstruction, Throwable cause) {
      super(cause);
      this.INVALID_INSTRUCTION = invalidInstruction;
   }

   public mipsException(String invalidInstruction, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
      this.INVALID_INSTRUCTION = invalidInstruction;
   }
}
