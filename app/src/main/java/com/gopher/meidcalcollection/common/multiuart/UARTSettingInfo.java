package com.gopher.meidcalcollection.common.multiuart;

import tw.com.prolific.pl2303multilib.PL2303MultiLib;

/**
 * Created by Administrator on 2017/11/27.
 */

public class UARTSettingInfo {
    private int iPortIndex = 0;
    private PL2303MultiLib.BaudRate mBaudrate;
    private PL2303MultiLib.DataBits mDataBits;
    private PL2303MultiLib.Parity mParity;
    private PL2303MultiLib.StopBits mStopBits;
    private PL2303MultiLib.FlowControl mFlowControl;

    public int getiPortIndex() {
        return iPortIndex;
    }

    public void setiPortIndex(int iPortIndex) {
        this.iPortIndex = iPortIndex;
    }

    public PL2303MultiLib.BaudRate getmBaudrate() {
        return PL2303MultiLib.BaudRate.B115200;
    }

    public void setmBaudrate(PL2303MultiLib.BaudRate mBaudrate) {
        this.mBaudrate = mBaudrate;
    }

    public PL2303MultiLib.DataBits getmDataBits() {
        return PL2303MultiLib.DataBits.D8;
    }

    public void setmDataBits(PL2303MultiLib.DataBits mDataBits) {
        this.mDataBits = mDataBits;
    }

    public PL2303MultiLib.Parity getmParity() {
        return PL2303MultiLib.Parity.NONE;
    }

    public void setmParity(PL2303MultiLib.Parity mParity) {
        this.mParity = mParity;
    }

    public PL2303MultiLib.StopBits getmStopBits() {
        return PL2303MultiLib.StopBits.S1;
    }

    public void setmStopBits(PL2303MultiLib.StopBits mStopBits) {
        this.mStopBits = mStopBits;
    }

    public PL2303MultiLib.FlowControl getmFlowControl() {
        return PL2303MultiLib.FlowControl.OFF;
    }

    public void setmFlowControl(PL2303MultiLib.FlowControl mFlowControl) {
        this.mFlowControl = mFlowControl;
    }
}
