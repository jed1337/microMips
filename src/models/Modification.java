package models;

public class Modification {
   private final int instructionIndex;
   private final String label;
   private final int argumentIndex;

   public Modification(int instructionIndex, String label, int argumentIndex) {
      this.instructionIndex = instructionIndex;
      this.label = label;
      this.argumentIndex = argumentIndex;
   }

   public int getInstructionIndex() {
      return instructionIndex;
   }

   public String getLabel() {
      return label;
   }

   public int getArgumentIndex() {
      return argumentIndex;
   }
   
   
}
