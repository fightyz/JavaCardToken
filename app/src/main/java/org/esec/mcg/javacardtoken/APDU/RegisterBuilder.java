package org.esec.mcg.javacardtoken.APDU;

/**
 * Created by yz on 2016/3/21.
 */
public class RegisterBuilder extends APDUBuilder {

    public static final byte INS_VALUE = (byte)0x01;

    public RegisterBuilder() {
        super();
        setCLA(CLA_7816);
        setINS(INS_VALUE);
        setParams((byte)0x00, (byte)0x00);
        setLe((byte)0xff);
    }

    public RegisterBuilder setRawMessage(byte[] rawMessage) {
        setCData(rawMessage);
        return this;
    }
}
