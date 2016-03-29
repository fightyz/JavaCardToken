package org.esec.mcg.androidu2fsimulator.token.msg;

import android.os.Parcel;
import android.os.Parcelable;

import org.esec.mcg.utils.ByteUtil;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by yz on 2016/3/7.
 */
public final class AuthenticationRequest extends BaseRequest implements Parcelable {
    public static final byte CHECK_ONLY = 0x07;
    public static final byte USER_PRESENCE_SIGN = 0x03;

    private byte control;
    private byte[] keyHandle;

    public AuthenticationRequest(byte control, byte[] challengeSha256, byte[] applicationSha256,
                                 byte[] keyHandle) {
        this.control = control;
        super.challengeSha256 = challengeSha256;
        super.applicationSha256 = applicationSha256;
        this.keyHandle = keyHandle;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                ", control=" + control +
                "applicationSha256=" + ByteUtil.ByteArrayToHexString(applicationSha256) +
                ", challengeSha256=" + ByteUtil.ByteArrayToHexString(challengeSha256) +
                ", keyHandle=" + ByteUtil.ByteArrayToHexString(keyHandle) +
                '}';
    }

    private AuthenticationRequest(Parcel source) {
        control = source.readByte();
        challengeSha256 = source.createByteArray();
        applicationSha256 = source.createByteArray();
        keyHandle = source.createByteArray();
    }

    /** The FIDO Client will set the control byte to one of the following values:
     * 0x07 ("check-only")
     * 0x03 ("enforce-user-presence-and-sign")
     */
    public byte getControl() {
        return control;
    }

    /** The key handle obtained during registration. */
    public byte[] getKeyHandle() {
        return keyHandle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(control, challengeSha256, applicationSha256, keyHandle);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AuthenticationRequest other = (AuthenticationRequest) obj;
        return Objects.equals(control, other.control)
                && Arrays.equals(challengeSha256, other.challengeSha256)
                && Arrays.equals(applicationSha256, other.applicationSha256)
                && Arrays.equals(keyHandle, other.keyHandle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(control);
        dest.writeByteArray(challengeSha256);
        dest.writeByteArray(applicationSha256);
        dest.writeByteArray(keyHandle);
    }

    public static final ClassLoaderCreator<AuthenticationRequest> CREATOR = new ClassLoaderCreator<AuthenticationRequest>() {
        @Override
        public AuthenticationRequest createFromParcel(Parcel source, ClassLoader loader) {
            return new AuthenticationRequest(source);
        }
        @Override
        public AuthenticationRequest createFromParcel(Parcel source) {
            return new AuthenticationRequest(source);
        }

        @Override
        public AuthenticationRequest[] newArray(int size) {
            return new AuthenticationRequest[size];
        }

    };
}
