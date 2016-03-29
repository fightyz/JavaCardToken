package org.esec.mcg.javacardtoken.APDU;

/**
 * Created by yz on 2016/3/18.
 */
public class SelectBuilder extends APDUBuilder {
    public static final byte INS_VALE = (byte) 0xA4;

    public SelectBuilder() {
        super();
        setCLA(CLA_7816);
        setINS(INS_VALE);
        setParams((byte) 0x04, (byte) 0x00);
    }

    public SelectBuilder setAID(byte[] aid) {
        setCData(aid);
        return this;
    }
}
