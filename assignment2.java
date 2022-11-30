import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
//import java.util.Scanner;
import java.util.HashMap;

public class assignment2{
    public static void main(String[] args)
    {
        

        try (
            InputStream inputStream = new FileInputStream(args[0]);
        ) {
            HashMap<String, Instruction> opcodeToInstruction = new HashMap<String, Instruction>();
            opcodeToInstruction.put("10001011000", new Instruction("ADD", 'R'));
            opcodeToInstruction.put("1001000100", new Instruction("ADDI", 'I'));
            opcodeToInstruction.put("10001010000", new Instruction("AND", 'R'));
            opcodeToInstruction.put("1001001000", new Instruction("ANDI", 'I'));
            opcodeToInstruction.put("000101", new Instruction("B", 'B'));
            opcodeToInstruction.put("01010100", new Instruction("B.cond", 'C'));
            opcodeToInstruction.put("100101", new Instruction("BL", 'B'));
            opcodeToInstruction.put("11010110000", new Instruction("BR", 'R'));
            opcodeToInstruction.put("10110100", new Instruction("CBNZ", 'C'));
            opcodeToInstruction.put("10110101", new Instruction("CBZ", 'C'));
            opcodeToInstruction.put("11001010000", new Instruction("EOR", 'R'));
            opcodeToInstruction.put("1101001000", new Instruction("EORI", 'I'));
            opcodeToInstruction.put("11111000010", new Instruction("LDUR", 'D'));
            opcodeToInstruction.put("11010011011", new Instruction("LSL", 'R'));
            opcodeToInstruction.put("11010011010", new Instruction("LSR", 'R'));
            opcodeToInstruction.put("10101010000", new Instruction("ORR", 'R'));
            opcodeToInstruction.put("1011001000", new Instruction("ORRI", 'I'));
            opcodeToInstruction.put("11111000000", new Instruction("STUR", 'D'));
            opcodeToInstruction.put("11001011000", new Instruction("SUB", 'R'));
            opcodeToInstruction.put("1101000100", new Instruction("SUBI", 'I'));
            opcodeToInstruction.put("1111000100", new Instruction("SUBIS", 'I'));
            opcodeToInstruction.put("11101011000", new Instruction("SUBS", 'R'));
            opcodeToInstruction.put("10011011000", new Instruction("MUL", 'R'));
            opcodeToInstruction.put("11111111101", new Instruction("PRNT", 'R'));
            opcodeToInstruction.put("11111111100", new Instruction("PRNL", 'R'));
            opcodeToInstruction.put("11111111110", new Instruction("DUMP", 'R'));
            opcodeToInstruction.put("11111111111", new Instruction("HALT", 'R'));


            HashMap<Integer, String> labels = new HashMap<Integer, String>();
            int lineNumber = 0; //keeps track of line number for instructions
            int labelCount = 0; //keeps track of the number of labels
            String binaryInstruction = "";
            String instruction;

            int byteRead = -1;
            while ((byteRead = inputStream.read()) != -1) {
                binaryInstruction = String.format("%8s", Integer.toBinaryString(byteRead)).replaceAll(" ", "0");
                for(int i = 0; i < 3; i++){
                    int in = inputStream.read();
                    binaryInstruction += String.format("%8s", Integer.toBinaryString(in)).replaceAll(" ", "0");
                }
                if(opcodeToInstruction.containsKey(binaryInstruction.substring(0,6))){
                    System.out.println(opcodeToInstruction.get(binaryInstruction.substring(0,6)).name);
                }
                else if(opcodeToInstruction.containsKey(binaryInstruction.substring(0,8))){
                    System.out.println(opcodeToInstruction.get(binaryInstruction.substring(0,8)).name);
                }
                else if(opcodeToInstruction.containsKey(binaryInstruction.substring(0,10))){
                    instruction = getITypeInstruction(opcodeToInstruction.get(binaryInstruction.substring(0,10)).name, binaryInstruction);

                    System.out.println(instruction);
                }
                else if(opcodeToInstruction.containsKey(binaryInstruction.substring(0,11))){
                    String instructionName = opcodeToInstruction.get(binaryInstruction.substring(0,11)).name;
                    char instructionType = opcodeToInstruction.get(binaryInstruction.substring(0,11)).type;
                    
                    if (instructionType == 'D') {
                        instruction = getDTypeInstruction(instructionName, binaryInstruction);
                    } else {
                        instruction = getRTypeInstruction(instructionName, binaryInstruction);
                    }

                    System.out.println(instruction);
                }
            }  
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        
    }

    private static String getRTypeInstruction(String instructionName, String binaryInstruction){
        int rm = Integer.parseInt(binaryInstruction.substring(11, 16), 2);
        int shamt = Integer.parseInt(binaryInstruction.substring(16, 22), 2);
        int rn = Integer.parseInt(binaryInstruction.substring(22, 27), 2);
        int rd = Integer.parseInt(binaryInstruction.substring(27), 2);
        
        String instruction;
        if (instructionName == "LSL" || instructionName == "LSR") {
            instruction = instructionName + " " + numberToRegister(rd) + ", " + numberToRegister(rn) + ", #" + shamt;
        } else if (instructionName == "BR") {
            instruction = instructionName + " " + numberToRegister(rn);
        } else if (instructionName == "PRNT") {
            instruction = "PRNT";
        } else if (instructionName == "PRNL") {
            instruction = "PRNL";
        } else if (instructionName == "DUMP") {
            instruction = "DUMP";
        } else if (instructionName == "HALT") {
            instruction = "HALT";
        } else {
            instruction = instructionName + " " + numberToRegister(rd) + ", " + numberToRegister(rn) + ", " + numberToRegister(rm);
        }
        
        return instruction;
    }

    private static String getITypeInstruction(String instructionName, String binaryInstruction){
        int aluImmediate = Integer.parseInt(binaryInstruction.substring(10, 22), 2);
        int rn = Integer.parseInt(binaryInstruction.substring(22, 27), 2);
        int rd = Integer.parseInt(binaryInstruction.substring(27), 2);

        String instruction = instructionName + " " + numberToRegister(rd) + ", " + numberToRegister(rn) + ", #" + aluImmediate;

        return instruction;
    }

    private static String getBTypeInstruction(String instructionName, String binaryInstruction, int lineNumber, int labelCount, HashMap labels){
        //todo
        return "";
    }

    private static String getCBTypeInstruction(String instructionName, String binaryInstruction, int lineNumber, int labelCount, HashMap labels){
        //todo
        return "";
    }

    private static String getDTypeInstruction(String instructionName, String binaryInstruction){
        int dtAddress = Integer.parseInt(binaryInstruction.substring(11, 20), 2);
        int rn = Integer.parseInt(binaryInstruction.substring(22, 27), 2);
        int rt = Integer.parseInt(binaryInstruction.substring(27), 2);

        String instruction = instructionName + " " + numberToRegister(rt) + ", [" + numberToRegister(rn) + ", #" + dtAddress + "]";

        return instruction;
    }

    /**
     * checks to see if hashmap has label at the needed lineOfLabel
     * if it does not add to hashmap
     * return the label string
     * 
     * lineOfLabel = line of branch instruction + offset
     */
    private static String checkInstruction(int lineOfLabel, int labelCount, HashMap labels){
        //todo
        return "";
    }

    private static String getRegister(String fiveBitOpcode){
        //todo
        return "";
    }

    private static String numberToRegister(int registerNumber) {
        String registerName;
        switch (registerNumber) {
            case 28: registerName = "SP";
                     break;
            case 29: registerName = "FP";
                     break;
            case 30: registerName = "LR";
                     break;
            case 31: registerName = "XZR";
                     break;
            default: registerName = "X" + registerNumber;
                     break;
        }

        return registerName;
    }
}

class Instruction{
    String name;
    char type;
    public Instruction(String name, char type){
        this.name = name;
        this.type = type;
    }
}