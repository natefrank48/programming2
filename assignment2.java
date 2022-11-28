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

            int byteRead = -1;
            while ((byteRead = inputStream.read()) != -1) {
                String s = "";
                s += String.format("%8s", Integer.toBinaryString(byteRead)).replaceAll(" ", "0");
                for(int i = 0; i < 3; i++){
                    int in = inputStream.read();
                    s += String.format("%8s", Integer.toBinaryString(in)).replaceAll(" ", "0");
                }
                if(hm.containsKey(s.substring(0,6))){
                    System.out.println(hm.get(s.substring(0,6)).name);
                }
                else if(hm.containsKey(s.substring(0,8))){
                    System.out.println(hm.get(s.substring(0,8)).name);
                }
                else if(hm.containsKey(s.substring(0,10))){
                    System.out.println(hm.get(s.substring(0,10)).name);
                }
                else if(hm.containsKey(s.substring(0,11))){
                    System.out.println(hm.get(s.substring(0,11)).name);
                }
            }  
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        
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