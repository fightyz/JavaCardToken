package org.esec.mcg.javacardtoken.APDU;

/**
 * Created by yz on 2016/3/29.
 */
public class Constants {
    public static short SW_BYTES_REMAINING_00 = 0x6100;
    public static short SW_NO_ERROR = (short)0x9000;
    private static short SW_CONDITIONS_NOT_SATISFIED = 0x6985;
    private static short SW_WRONG_DATA = 0x6A80;

    public static short TEST_OF_USER_PRESENCE_REQUIRED = SW_CONDITIONS_NOT_SATISFIED;
    public static short INVALID_KEY_HANDLE = SW_WRONG_DATA;
}
