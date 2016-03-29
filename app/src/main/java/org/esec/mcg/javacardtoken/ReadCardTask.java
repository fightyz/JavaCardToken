package org.esec.mcg.javacardtoken;

import android.os.AsyncTask;

import org.esec.mcg.androidu2fsimulator.token.msg.RegistrationRequest;
import org.esec.mcg.javacardtoken.APDU.APDUError;
import org.esec.mcg.javacardtoken.APDU.Constants;
import org.esec.mcg.javacardtoken.APDU.RegisterBuilder;
import org.esec.mcg.javacardtoken.APDU.SelectBuilder;
import org.esec.mcg.javacardtoken.APDU.SignBuilder;
import org.esec.mcg.androidu2fsimulator.token.msg.AuthenticationRequest;
import org.esec.mcg.javacardtoken.codec.RawMessageCodec;
import org.esec.mcg.utils.ByteUtil;
import org.esec.mcg.utils.logger.LogUtils;

/**
 * Created by yz on 2016/3/18.
 */
public class ReadCardTask {
    private JavaCardTokenReader mReader;
    private OnCardReadFinishListener mListener;
    private Enum operation;
    private AuthenticationRequest[] mAuthenticationRequests;
    private RegistrationRequest mRegistrationRequest;

    private static final byte[] AID = {(byte)0xa0, 0x00, 0x00, 0x06, 0x47, 0x2f, 0x00, 0x01};
    private static final byte[] U2F_V2 = {'U', '2', 'F', '_', 'V', '2'};

    public ReadCardTask(JavaCardTokenReader reader,
                        OnCardReadFinishListener listener,
                        RegistrationRequest registrationRequest,
                        AuthenticationRequest[] authenticationRequests,
                        Enum op) {
        mReader = reader;
        mListener = listener;
        mRegistrationRequest = registrationRequest;
        mAuthenticationRequests = authenticationRequests;
        operation = op;
    }

    public void startExecute() {
        SendAPDUsTask task = new SendAPDUsTask();
        task.execute();
    }

    protected class SendAPDUsTask extends AsyncTask<Boolean, Void, byte[]> {
        private byte[] mSelectToken;
        private byte[] mRegisterCmd;
        private byte[] mSigncmd;
        private APDUError e;

        public SendAPDUsTask() {
            mSelectToken = new SelectBuilder().setAID(AID).build();
        }

        @Override
        protected byte[] doInBackground(Boolean... sign) {
            try {
                byte[] response = mReader.transceive(mSelectToken);
                // TODO: 2016/3/22 Check the AID
                LogUtils.d(ByteUtil.ByteArrayToHexString(response));
                if (operation.equals(U2FTokenIntentType.U2F_OPERATION_REG)) {
                    response = doRegister();
                } else if (operation.equals(U2FTokenIntentType.U2F_OPERATION_SIGN)) {
                    response = doSign();
                }

                return response;
            } catch (APDUError apduError) {
                e = apduError;
                return null;
            } finally {
                mReader.close();
            }
        }

        @Override
        protected void onPostExecute(byte[] result) {
            if (e != null) {
                mListener.onCardReadFial(e);
            } else {
                mListener.onCardReadSuccess(result);
            }

            super.onPostExecute(result);
        }

        private byte[] doRegister() throws APDUError {
            if (mAuthenticationRequests != null) {
                checkOnly();
                return register();
            } else {
                return register();
            }
        }

        private void checkOnly() throws APDUError {
            byte[] response;
            for (AuthenticationRequest authenticationRequest :
                    mAuthenticationRequests) {
                mSigncmd = new SignBuilder()
                        .setRawMessage(RawMessageCodec.encodeAuthenticationRequest(authenticationRequest))
                        .setControl(authenticationRequest.getControl())
                        .build();
                response = mReader.transceive(mSigncmd);
                if (ByteUtil.Makeshort(response[0], response[1]) == Constants.TEST_OF_USER_PRESENCE_REQUIRED) {
                    throw new APDUError(Constants.TEST_OF_USER_PRESENCE_REQUIRED);
                }
            }
        }

        private byte[] register() throws APDUError {
            byte[] response;
            mRegisterCmd = new RegisterBuilder().setRawMessage(RawMessageCodec.encodeRegistrationRequest(mRegistrationRequest)).build();
            LogUtils.d(ByteUtil.ByteArrayToHexString(mRegisterCmd));
            response = mReader.transceive(mRegisterCmd);
            LogUtils.d(ByteUtil.ByteArrayToHexString(response));
            return response;
        }

        private byte[] doSign() throws APDUError {
//            SignBuilder sb = new SignBuilder().setRawMessage(mRawMessage);
//            mSigncmd = sb.build();
//            LogUtils.d(ByteUtil.ByteArrayToHexString(mSigncmd));
//            byte[] response = mReader.transceive(mSigncmd);
////            LogUtils.d(ByteUtil.ByteArrayToHexString(response));
//            return response;
            return null;
        }
    }

    public static interface OnCardReadFinishListener {
        void onCardReadSuccess(byte[] result);
        void onCardReadFial(APDUError e);
    }
}
