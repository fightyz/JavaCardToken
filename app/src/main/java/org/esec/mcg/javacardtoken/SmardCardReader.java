package org.esec.mcg.javacardtoken;

import android.nfc.Tag;

import org.esec.mcg.javacardtoken.APDU.APDUError;

/**
 * Created by yz on 2016/3/18.
 */
public interface SmardCardReader {
    public void init(Tag tag);

    public void connect();

    public byte[] transceive(byte[] cmd) throws APDUError;

    public boolean isConnected();

    public void close();
}
