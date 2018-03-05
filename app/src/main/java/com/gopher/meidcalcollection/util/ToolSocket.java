package com.gopher.meidcalcollection.util;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ToolSocket {

    private static final String TAG = ToolSocket.class.getSimpleName();
    private static SocketApi socketApi;
    private static ToolSocket instance;
    private static ExecutorService threadPool;
    private static boolean autoClose;

    private ToolSocket() {
        socketApi = new SocketApi();
        threadPool = Executors.newCachedThreadPool();
    }

    public static synchronized ToolSocket getInstance() {
        if (instance == null) {
            instance = new ToolSocket();
        }
        return instance;
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public byte[] doSocket(final String ip, final String port, final byte[] sends) {
        byte[] bytes = new byte[1024];
        if (!threadPool.isShutdown()) {
            Future<byte[]> future = threadPool.submit(new Callable<byte[]>() {
                @Override
                public byte[] call() throws Exception {
                    int i = socketApi.checkNet(ip, port);
                    if (i == 0) {
                        int send = socketApi.send(sends);
                        if (send == 0) {
                            byte[] receive = socketApi.receive(autoClose);
                            Log.i(TAG, "receive = " + receive);
                            return receive;
                        }
                    }
                    return null;
                }
            });
            try {
                bytes = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
