package dev.xpepelok.wishlist.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public final class ArrayUtil {
    public static <T> T[] skip(T[] array, int skip) {
        return Arrays.copyOfRange(array, skip, array.length);
    }
}