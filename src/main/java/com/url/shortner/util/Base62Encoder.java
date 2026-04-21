package com.url.shortner.util;

public final class Base62Encoder {

    private Base62Encoder() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            sb.append(BASE62.charAt((int) (num % 62)));
            num /= 62;
        }

        return sb.reverse().toString();
    }
}