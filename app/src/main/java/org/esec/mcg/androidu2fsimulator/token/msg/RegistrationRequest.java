package org.esec.mcg.androidu2fsimulator.token.msg;

import android.os.Parcel;
import android.os.Parcelable;

import org.esec.mcg.utils.ByteUtil;

import java.util.Arrays;

/**
 * Byte array of challengeSha256 and applicationSha256
 * Created by yz on 2016/1/14.
 */
public final class RegistrationRequest extends BaseRequest implements Parcelable {

    public RegistrationRequest(byte[] applicationSha256, byte[] challengeSha256) {
        super(challengeSha256, applicationSha256);
    }

    private RegistrationRequest(Parcel source) {
        challengeSha256 = source.createByteArray();
        applicationSha256 = source.createByteArray();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(applicationSha256);
        result = prime * result + Arrays.hashCode(challengeSha256);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistrationRequest other = (RegistrationRequest) obj;
        if (!Arrays.equals(applicationSha256, other.applicationSha256))
            return false;
        if (!Arrays.equals(challengeSha256, other.challengeSha256))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(challengeSha256);

        dest.writeByteArray(applicationSha256);
    }

    public static final ClassLoaderCreator<RegistrationRequest> CREATOR = new ClassLoaderCreator<RegistrationRequest>() {
        @Override
        public RegistrationRequest createFromParcel(Parcel source, ClassLoader loader) {
            return new RegistrationRequest(source);
        }

        @Override
        public RegistrationRequest createFromParcel(Parcel source) {
            return createFromParcel(source, null);
        }

        @Override
        public RegistrationRequest[] newArray(int size) {
            return new RegistrationRequest[size];
        }
    };

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "applicationSha256=" + ByteUtil.ByteArrayToHexString(applicationSha256) +
                ", challengeSha256=" + ByteUtil.ByteArrayToHexString(challengeSha256) +
                '}';
    }
}
