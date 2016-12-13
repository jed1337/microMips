package models;

import utilities.INSTRUCTION_TYPE;

public class Instruction {

    private final INSTRUCTION_TYPE instructionType;
    private final String label;
    private int[] argument;

    public Instruction(INSTRUCTION_TYPE instructionType, String label, int[] argument) {
        this.instructionType = instructionType;
        this.label = label;
        this.argument = argument;
    }

    public INSTRUCTION_TYPE getInstructionType() {
        return instructionType;
    }

    public String getLabel() {
        return label;
    }

    public int[] getArguments() {
        return argument;
    }

    public void setArguementAt(int index, int value) {
        this.argument[index] = value;
    }
}
