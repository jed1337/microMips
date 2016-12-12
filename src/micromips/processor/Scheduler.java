package micromips.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import models.Instruction;
import models.Modification;
import models.Storage;
import utilities.AssemblyRegex;
import utilities.Errors;
import utilities.INSTRUCTION_TYPE;
import utilities.INSTRUCTION_CONTENTS;
import static utilities.INSTRUCTION_CONTENTS.*;
import utilities.UtilityFunctions;

public class Scheduler {

    private final ArrayList<Instruction> instructions;
    private final ArrayList<Modification> modifications;
    private final ArrayList<Integer> opcodes;

    private int index_WB = -1;
    private int index_MEM = -1;
    private int index_EX = -1;
    private int index_ID = -1;
    private int index_IF = -1;

    private int endPC = 0x1000;

    public Scheduler() {
        instructions = new ArrayList<>();
        modifications = new ArrayList<>();
        opcodes = new ArrayList<>();
    }

    public void setInput(String jText) {
        for (String input : jText.split("\n")) {
            AssemblyRegex ar = new AssemblyRegex(input);
            if (!Errors.getParsingErrors().isEmpty()) {
                System.err.println(input + " has an error!");
            } else {
                createInstruction(ar.getInstructionContents());
            }
        }
    }

    public void applyModifications() {
        hasDuplicateLabel();
        for (Modification modification : modifications) {
            int mIndex = modification.getInstructionIndex();
            String mLabel = modification.getLabel();

            boolean labelExists = false;
            for (int i = 0; i < instructions.size(); i++) {
                String iLabel = instructions.get(i).getLabel();

                if (mLabel.equals(iLabel)) {
                    labelExists = true;

                    int offset = i - (mIndex + 1);
                    instructions.get(modification.getInstructionIndex())
                            .setArguementAt(modification.getArgumentIndex(), offset);

                    break;
                }
            }
            if (!labelExists) {
                Errors.addParsingError(mLabel, "Is nonexistent");
            }
        }
        System.out.println("Test end");
    }

    public void setOpcodes() {
        for (Instruction instruction : instructions) {
            opcodes.add(
                    UtilityFunctions.getOpCode(
                            instruction.getInstructionType(), instruction.getArguments()
                    ));
        }
    }

    public void storeOpcodes() {
        int addr = 0x1000;
        for (Integer i : opcodes) {
            Storage.codeStoreWord(addr, i);

            addr += 0x4;
        }
        endPC = addr;
    }

    private void hasDuplicateLabel() {
        List<String> iLabels = instructions.stream()
                .map(ins -> ins.getLabel())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int iDitinctSize = iLabels.stream()
                .distinct()
                .collect(Collectors.toList())
                .size();

        if (iLabels.size() != iDitinctSize) {
            Errors.addParsingError(Arrays.toString(iLabels.toArray()), "Contains duplicate labels");
        }
    }

    private Instruction createInstruction(EnumMap<INSTRUCTION_CONTENTS, String> ics) {
        Instruction instruction = new Instruction(null, null, null);
        INSTRUCTION_TYPE inType = INSTRUCTION_TYPE.getInstructionType(ics.get(OPCODE));
        switch (inType) {
            case XOR:
            case DSUBU:
            case SLT:
                instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
                        ics.get(RD),
                        ics.get(RS),
                        ics.get(RT)
                )));
                break;
            case NOP:
                instructions.add(new Instruction(inType, ics.get(LABEL), new int[]{}));
                break;
            case LD:
            case SD:
                instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
                        ics.get(RT),
                        ics.get(IMM),
                        ics.get(RS)
                )));
                break;
            case DADDIU:
                instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
                        ics.get(RT),
                        ics.get(RS),
                        ics.get(IMM)
                )));
                break;
            case BEQC:
                instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
                        ics.get(RS),
                        ics.get(RT),
                        "0"
                )));
                modifications.add(new Modification(instructions.size() - 1, ics.get(IMM), 2));
                //Add modification
                break;
            case BC:
                instructions.add(new Instruction(inType, ics.get(LABEL), getInArgs(
                        "0"
                )));
                modifications.add(new Modification(instructions.size() - 1, ics.get(IMM), 0));
                break;
        }
        return instruction;
    }

    private int[] getInArgs(String... strings) {
        int[] args = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            //A regsiter
            if (strings[i].toLowerCase().charAt(0) == 'r') {
                args[i] = Integer.parseInt(strings[i].substring(1));
                if (args[i] < 0 || args[i] > 31) {
                    Errors.addParsingError("R" + strings[i] + " Is an invalid register", "Valid registers are from r0 to r31");
                }
            } //An immediate
            else {
                args[i] = Integer.parseInt(strings[i], 16);
            }
        }
        return args;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public ArrayList<Modification> getModifications() {
        return modifications;
    }

    public ArrayList<Integer> getOpcodes() {
        return opcodes;
    }

    public int getToBeUpdated(int opCode) {
        String binaryIR = UtilityFunctions.to32BitBinString(opCode);
        switch (binaryIR.substring(0, 6)) {
            case "000000":
                switch (binaryIR.substring(26, 32)) {
                    case "100110": //XOR                        
                    case "101111": //DSUBU
                    case "101010": //SLT
                        return Integer.parseInt(binaryIR.substring(16, 21), 2);
                    case "000000": //NOP
                        break;
                    default:
                        break;
                }
                break;
            case "001000": //BEQC   
                break;
            case "011001": //DADDIU                
            case "110111": //LD
                return Integer.parseInt(binaryIR.substring(11, 16), 2);
            case "111111": //SD
                break;
            default:
                break;
        }
        return -1;
    }

    public int[] getDependencies(int opCode) {
        String binaryIR = UtilityFunctions.to32BitBinString(opCode);
        switch (binaryIR.substring(0, 6)) {
            case "000000":
                switch (binaryIR.substring(26, 32)) {
                    case "100110": //XOR                        
                    case "101111": //DSUBU
                    case "101010": //SLT
                        return new int[]{
                            Integer.parseInt(binaryIR.substring(6, 11), 2),
                            Integer.parseInt(binaryIR.substring(11, 16), 2)
                        };
                    case "000000": //NOP
                        break;
                    default:
                        break;
                }
                break;
            case "001000": //BEQC   
                return new int[]{
                    Integer.parseInt(binaryIR.substring(6, 11), 2),
                    Integer.parseInt(binaryIR.substring(11, 16), 2)
                };
            case "011001": //DADDIU                
            case "110111": //LD
                return new int[]{
                    Integer.parseInt(binaryIR.substring(6, 11), 2)
                };
            case "111111": //SD
                return new int[]{
                    Integer.parseInt(binaryIR.substring(6, 11), 2),
                    Integer.parseInt(binaryIR.substring(11, 16), 2)
                };
            default:
                break;
        }
        return new int[]{};
    }

    private int indexOf(int[] array, int val) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == val) {
                return i;
            }
        }
        return -1;
    }

    public boolean checkAndForward(int opCode, int[] prevOpCodes, String stage) {
//        System.out.println("opCode: " + UtilityFunctions.to32BitHexString(opCode) + ": " + getToBeUpdated(opCode));
        for (int i = 0; i < prevOpCodes.length; i++) {
            int currOpCodeChecked = prevOpCodes[i];
//            System.out.println("opCodeCheck: " + UtilityFunctions.to32BitHexString(currOpCodeChecked) + ": " + Arrays.toString(getDependencies(currOpCodeChecked)));
            if (Arrays.stream(getDependencies(currOpCodeChecked))
                    .anyMatch(j -> j == getToBeUpdated(opCode))) {
//                System.out.println("Dependecy!");
                if (UtilityFunctions.to32BitBinString(opCode).substring(0, 6).equals("110111")) { //check if opcode is a load
                    if (stage.equals("MEM")) { //check if load instruction is in MEM (is the data ready?)
                        int index = indexOf(getDependencies(currOpCodeChecked), getToBeUpdated(opCode));
                        if (index == 0) { //checks which value override
                            InstructionDecode.ID_EX_A = MemoryAccess.MEM_WB_LMD;
                        } else {
                            InstructionDecode.ID_EX_B = MemoryAccess.MEM_WB_LMD;
                        }
//                        System.out.println("MEM Forward!");
                        return false;
                    } else { //not mem
                        if (!UtilityFunctions.to32BitBinString(currOpCodeChecked).substring(0, 6).equals("111111")) { //check if checked opcode is not store
                            //load is at EX
                            if (i == 0) { //that means ALU operation. If index is 0 then, it is already going to EX
//                                System.out.println("Stall!");
                                return true; //stall
                            }
                        }
                    }
                } else {//not a load REGLAR ALU
                    if (stage.equals("MEM")) { //is mem
                        if (UtilityFunctions.to32BitBinString(currOpCodeChecked).substring(0, 6).equals("111111")) {// not a store
                            if (i == 1) {
                                int index = indexOf(getDependencies(currOpCodeChecked), getToBeUpdated(opCode));
                                if (index == 0) {
                                    InstructionDecode.ID_EX_A = MemoryAccess.MEM_WB_ALU_OUTPUT;
                                } else {
                                    InstructionDecode.ID_EX_B = MemoryAccess.MEM_WB_ALU_OUTPUT;
                                }
//                                System.out.println("MEM Forward!");
                            }
                        } else {// it's a store going to memory
                            int index = indexOf(getDependencies(currOpCodeChecked), getToBeUpdated(opCode));
                            if (index == 0) {
                                InstructionDecode.ID_EX_A = MemoryAccess.MEM_WB_ALU_OUTPUT;
                            } else {
                                InstructionDecode.ID_EX_B = MemoryAccess.MEM_WB_ALU_OUTPUT;
                            }
//                            System.out.println("MEM Forward!");
                        }
                        return false;
                    } else if (stage.equals("EX")) {//ALU operation is in EX
                        if (UtilityFunctions.to32BitBinString(currOpCodeChecked).substring(0, 6).equals("111111")) {// check if dependent instruction is store
                            int index = indexOf(getDependencies(currOpCodeChecked), getToBeUpdated(opCode));
                            if (index == 0) { //check if the computation required is only in address (EX)
                                InstructionDecode.ID_EX_A = Execution.EX_MEM_ALU_OUTPUT;
//                                System.out.println("EX Forward!");
                            }
                        } else {//if its not a store, just forward!
                            int index = indexOf(getDependencies(currOpCodeChecked), getToBeUpdated(opCode));
                            if (index == 0) {
                                InstructionDecode.ID_EX_A = Execution.EX_MEM_ALU_OUTPUT;
                            } else {
                                InstructionDecode.ID_EX_B = Execution.EX_MEM_ALU_OUTPUT;
                            }
//                            System.out.println("EX Forward!");
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public void runOneCycle() {

        Writeback.writeback();
        if (!checkAndForward(MemoryAccess.MEM_WB_IR, new int[]{
            Execution.EX_MEM_IR,
            InstructionDecode.ID_EX_IR,
            InstructionFetch.IF_ID_IR
        }, "MEM")) {
            MemoryAccess.memoryAccess();
            if (!checkAndForward(Execution.EX_MEM_IR, new int[]{
                InstructionDecode.ID_EX_IR,
                InstructionFetch.IF_ID_IR
            }, "EX")) {
                Execution.execute();
                InstructionDecode.decode();

                if (InstructionFetch.PC < endPC) {
                    InstructionFetch.fetch();
                }
            }
        }

        InstructionFetch.printContents();
        InstructionDecode.printContents();
        Execution.printContents();
        MemoryAccess.printContents();
        Writeback.printContents();

        System.out.println();
    }

}
