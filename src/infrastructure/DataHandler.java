package infrastructure;

public class DataHandler {
    public static byte[] shortToByteTwiddleMethod(short[] input)
    {
        int short_index, byte_index;
        int iterations = input.length;

        byte[] buffer = new byte[input.length * 2];

        short_index = byte_index = 0;

        while (short_index != iterations)
        {
            buffer[byte_index]     = (byte) (input[short_index] & 0x00FF);
            buffer[byte_index + 1] = (byte) ((input[short_index] & 0xFF00) >> 8);

            ++short_index; byte_index += 2;
        }

        return buffer;
    }
}
