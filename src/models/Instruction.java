package models;

public class Instruction {
   private String instructionType;
   private String label;
   private int[] argument;

   public Instruction(String instructionType, String label, int[] argument) {
      this.instructionType = instructionType;
      this.label = label;
      this.argument = argument;
   }

   public String getInstructionType() {
      return instructionType;
   }

   public String getLabel() {
      return label;
   }

   public int[] getArgument() {
      return argument;
   }
   
   
}
