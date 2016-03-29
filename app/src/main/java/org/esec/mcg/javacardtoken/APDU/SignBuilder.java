package org.esec.mcg.javacardtoken.APDU;

/**
 * Created by yz on 2016/3/21.
 */
public class SignBuilder extends APDUBuilder {
    public static final byte INS_VALUE = (byte)0x02;

    public SignBuilder() {
        super();
        setCLA(CLA_7816);
        setINS(INS_VALUE);
        setParams((byte)0x03, (byte)0x00);
        setLe((byte)0xff);
    }

    public SignBuilder setRawMessage(byte[] rawMessage) {
        setCData(rawMessage);
        return this;
    }

    public SignBuilder setControl(byte control) {
        setParams(control, (byte)0x00);
        return this;
    }
}
