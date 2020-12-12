package com.security.utils;

public class UntemperingUtils {

    private static final int INTEGER_32 = 32;

    private static int unshiftLeft(int x, int shift, int mask) {
        int res = x;
        for (int i = 0; i < INTEGER_32; i++) {
            res = x ^ (res << shift & mask);
        }
        return res;
    }

    private static int unshiftRight(int x, int shift) {
        int res = x;
        for (int i = 0; i < INTEGER_32; i++) {
            res = x ^ res >>> shift;
        }
        return res;
    }

    //    tempering
    //    y ^=  y >>> 11;
    //    y ^= (y <<   7) & 0x9d2c5680;
    //    y ^= (y <<  15) & 0xefc60000;
    //    y ^=  y >>> 18;
    public static int unTemp(int x) {
        x = unshiftRight(x, 18);
        x = unshiftLeft(x, 15, 0xefc60000);
        x = unshiftLeft(x, 7, 0x9d2c5680);
        x = unshiftRight(x, 11);
        return x;
    }
}
