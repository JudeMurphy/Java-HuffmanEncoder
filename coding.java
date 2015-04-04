import java.util.BitSet;
import java.io.*;
import java.lang.Number;
import java.lang.Math;
import java.math.BigInteger;

class coding
{
    public static void encode(String code, String filename) throws IOException
    {
        FileOutputStream out = new FileOutputStream(filename);
        int nbytes = (int)Math.ceil(code.length()/8.0);
        int last = code.length()%8;
        //System.out.println(last);
        out.write(last);
        int start = 0;
        int stop = start + 8;
        for (int i=0; i<nbytes; i++)
        {
            String s = code.substring(start, stop);
            start += 8;
            if (i == nbytes-2)
                stop = code.length();
            else
                stop +=8;
            out.write(Integer.parseInt(s, 2));
        }
                
        out.close();
    }

    public static String decode(String filename) throws IOException
    {
        String S = "";
        int input=0, input_next=0;
        FileInputStream in = new FileInputStream(filename);
        boolean done = false;
        int last = in.read();
        input=in.read();
        while (input != -1)
        {
            input_next = in.read();
            String newS = Integer.toBinaryString(input);
            if (input_next == -1 && last!=0)
            {
                int newS_length = last - newS.length();
                if (newS_length<8)
                    for(int i=0; i<newS_length; i++)
                        newS = "0" + newS;
            }
            else
            {
                int newS_length = newS.length();
                if (newS_length<8)
                    for(int i=0; i<(8-newS_length); i++)
                        newS = "0" + newS;
            }
            S += newS;
            input = input_next;
        }
        in.close();
               
        return S;
    }
    /*public static void main(String args[]) throws IOException
    {
        String S1 = "110100010000100011001010011001110010";
        coding C = new coding();
        C.encode(S1, "string.txt");
        String S2 = C.decode("string.txt");
        System.out.println("S1: " + S1);
        System.out.println("S2: " + S2);
    }*/
}
