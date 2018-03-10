package com.gopher.meidcalcollection.common.util;

/**
 * Created by Administrator on 2017/12/5.
 */

public interface NetworkRequestImpl {
    /**
     * 检查网络
     *
     * @param ip
     * @param iPort
     * @return 0 成功 -1无网络连接 -2网络异常
     */
    public int checkNet(String ip, String iPort);

    /**
     * 发送数据包
     *
     * @param sendBuffer
     * @return
     */
    public int send(byte[] sendBuffer);

    /**
     * 接收数据包
     */
    public byte[] receive(boolean autoClose);

    /**
     * 手动关闭连接
     */
    public void close();
}
