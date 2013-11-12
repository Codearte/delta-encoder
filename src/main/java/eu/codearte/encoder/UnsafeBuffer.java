package eu.codearte.encoder;

import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 12/11/13
 * Time: 00:16
 */
public class UnsafeBuffer {

    private static Unsafe _unsafe;

    static {
        try {
            java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            _unsafe = (sun.misc.Unsafe) field.get(null);
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    private long address;
    private int position;

    void setBuffer(final ByteBuffer buffer) {
        if (!buffer.isDirect()) throw new IllegalArgumentException();
        address = ((DirectBuffer) buffer).address();
    }

    public void putLong(final long val) {
        _unsafe.putLong(address + (position += Long.SIZE), val);
    }

    public void putInt(final int val) {
        _unsafe.putInt(address + (position += Integer.SIZE), val);
    }

    public void putShort(final short val) {
        _unsafe.putInt(address + (position += Short.SIZE), val);
    }

    public void put(final byte val) {
        _unsafe.putInt(address + (position += Byte.SIZE), val);
    }

    public int position() {
        return position;
    }

    public void position(final int pos) {
        position = pos;
    }
}
