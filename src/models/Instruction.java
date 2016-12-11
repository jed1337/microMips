package models;

import utilities.INSTRUCTION;

public class Instruction {
   private final INSTRUCTION instructionType;
   private final String label;
   private final int[] argument;

   public Instruction(INSTRUCTION instructionType, String label, int[] argument) {
      this.instructionType = instructionType;
      this.label = label;
      this.argument = argument;
   }

   public INSTRUCTION getInstructionType() {
      return instructionType;
   }

   public String getLabel() {
      return label;
   }

   public int[] getArgument() {
      return argument;
   }
}
