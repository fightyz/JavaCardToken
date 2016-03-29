package org.esec.mcg.utils;

public final class ByteUtil {
    /**
     * Convert a byte array to hex string
     * {0x00, 0x01, (byte)0xff}	-> "00 01 ff"
     */
    public static String ByteArrayToHexString(byte[] data) {
        final int len = 3*data.length;
        final StringBuilder stringBuilder = new StringBuilder(len);
        for (byte b : data) {
            stringBuilder.append(Integer.toString((b & 0x000000ff) + 0x100, 16).substring(1));
            stringBuilder.append(" ");
        }
        stringBuilder.delete(len-1, len);
        return stringBuilder.toString();
    }

    public static int CompareTo(byte[] buffer1, int offset1,
                                int length1, byte[] buffer2, int offset2, int length2) {
        if(buffer1 == buffer2 && offset1 == offset2 && length1 == length2)
            return 0;

        int end1 = offset1 + length1;
        int end2 = offset2 + length2;

        for(int i = offset1, j = offset2; i < end1 && j < end2;
            i++, j++)
        {
            int a = (buffer1[i] & 0xff);
            int b = (buffer2[j] & 0xff);
            if(a != b)
                return a - b;
        }
        return length1 - length2;
    }

    public static short Makeshort(byte a, byte b) {
        return (short)(((0xff & a) << 8) | (0xff & b));
    }
}