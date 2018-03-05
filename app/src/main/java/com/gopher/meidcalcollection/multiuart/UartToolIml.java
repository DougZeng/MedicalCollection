package com.gopher.meidcalcollection.multiuart;


import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface UartToolIml<T> {

    Future<T> openUart(int index, UARTSettingInfo info);

    Future<T> readData(int index, byte[] readDatas);

    Future<T> writeData(int index, byte[] writeDatas);

    void release();
}
