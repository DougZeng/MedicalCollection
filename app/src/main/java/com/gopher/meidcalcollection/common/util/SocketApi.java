package com.gopher.meidcalcollection.common.util;

import com.orhanobut.logger.Logger;
import java.io.BufferedWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/12/5.
 */

public class SocketApi implements NetworkRequestImpl {
    private Socket mScoketClient;

    public int checkNet(String serverIp, String iPort) {
        int iRet = 0;
        mScoketClient = null;
        try {
            mScoketClient = new Socket();
            InetSocketAddress isa = new InetSocketAddress(serverIp, Integer.parseInt(iPort));
            mScoketClient.connect(isa, 60000);
        } catch (UnknownHostException e) {
            Logger.e( e.toString());
            iRet = -1;
        } catch (IOException e) {
            Logger.e(e.toString());
            iRet = -2;
        }
        return iRet;

    }

    public int send(byte[] buff) {
        int iRet = 0;
        try {
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(mScoketClient.getOutputStream())), true);

            OutputStream socketWriter = mScoketClient.getOutputStream();
            socketWriter.write(buff);
            socketWriter.flush();
            out.flush();

        } catch (Exception e) {
            Logger.e( e.toString());
            try {
                mScoketClient.close();
            } catch (IOException e1) {
                Logger.e(e1.toString());
            }
            iRet = -1;
        }
        return iRet;
    }

    public byte[] receive(boolean autoClose) {
        String systemE = "exception";
        byte[] exception = systemE.getBytes();
        byte[] RecvBuff = new byte[2048];
        try {
            int time = 60;// Integer.parseInt(MerchantPrefsTool.getString(SharedPrefKeys.CS_WaitTime,
            // "60"));
            mScoketClient.setSoTimeout(time * 1000);

            InputStream input = mScoketClient.getInputStream();
            int Recvlen = input.read(RecvBuff);
            if (Recvlen != -1) {
                byte[] recvBuff = new byte[Recvlen];
                System.arraycopy(RecvBuff, 0, recvBuff, 0, Recvlen);
                return recvBuff;
            } else {
                return exception;
            }

        } catch (IOException e) {
            Logger.e( e.toString());
            return null;
        } finally {
            if (autoClose)
                close();
        }

    }

    public void close() {
        if (mScoketClient != null) {
            try {
                mScoketClient.close();
            } catch (IOException e) {
                Logger.e(e.toString());
            }
            mScoketClient = null;
        }
    }
}
