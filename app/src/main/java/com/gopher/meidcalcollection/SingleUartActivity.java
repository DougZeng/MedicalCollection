package com.gopher.meidcalcollection;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gopher.meidcalcollection.common.base.BaseActivity;
import com.gopher.meidcalcollection.common.uart.UartConsts;
import com.gopher.meidcalcollection.common.util.ToolAlert;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Random;

import tw.com.prolific.driver.pl2303.PL2303Driver;

/**
 * Created by Administrator on 2017/12/4.
 */

public class SingleUartActivity extends BaseActivity {


    private static final int DISP_CHAR = 0;
    private static final int LINEFEED_CODE_CRLF = 1;

    private static final int LINEFEED_CODE_LF = 2;


    PL2303Driver mSerial;

    private Button btWrite;
    private EditText etWrite;

    private Button btRead;
    private EditText etRead;

    private Button btLoopBack;
    private ProgressBar pbLoopBack;
    private TextView tvLoopBack;

    private Button btGetSN;
    private TextView tvShowSN;

    private Button mButton01;

    private int mDisplayType = DISP_CHAR;
    private int mReadLinefeedCode = LINEFEED_CODE_LF;
    private int mWriteLinefeedCode = LINEFEED_CODE_LF;

    //BaudRate.B4800, DataBits.D8, StopBits.S1, Parity.NONE, FlowControl.RTSCTS
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;
    private PL2303Driver.DataBits mDataBits = PL2303Driver.DataBits.D8;
    private PL2303Driver.Parity mParity = PL2303Driver.Parity.NONE;
    private PL2303Driver.StopBits mStopBits = PL2303Driver.StopBits.S1;
    private PL2303Driver.FlowControl mFlowControl = PL2303Driver.FlowControl.OFF;


    private static final String NULL = null;


    public Spinner PL2303HXD_BaudRate_spinner;
    public int PL2303HXD_BaudRate;
    public String PL2303HXD_BaudRate_str = "B4800";

    private String strStr;

    @Override
    public int bindLayout() {
        return R.layout.activity_singleuart;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView(View view) {
        PL2303HXD_BaudRate_spinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.BaudRate_Var, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        PL2303HXD_BaudRate_spinner.setAdapter(adapter);
        PL2303HXD_BaudRate_spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        Button mButton01 = (Button) findViewById(R.id.button1);
        mButton01.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                openUsbSerial();

            }
        });

        btWrite = (Button) findViewById(R.id.button2);
        btWrite.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                etWrite = (EditText) findViewById(R.id.editText1);
                writeDataToSerial();
            }
        });

        btRead = (Button) findViewById(R.id.button3);
        btRead.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                etRead = (EditText) findViewById(R.id.editText2);
                readDataFromSerial();
            }
        });

        btLoopBack = (Button) findViewById(R.id.button4);
        btLoopBack.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                pbLoopBack = (ProgressBar) findViewById(R.id.ProgressBar1);
                setProgressBarVisibility(true);
                pbLoopBack.setIndeterminate(false);
                pbLoopBack.setVisibility(View.VISIBLE);
                pbLoopBack.setProgress(0);
                tvLoopBack = (TextView) findViewById(R.id.textView2);
                new Thread(tLoop).start();
            }
        });


        btGetSN = (Button) findViewById(R.id.btn_GetSN);
        tvShowSN = (TextView) findViewById(R.id.text_ShowSN);
        tvShowSN.setText("");
        btGetSN.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                ShowPL2303HXD_SerialNmber();

            }
        });
        mSerial = new PL2303Driver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, UartConsts.ACTION_USB_PERMISSION);

        if (!mSerial.PL2303USBFeatureSupported()) {

            Toast.makeText(this, "No Support USB host API", Toast.LENGTH_SHORT)
                    .show();

            Logger.d("No Support USB host API");

            mSerial = null;

        }

        if (!mSerial.enumerate()) {
            Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
        }

        try {
            Thread.sleep(1500);
            openUsbSerial();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.d("Leave onCreate");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {
        super.onResume();
        String action = getIntent().getAction();
        Logger.d("onResume:" + action);

        //if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
        if (!mSerial.isConnected()) {
            Logger.d("New instance : " + mSerial);

            if (!mSerial.enumerate()) {

                Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Logger.d("onResume:enumerate succeeded!");
            }
            try {
                Thread.sleep(1500);
                openUsbSerial();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.d("Leave onResume");
    }

    @Override
    public void destroy() {
        if (mSerial != null) {
            mSerial.end();
            mSerial = null;
        }

        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {

        Logger.d("Enter onConfigurationChanged");

        super.onConfigurationChanged(newConfig);

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
            Logger.d("keyboard visible");
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
            Logger.d("keyboard hidden");
        }

        if (newConfig.orientation == ActivityInfo.CONFIG_ORIENTATION) {
            Logger.d("CONFIG_ORIENTATION");
        }

        if (newConfig.keyboard == ActivityInfo.CONFIG_KEYBOARD) {
            Logger.d("CONFIG_KEYBOARD");
        }

        if (newConfig.keyboardHidden == ActivityInfo.CONFIG_KEYBOARD_HIDDEN) {
            Logger.d("CONFIG_KEYBOARD_HIDDEN");
        }


        Logger.d("Exit onConfigurationChanged");

    }

    private void openUsbSerial() {
        Logger.d("Enter  openUsbSerial");

        if (mSerial == null) {

            Logger.d("No mSerial");
            return;

        }


        if (mSerial.isConnected()) {
            Logger.d("openUsbSerial : isConnected ");
            String str = PL2303HXD_BaudRate_spinner.getSelectedItem().toString();
            int baudRate = Integer.parseInt(str);
            switch (baudRate) {
                case 9600:
                    mBaudrate = PL2303Driver.BaudRate.B9600;
                    break;
                case 19200:
                    mBaudrate = PL2303Driver.BaudRate.B19200;
                    break;
                case 115200:
                    mBaudrate = PL2303Driver.BaudRate.B115200;
                    break;
                default:
                    mBaudrate = PL2303Driver.BaudRate.B9600;
                    break;
            }
            Logger.d("baudRate: %d", baudRate);
            if (!mSerial.InitByBaudRate(mBaudrate, 700)) {
                if (!mSerial.PL2303Device_IsHasPermission()) {
                    ToolAlert.toastShort(this, "cannot open, maybe no permission");
                }

                if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    ToolAlert.toastShort(this, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                    Logger.d("cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                }
            } else {

                ToolAlert.toastShort(this, "connected : OK");
                Logger.d("connected : OK");
                Logger.d("Exit  openUsbSerial");


            }
        } else {
            ToolAlert.toastShort(this, "Connected failed, Please plug in PL2303 cable again!");
            Logger.d("connected failed, Please plug in PL2303 cable again!");


        }
    }


    private void readDataFromSerial() {

        int len;
        byte[] rbuf = new byte[20];
        StringBuffer sbHex = new StringBuffer();

        Logger.d("Enter readDataFromSerial");

        if (null == mSerial)
            return;

        if (!mSerial.isConnected())
            return;

        len = mSerial.read(rbuf);
        if (len < 0) {
            Logger.d("Fail to bulkTransfer(read data)");
            return;
        }

        if (len > 0) {
            Logger.d("read len : %d", len);
            for (int j = 0; j < len; j++) {
                sbHex.append((char) (rbuf[j] & 0x000000FF));
            }
            etRead.setText(sbHex.toString());
            Toast.makeText(this, "len=" + len, Toast.LENGTH_SHORT).show();
        } else {
            Logger.d("read len : 0 ");
            etRead.setText("empty");
            return;
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Logger.d("Leave readDataFromSerial");
    }

    private void writeDataToSerial() {

        Logger.d("Enter writeDataToSerial");

        if (null == mSerial)
            return;

        if (!mSerial.isConnected())
            return;

        String strWrite = etWrite.getText().toString();
        Logger.d("PL2303Driver Write 2( %d) : %s", strWrite.length(), strWrite);
        int res = mSerial.write(strWrite.getBytes(), strWrite.length());
        if (res < 0) {
            Logger.d("setup2: fail to controlTransfer: %d", res);
            return;
        }

        Logger.d("Write length: %d  %s", strWrite.length(), strWrite);

        Logger.d("Leave writeDataToSerial");
    }


    private void ShowPL2303HXD_SerialNmber() {


        Logger.d("Enter ShowPL2303HXD_SerialNmber");

        if (null == mSerial)
            return;

        if (!mSerial.isConnected())
            return;

        if (mSerial.PL2303Device_GetSerialNumber() != NULL) {
            tvShowSN.setText(mSerial.PL2303Device_GetSerialNumber());

        } else {
            tvShowSN.setText("No SN");

        }

        Logger.d("Leave ShowPL2303HXD_SerialNmber");
    }


    private static final int START_NOTIFIER = 0x100;
    private static final int STOP_NOTIFIER = 0x101;
    private static final int PROG_NOTIFIER_SMALL = 0x102;
    private static final int PROG_NOTIFIER_LARGE = 0x103;
    private static final int ERROR_BAUDRATE_SETUP = 0x8000;
    private static final int ERROR_WRITE_DATA = 0x8001;
    private static final int ERROR_WRITE_LEN = 0x8002;
    private static final int ERROR_READ_DATA = 0x8003;
    private static final int ERROR_READ_LEN = 0x8004;
    private static final int ERROR_COMPARE_DATA = 0x8005;

    Handler myMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_NOTIFIER:
                    pbLoopBack.setProgress(0);
                    tvLoopBack.setText("LoopBack Test start...");
                    btWrite.setEnabled(false);
                    btRead.setEnabled(false);
                    break;
                case STOP_NOTIFIER:
                    pbLoopBack.setProgress(pbLoopBack.getMax());
                    tvLoopBack.setText("LoopBack Test successfully!");
                    btWrite.setEnabled(true);
                    btRead.setEnabled(true);
                    break;
                case PROG_NOTIFIER_SMALL:
                    pbLoopBack.incrementProgressBy(5);
                    break;
                case PROG_NOTIFIER_LARGE:
                    pbLoopBack.incrementProgressBy(10);
                    break;
                case ERROR_BAUDRATE_SETUP:
                    tvLoopBack.setText("Fail to setup:baudrate " + msg.arg1);
                    break;
                case ERROR_WRITE_DATA:
                    tvLoopBack.setText("Fail to write:" + msg.arg1);
                    break;
                case ERROR_WRITE_LEN:
                    tvLoopBack.setText("Fail to write len:" + msg.arg2 + ";" + msg.arg1);
                    break;
                case ERROR_READ_DATA:
                    tvLoopBack.setText("Fail to read:" + msg.arg1);
                    break;
                case ERROR_READ_LEN:
                    tvLoopBack.setText("Length(" + msg.arg2 + ") is wrong! " + msg.arg1);
                    break;
                case ERROR_COMPARE_DATA:
                    tvLoopBack.setText("wrong:" +
                            String.format("rbuf=%02X,byteArray1=%02X", msg.arg1, msg.arg2));
                    break;

            }
            super.handleMessage(msg);
        }//handleMessage
    };

    private void Send_Notifier_Message(int mmsg) {
        Message m = new Message();
        m.what = mmsg;
        myMessageHandler.sendMessage(m);
        Logger.d("Msg index: %04x", mmsg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void Send_ERROR_Message(int mmsg, int value1, int value2) {
        Message m = new Message();
        m.what = mmsg;
        m.arg1 = value1;
        m.arg2 = value2;
        myMessageHandler.sendMessage(m);
        Logger.d("Msg index: %04x", mmsg);
    }

    private Runnable tLoop = new Runnable() {
        public void run() {

            int res = 0, len, i;
            Time t = new Time();
            byte[] rbuf = new byte[4096];
            final int mBRateValue[] = {9600, 19200, 115200};
            PL2303Driver.BaudRate mBRate[] = {PL2303Driver.BaudRate.B9600, PL2303Driver.BaudRate.B19200, PL2303Driver.BaudRate.B115200};

            if (null == mSerial) {
                Logger.d("mSerial is Null");
                return;
            }
            if (!mSerial.isConnected()) {
                Logger.d("mSerial <<-->>disconnect");
                return;
            }

            t.setToNow();
            Random mRandom = new Random(t.toMillis(false));

            byte[] byteArray1 = new byte[256]; //test pattern-1
            mRandom.nextBytes(byteArray1);//fill buf with random bytes
            Send_Notifier_Message(START_NOTIFIER);

            for (int WhichBR = 0; WhichBR < mBRate.length; WhichBR++) {

                try {
                    if (null == mSerial) {
                        Logger.d("mSerial is Null");
                        return;
                    }
                    if (!mSerial.isConnected()) {
                        Logger.d("mSerial <<-->>disconnect");
                        return;
                    }

                    res = mSerial.setup(mBRate[WhichBR], mDataBits, mStopBits, mParity, mFlowControl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (res < 0) {
                    Send_Notifier_Message(START_NOTIFIER);
                    Send_ERROR_Message(ERROR_BAUDRATE_SETUP, mBRateValue[WhichBR], 0);
                    Logger.d("Fail to setup= %d", res);
                    return;
                }
                Send_Notifier_Message(PROG_NOTIFIER_LARGE);

                for (int times = 0; times < 2; times++) {

                    if (null == mSerial)
                        return;

                    if (!mSerial.isConnected())
                        return;

                    len = mSerial.write(byteArray1, byteArray1.length);
                    if (len < 0) {
                        Send_ERROR_Message(ERROR_WRITE_DATA, mBRateValue[WhichBR], 0);
                        Logger.d("Fail to write= %d", len);
                        return;
                    }

                    if (len != byteArray1.length) {
                        Send_ERROR_Message(ERROR_WRITE_LEN, mBRateValue[WhichBR], len);
                        return;
                    }
                    Send_Notifier_Message(PROG_NOTIFIER_SMALL);

                    if (null == mSerial)
                        return;

                    if (!mSerial.isConnected())
                        return;

                    len = mSerial.read(rbuf);
                    if (len < 0) {
                        Send_ERROR_Message(ERROR_READ_DATA, mBRateValue[WhichBR], 0);
                        return;
                    }
                    Logger.d("read length= %d ;byteArray1 length=", len, byteArray1.length);

                    if (len != byteArray1.length) {
                        Send_ERROR_Message(ERROR_READ_LEN, mBRateValue[WhichBR], len);
                        return;
                    }
                    Send_Notifier_Message(PROG_NOTIFIER_SMALL);

                    for (i = 0; i < len; i++) {
                        if (rbuf[i] != byteArray1[i]) {
                            Send_ERROR_Message(ERROR_COMPARE_DATA, rbuf[i], byteArray1[i]);
                            Logger.d("Data is wrong at rbuf[%d]=%02X,byteArray1[%d]=%02X", i, rbuf[i], i, byteArray1[i]);
                            return;
                        }
                    }
                    Send_Notifier_Message(PROG_NOTIFIER_LARGE);

                }

            }

            try {
                res = mSerial.setup(mBaudrate, mDataBits, mStopBits, mParity, mFlowControl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (res < 0) {
                Send_ERROR_Message(ERROR_BAUDRATE_SETUP, 0, 0);
                return;
            }
            Send_Notifier_Message(STOP_NOTIFIER);

        }
    };


    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (null == mSerial)
                return;

            if (!mSerial.isConnected())
                return;

            int baudRate = 0;
            String newBaudRate;
            Toast.makeText(parent.getContext(), "newBaudRate is-" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            newBaudRate = parent.getItemAtPosition(position).toString();

            try {
                baudRate = Integer.parseInt(newBaudRate);
            } catch (NumberFormatException e) {
                System.out.println(" parse int error!!  " + e);
            }

            switch (baudRate) {
                case 9600:
                    mBaudrate = PL2303Driver.BaudRate.B9600;
                    break;
                case 19200:
                    mBaudrate = PL2303Driver.BaudRate.B19200;
                    break;
                case 115200:
                    mBaudrate = PL2303Driver.BaudRate.B115200;
                    break;
                default:
                    mBaudrate = PL2303Driver.BaudRate.B9600;
                    break;
            }

            int res = 0;
            try {
                res = mSerial.setup(mBaudrate, mDataBits, mStopBits, mParity, mFlowControl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (res < 0) {
                Logger.d("fail to setup");
                return;
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
