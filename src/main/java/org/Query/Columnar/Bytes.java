package org.Query.Columnar;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public final class Bytes
{
    public static byte[] convertBitToByteArray(BitSet bitSet) {
        byte[] bytes = new byte[bitSet.size() / 8];

        for (int i = 0; i < bitSet.size(); i++) {
            int index = i / 8;
            int offset = 7 - i % 8;

            bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
        }

        return bytes;
    }

    public static BitSet ConvertByteArrayToBit(byte[] bytes) {
        BitSet bitSet = new BitSet(bytes.length * 8);
        int index = 0;

        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1);
            }
        }

        return bitSet;
    }

    public static byte[] convertToByteArray(String s) {
        byte[] b = new byte[s.length()];

        for (int i = 0; i < s.length(); i++) {
            b[i] = (byte) s.charAt(i);
        }

        return b;
    }

    public static byte[] convertToByteArray(byte n) {
        n = (byte) (n ^ ((byte) 0x80));
        return new byte[]{n};
    }

    public static byte[] convertToByteArray(short n) {
        n = (short) (n ^ ((short) 0x8000));
        byte[] key = new byte[2];

        pack2(key, 0, n);
        return key;
    }

    public static byte[] convertToByteArray(int n) {
        n = (n ^ 0x80000000);
        byte[] key = new byte[4];

        pack4(key, 0, n);
        return key;
    }

    public static byte[] convertToByteArray(long n) {
        n = (n ^ 0x8000000000000000L);
        byte[] key = new byte[8];

        pack8(key, 0, n);
        return key;
    }

    public static String ConvertBytesToString(byte[] buf) {
        return new String(buf, StandardCharsets.UTF_8);
    }

    public static int ConvertBytesToInt(byte[] buf) {
        int value = unpack4(buf, 0);
        value = (value ^ 0x80000000);
        return value;
    }

    public static short ConvertBytesToShort(byte[] buf) {
        short value = unpack2(buf, 0);
        return (short) (value ^ 0x8000);
    }

    public static long ConvertBytesToLong(byte[] buf) {
        long value = ((long) unpack4(buf, 0) << 32) + (unpack4(buf, 4) & 0xFFFFFFFFL);
        value = (value ^ 0x8000000000000000L);
        return value;
    }

    private static final void pack(byte[] data, int offs, int val) {
        data[offs++] = (byte) val;
    }

    private static final void pack2(byte[] data, int offs, int val) {
        data[offs++] = (byte) (val >> 8);
        data[offs++] = (byte) val;
    }

    private static final void pack4(byte[] data, int offs, int val) {
        data[offs++] = (byte) (val >> 24);
        data[offs++] = (byte) (val >> 16);
        data[offs++] = (byte) (val >> 8);
        data[offs++] = (byte) val;
    }

    private static final void pack8(byte[] data, int offs, long val) {
        pack4(data, 0, (int) (val >> 32));
        pack4(data, 4, (int) val);
    }

    private static long unpack8(byte[] buf, int offset) {
        long value =(buf[offset     ] << 32)                |
                    (buf[offset +  1] << 24)                |
                    ((buf[offset + 2] << 16) & 0x00FF0000)  |
                    ((buf[offset + 3] << 8) & 0x0000FF00)   |
                    ((buf[offset + 4] << 0) & 0x000000FF)   ;
        return value;
    }

    private static int unpack4(byte[] buf, int offset) {
        int value = (buf[offset     ] << 24)                |
                    ((buf[offset + 1] << 16) & 0x00FF0000)  |
                    ((buf[offset + 2] << 8) & 0x0000FF00)   |
                    ((buf[offset + 3] << 0) & 0x000000FF)   ;
        return value;
    }

    private static short unpack2(byte[] buf, int offset) {
        short value = (short) (((buf[offset] << 8) & 0x0000FF00)    | 
                              ((buf[offset + 1] << 0) & 0x000000FF));
        return value;
    }

    private static byte unpack(byte[] buf, int offset) {
        return (byte) (buf[offset] & 0x000000FF);
    }
}
