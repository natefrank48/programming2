import java.util.HashMap;

public class assignment2{
    public static void main(String [ ] args)
    {
        HashMap<String, Instruction> hm = new HashMap<String, Instruction>();
        hm.put("10001011000", new Instruction("ADD", 'R'));
        hm.put("1001000100", new Instruction("ADDI", 'I'));
        hm.put("10001010000", new Instruction("AND", 'R'));
        hm.put("1001001000", new Instruction("ANDI", 'I'));
        hm.put("000101", new Instruction("B", 'B'));
        hm.put("01010100", new Instruction("B.cond", 'C'));
        hm.put("100101", new Instruction("BL", 'B'));
        hm.put("11010110000", new Instruction("BR", 'R'));
        hm.put("10110100", new Instruction("CBNZ", 'C'));
        hm.put("10110101", new Instruction("CBZ", 'C'));
        hm.put("11001010000", new Instruction("EOR", 'R'));
        hm.put("1101001000", new Instruction("EORI", 'I'));
        hm.put("11111000010", new Instruction("LDUR", 'D'));
        hm.put("11010011011", new Instruction("LSL", 'R'));
        hm.put("11010011010", new Instruction("LSR", 'R'));

        hm.put("10101010000", new Instruction("ORR", 'R'));
        hm.put("1011001000", new Instruction("ORRI", 'I'));
        hm.put("11111000000", new Instruction("STUR", 'D'));
        hm.put("11001011000", new Instruction("SUB", 'R'));
        hm.put("1101000100", new Instruction("SUBI", 'I'));
        hm.put("1111000100", new Instruction("SUBIS", 'I'));
        hm.put("11101011000", new Instruction("SUBS", 'R'));
        hm.put("10011011000", new Instruction("MUL", 'R'));
        hm.put("11111111101", new Instruction("PRNT", 'R'));
        hm.put("11111111100", new Instruction("PRNL", 'R'));
        hm.put("11111111110", new Instruction("DUMP", 'R'));
        hm.put("11111111111", new Instruction("HALT", 'R'));

        System.out.println(hm.get("10001011000").name);
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