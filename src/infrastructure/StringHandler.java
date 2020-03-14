package infrastructure;

import java.util.ArrayList;

public class StringHandler {
    private String src;
    private int[] b;
    private int bits;
    private static ArrayList<Integer> demodulated;

    public StringHandler(String src) { //Constructor For Getting Binary Sequence
        this.src = src;
        this.bits = 16;                 //16 for UTF-16
        generate();
    }

    public StringHandler(ArrayList<Integer> demodulated) {
        this.demodulated = demodulated;
        this.bits = 16;                 //16 for UTF-16
    }

    public void generate() {
        String temp = toBinary();
        b = new int[temp.length()];
        for (int i = 0; i < temp.length() - 1; i++) {
            b[i] = Integer.parseInt(temp.substring(i, i + 1));
        }
    }

    public String toBinary() {
        String result = "";
        String tmpStr;
        int tmpInt;
        char[] messChar = src.toCharArray();
        for (int i = 0; i < messChar.length; i++) {
            tmpStr = Integer.toBinaryString(messChar[i]);
            tmpInt = tmpStr.length();
            if (tmpInt != bits) {
                tmpInt = bits - tmpInt;
                if (tmpInt == bits) {
                    result += tmpStr;
                } else if (tmpInt > 0) {
                    for (int j = 0; j < tmpInt; j++) {
                        result += "0";
                    }
                    result += tmpStr;
                } else {
                    System.err.println("argument 'bits' is too small");
                }
            } else {
                result += tmpStr;
            }
        }
        return result;
    }

    public String getString() {
        String bin = arToString();
        StringBuilder b = new StringBuilder();
        int len = bin.length();
        int i = 0;
        while (i + 16 <= len) {
            char c = convert(bin.substring(i, i + 16));
            i += 16;
            b.append(c);
        }
        String recovered = b.toString();
        return recovered;
    }

    private char convert(String bs) {
        return (char) Integer.parseInt(bs, 2);
    }

    public String arToString() {
        String r = "";
        for (int i : demodulated) {
            r +=i+"";
        }
        return r;
    }

    public int[] getB() {
        return b;
    }
}