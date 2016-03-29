package org.esec.mcg.androidu2fsimulator.token.msg;

/**
 * Created by yz on 2016/3/28.
 */
class BaseRequest {
    protected byte[] challengeSha256;
    protected byte[] applicationSha256;

    protected BaseRequest(){}

    protected BaseRequest(byte[] challengeSha256, byte[] applicationSha256) {
        this.challengeSha256 = challengeSha256;
        this.applicationSha256 = applicationSha256;
    }

    /**
     * The challenge parameter is the SHA-256 hash of the Client Data, a
     * stringified JSON datastructure that the FIDO Client prepares. Among other
     * things, the Client Data contains the challenge from the relying party
     * (hence the name of the parameter).
     */
    public byte[] getChallengeSha256() {
        return challengeSha256;
    }

    /**
     * The application parameter is the SHA-256 hash of the application identity
     * of the application requesting the registration
     */
    public byte[] getApplicationSha256() {
        return applicationSha256;
    }
}
