package org.esec.mcg.javacardtoken.APDU;

/**
 * Created by yz on 2016/3/21.
 */
public class APDUError extends Exception {
    private final int code;

    public APDUError(int code) {
        super(String.format("APDU status: %04x", code));
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
