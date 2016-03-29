package org.esec.mcg.javacardtoken;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.esec.mcg.androidu2fsimulator.token.msg.AuthenticationRequest;
import org.esec.mcg.androidu2fsimulator.token.msg.RegistrationRequest;
import org.esec.mcg.javacardtoken.APDU.APDUError;
import org.esec.mcg.javacardtoken.APDU.Constants;
import org.esec.mcg.utils.ByteUtil;
import org.esec.mcg.utils.logger.LogUtils;

import static java.lang.Thread.sleep;

public class JavaCardTokenActivity extends AppCompatActivity implements ReadCardTask.OnCardReadFinishListener {

    private NfcAdapter mAdapter;
    private PendingIntent mPendingintent;
    private IntentFilter[] mFilters;
    private String[][] mTechList;

    private JavaCardTokenReader mReader;

    private U2FTokenIntentType u2fTokenIntentType;
    private AuthenticationRequest[] signBatch;
    private RegistrationRequest registrationRequest;
    private int signBatchIndex;

    private TextView mJavaCardMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingintent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mFilters = new IntentFilter[] {ndef};
        mTechList = new String[][] { new String[] {IsoDep.class.getName()},
                                     new String[] {NfcA.class.getName()}};
        LogUtils.d("JavaCardTokenActivity");

        Intent intent = getIntent();
        Bundle data;

        if (intent.getBundleExtra(U2FTokenIntentType.U2F_OPERATION_SIGN_BATCH.name()) != null) {
            u2fTokenIntentType = U2FTokenIntentType.U2F_OPERATION_SIGN_BATCH;
            Bundle extras = getIntent().getBundleExtra(U2FTokenIntentType.U2F_OPERATION_SIGN_BATCH.name());
            Parcelable[] allParcelables = extras.getParcelableArray("signBatch");
            if (allParcelables != null) {
                signBatch = new AuthenticationRequest[allParcelables.length];
                for (int i = 0; i < allParcelables.length; i++) {
                    signBatch[i] = (AuthenticationRequest)allParcelables[i];
                    LogUtils.d("signBatch: " + signBatch[i]);
                }
            }
        }
        else if ((data = intent.getBundleExtra(U2FTokenIntentType.U2F_OPERATION_REG.name())) != null) {
            LogUtils.d("this is reg");
            u2fTokenIntentType = U2FTokenIntentType.U2F_OPERATION_REG;
//            rawMessage = data.getByteArray("RawMessage");
            registrationRequest = data.getParcelable("registerRequest");
            Parcelable[] allParcelables = data.getParcelableArray("signBatch");
            if (allParcelables != null) {
                signBatch = new AuthenticationRequest[allParcelables.length];
                for (int i = 0; i < allParcelables.length; i++) {
                    signBatch[i] = (AuthenticationRequest)allParcelables[i];
                }
            }
        }
        else {
            // TODO: 2016/3/28 erroe message layout
            setContentView(R.layout.activity_java_card_token);
            mJavaCardMessage = (TextView)findViewById(R.id.java_card_message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
                    i.putExtra("SW", Constants.TEST_OF_USER_PRESENCE_REQUIRED);
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }, 5000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingintent, mFilters, mTechList);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            mReader = JavaCardTokenReader.getInstance();
            mReader.init(tag);
            mReader.connect();
            startReadTask();
        } else {
            // TODO: 2016/3/22 User presence, lost tag exception.
            LogUtils.d("tag is null");
        }
    }

    private void startReadTask() {
        // TODO: 2016/3/18 post a handler.
        ReadCardTask task = new ReadCardTask(mReader, this, registrationRequest, signBatch, u2fTokenIntentType);
        task.startExecute();
    }

    @Override
    public void onCardReadSuccess(byte[] result) {
//        mJavaCardMessage.setText(ByteUtil.ByteArrayToHexString(result));
        Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
        Bundle data = new Bundle();
        data.putByteArray("RawMessage", result);
        i.putExtras(data);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onCardReadFial(APDUError e) {
        // TODO: 2016/3/21 Handle this
        Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
        i.putExtra("SW", e.getCode());
        LogUtils.d("Status Word: " + e.getMessage());
        setResult(RESULT_OK, i); // Token have already been registered
        finish();
    }

}
