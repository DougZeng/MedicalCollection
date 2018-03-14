package com.gopher.meidcalcollection.common.util.async;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/3/10.
 * 兼容旧版本Android的 {@link Arrays}。
 */

public class ArraysCompat {
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        @SuppressWarnings("unchecked")
        T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[newLength] : (T[]) Array
                .newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
}
