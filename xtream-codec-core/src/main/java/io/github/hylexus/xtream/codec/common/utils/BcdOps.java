package io.github.hylexus.xtream.codec.common.utils;

import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public abstract class BcdOps {

    public static String encodeBcd8421(ByteBuf source, final int length) {
        final StringBuilder builder = new StringBuilder(length << 2);
        for (int i = 0; i < length; i++) {
            builder.append((source.readByte() & 0xf0) >>> 4);
            builder.append(source.readByte() & 0x0f);
        }
        return builder.toString();
    }

    public static String encodeBcd8421(final byte[] source, final int start, final int end) {
        assert start < end : "start < end";

        final int length = end - start;
        final StringBuilder builder = new StringBuilder(length << 2);
        for (int i = start; i < end; i++) {
            builder.append((source[i] & 0xf0) >>> 4);
            builder.append(source[i] & 0x0f);
        }
        return builder.toString();
    }

    public static byte[] decodeBcd8421(final String str) {
        // odd -> 0 + str
        final String source = (str.length() & 1) == 1
                ? "0" + str
                : str;

        final byte[] result = new byte[source.length() >> 1];
        final byte[] sourceBytes = source.getBytes();
        for (int i = 0; i < result.length; i++) {

            byte high = asciiToBcd(sourceBytes[i << 1]);
            byte low = asciiToBcd(sourceBytes[(i << 1) + 1]);

            result[i] = (byte) ((high << 4) | low);
        }
        return result;
    }

    private static byte asciiToBcd(final byte asc) {
        if ((asc >= '0') && (asc <= '9')) {
            return (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            return (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            return (byte) (asc - 'a' + 10);
        } else {
            return (byte) (asc - 48);
        }
    }
}
