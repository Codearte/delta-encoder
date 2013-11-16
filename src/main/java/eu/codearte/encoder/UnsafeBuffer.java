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
        position = buffer.position();
    }

    private long addr(final int offset) {
        final long pos = address + position;
        position += offset;
        return pos;
    }

    public long getLong() {
        return _unsafe.getLong(addr(8));
    }

    public int getInt() {
        return _unsafe.getInt(addr(4));
    }

    public short getShort() {
        return _unsafe.getShort(addr(2));
    }

    public byte get() {
        return _unsafe.getByte(address + (position++));
    }

    public void putLong(final long val) {
        _unsafe.putLong(addr(8), val);
    }

    public void putInt(final int val) {
        _unsafe.putInt(addr(4), val);
    }

    public void putShort(final short val) {
        _unsafe.putInt(addr(2), val);
    }
    public void put(final byte val) {
        _unsafe.putInt(address + (position++), val);
    }

    public int position() {
        return position;
    }

    public void position(final int pos) {
        position = pos;
    }
}
