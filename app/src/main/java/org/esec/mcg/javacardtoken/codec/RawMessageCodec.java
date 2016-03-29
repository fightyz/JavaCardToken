package org.esec.mcg.javacardtoken.codec;

import org.esec.mcg.androidu2fsimulator.token.msg.AuthenticationRequest;
import org.esec.mcg.androidu2fsimulator.token.msg.RegistrationRequest;

import java.nio.ByteBuffer;

/**
 * Created by yz on 2016/3/29.
 */
public class RawMessageCodec {
    public static byte[] encodeRegistrationRequest(RegistrationRequest registrationRequest) {
        byte[] challengeSha256 = registrationRequest.getChallengeSha256();
        byte[] applicationSha256 = registrationRequest.getApplicationSha256();

        byte[] result = new byte[challengeSha256.length + applicationSha256.length];
        ByteBuffer.wrap(result)
                .put(challengeSha256)
                .put(applicationSha256);

        return result;
    }

    public static byte[] encodeAuthenticationRequest(AuthenticationRequest authenticationRequest) {
        byte[] challengeSha256 = authenticationRequest.getChallengeSha256();
        byte[] applicationSha256 = authenticationRequest.getApplicationSha256();
        byte[] keyHandle = authenticationRequest.getKeyHandle();

        byte[] result = new byte[challengeSha256.length + applicationSha256.length + 1 + keyHandle.length];
        ByteBuffer.wrap(result)
                .put(challengeSha256)
                .put(applicationSha256)
                .put((byte)keyHandle.length)
                .put(keyHandle);
        return result;
    }
}
