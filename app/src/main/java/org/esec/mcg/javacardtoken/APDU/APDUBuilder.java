package org.esec.mcg.javacardtoken.APDU;

/**
 * Created by yz on 2016/3/18.
 */
public class APDUBuilder {
    public static final byte CLA_7816 = (byte) 0x00;

    public static final int OFFSET_CLA = 0;

    public static final int OFFSET_INS = 1;

    public static final int OFFSET_P1 = 2;

    public static final int OFFSET_P2 = 3;

    public static final int OFFSET_LC = 4;

    public static final int OFFSET_CDATA = 5;

    private byte mCLA;

    private byte mINS;

    private byte mP1, mP2;

    private byte mLe;

    private byte mCData[];

    public APDUBuilder() {
        mCLA = 0x00;
        mINS = 0x00;
        mP1 = 0x00;
        mP2 = 0x00;
        mLe = 0x00;
        mCData = null;
    }

    protected void setCLA(byte cla) {
        mCLA = cla;
    }

    protected void setINS(byte ins) {
        mINS = ins;
    }

    protected void setParams(byte p1, byte p2) {
        mP1 = p1;
        mP2 = p2;
    }

    protected void setCData(byte[] data) {
        mCData = data;
    }

    protected void setLe(byte le) {
        mLe = le;
    }

    public byte[] build() {
        int apduLength = 4;

        if (mCData != null) {
            apduLength += mCData.length + 1;
        }

        if (mLe != 0x00) {
            apduLength += 1;
        }

        byte[] apduBytes = new byte[apduLength];
        apduBytes[OFFSET_CLA] = mCLA;
        apduBytes[OFFSET_INS] = mINS;
        apduBytes[OFFSET_P1] = mP1;
        apduBytes[OFFSET_P2] = mP2;

        if (mCData != null) {
            if (mCData.length > 255) {
                // TODO: 2016/3/18 throw exception
            }
            apduBytes[OFFSET_LC] = (byte) mCData.length;
            System.arraycopy(mCData, 0, apduBytes, OFFSET_CDATA, mCData.length);
        }

        if (mLe != 0x00) {
            apduBytes[apduLength - 1] = mLe;
        }
        return apduBytes;
    }
}
