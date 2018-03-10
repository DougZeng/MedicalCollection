package com.gopher.meidcalcollection.common.util;

/**
 * Created by Administrator on 2017/12/6.
 */

public class DataStruct {
    public static void main(String[] args) {
        //732*16
        int[] ints = {2, 3, 7};

        int num = 16;
        //计算每一位

        for (int i = 0; i < ints.length; i++) {
            System.out.print(ints[i]);
        }

    }

    static int[] demo(int[] ints, int num) {
        int len = ints.length;
        for (int i = 0; i < len; i++) {
            ints[i] *= num;
        }
        //进和留
        for (int i = len - 1; i > 0; i--) {
            ints[i - 1] += ints[i] / 10;
            ints[i] = ints[i] % 10;
        }
        return ints;
    }
}
