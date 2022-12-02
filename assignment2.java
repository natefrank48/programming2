import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

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
            opcodeToInstruction.put("01010100", new Instruction("B.", 'C'));
            opcodeToInstruction.put("100101", new Instruction("BL", 'B'));
            opcodeToInstruction.put("11010110000", new Instruction("BR", 'R'));
            opcodeToInstruction.put("10110101", new Instruction("CBNZ", 'C'));
            opcodeToInstruction.put("10110100", new Instruction("CBZ", 'C'));
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


            HashSet<Integer> linesWithLabelsHashSet = new HashSet<Integer>();
            ArrayList<String> instructionsList = new ArrayList<String>();
            int lineNumber = 0; //keeps track of line number for instructions
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
                    int brAddress = binary2ComplimentToInt(binaryInstruction.substring(6));
                    int labelLineNumber = lineNumber + brAddress;
                    addLabel(linesWithLabelsHashSet, lineNumber + brAddress);
                    instruction = opcodeToInstruction.get(binaryInstruction.substring(0, 6)).name + " label" + (lineNumber + brAddress);
                    
                    instructionsList.add(instruction);
                }
                else if(opcodeToInstruction.containsKey(binaryInstruction.substring(0,8))){
                    instruction = getCBTypeInstruction(opcodeToInstruction.get(binaryInstruction.substring(0,8)).name, binaryInstruction, lineNumber, linesWithLabelsHashSet);
                    instructionsList.add(instruction);
                }
                else if(opcodeToInstruction.containsKey(binaryInstruction.substring(0,10))){
                    instruction = getITypeInstruction(opcodeToInstruction.get(binaryInstruction.substring(0,10)).name, binaryInstruction);

                    instructionsList.add(instruction);
                }
                else if(opcodeToInstruction.containsKey(binaryInstruction.substring(0,11))){
                    String instructionName = opcodeToInstruction.get(binaryInstruction.substring(0,11)).name;
                    char instructionType = opcodeToInstruction.get(binaryInstruction.substring(0,11)).type;
                    
                    if (instructionType == 'D') {
                        instruction = getDTypeInstruction(instructionName, binaryInstruction);
                    } else {
                        instruction = getRTypeInstruction(instructionName, binaryInstruction);
                    }

                    instructionsList.add(instruction);
                }
                lineNumber++;
            }  

            for(int i = 0; i < instructionsList.size(); i++){
                if(linesWithLabelsHashSet.contains(i)){
                    System.out.println("label" + i + ":");
                }
                System.out.println(instructionsList.get(i));
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
            instruction = "PRNT " + numberToRegister(Integer.parseInt(binaryInstruction.substring(27), 2));
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
        int aluImmediate = binary2ComplimentToInt(binaryInstruction.substring(10, 22));
        int rn = Integer.parseInt(binaryInstruction.substring(22, 27), 2);
        int rd = Integer.parseInt(binaryInstruction.substring(27), 2);

        String instruction = instructionName + " " + numberToRegister(rd) + ", " + numberToRegister(rn) + ", #" + aluImmediate;

        return instruction;
    }

    private static String getCBTypeInstruction(String instructionName, String binaryInstruction, int lineNumber, HashSet<Integer> linesWithLabelsHashSet){
        if(instructionName.equals("B.")){
            int condition = Integer.parseInt(binaryInstruction.substring(28), 2);
            switch (condition) {
                case 0: instructionName += "EQ";
                         break;
                case 1: instructionName += "NE";
                         break;
                case 2: instructionName += "HS";
                         break;
                case 3: instructionName += "LO";
                         break;
                case 4: instructionName += "MI";
                         break;
                case 5: instructionName += "PL";
                         break;
                case 6: instructionName += "VS";
                         break;
                case 7: instructionName += "VC";
                         break;
                case 8: instructionName += "HI";
                         break;
                case 9: instructionName += "LS";
                         break;
                case 10: instructionName += "GE";
                         break;
                case 11: instructionName += "LT";
                         break;
                case 12: instructionName += "GT";
                         break;
                case 13: instructionName += "LE";
                         break;
            }
        }
        else if(instructionName.equals("CBZ") || instructionName.equals("CBNZ")){
            instructionName += (" " + numberToRegister(Integer.parseInt(binaryInstruction.substring(27), 2)) + ",");
        }
        int brAddress = binary2ComplimentToInt(binaryInstruction.substring(8, 27));
        addLabel(linesWithLabelsHashSet, (brAddress + lineNumber));
        return instructionName + " label" + (brAddress + lineNumber);
    }

    private static String getDTypeInstruction(String instructionName, String binaryInstruction){
        int dtAddress = binary2ComplimentToInt(binaryInstruction.substring(11, 20));
        int rn = Integer.parseInt(binaryInstruction.substring(22, 27), 2);
        int rt = Integer.parseInt(binaryInstruction.substring(27), 2);

        String instruction = instructionName + " " + numberToRegister(rt) + ", [" + numberToRegister(rn) + ", #" + dtAddress + "]";
        return instruction;
    }

    /**
     * checks to see if hashmap has label at the needed labelLineNumber
     * if it does not add to hashmap
     * return the label string
     * 
     * labelLineNumber = line of branch instruction + offset
     */
    private static void addLabel(HashSet<Integer> linesWithLabelsHashSet, int line){
        linesWithLabelsHashSet.add(line);
    }

    private static int binary2ComplimentToInt(String binaryInt) {
        if (binaryInt.charAt(0) == '1') {
            String invertedInt = invertDigits(binaryInt);
            int decimalValue = Integer.parseInt(invertedInt, 2);
            
            decimalValue = (decimalValue + 1) * -1;
            return decimalValue;
        } 
        else {
            return Integer.parseInt(binaryInt, 2);
        }
    }
    
    private static String invertDigits(String binaryInt) {
        String result = binaryInt;
        result = result.replace("0", " ");
        result = result.replace("1", "0");
        result = result.replace(" ", "1");
        return result;
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