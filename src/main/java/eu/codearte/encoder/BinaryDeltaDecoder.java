package eu.codearte.encoder;

import java.nio.ByteBuffer;

import static eu.codearte.encoder.Constants.*;

public class BinaryDeltaDecoder {

    private ByteBuffer buffer;
    private double[] doubles;
    private long current;
    private int divisor, deltaSize, length, mask;

    public void decode(final ByteBuffer buf, final double[] doubles) {
        buffer = buf; this.doubles = doubles;
        final int bits = buffer.getInt();
        divisor = Utils.pow(10, bits >>> (LENGTH_BITS + DELTA_SIZE_BITS));
        deltaSize = (bits >>> LENGTH_BITS) & 0x1F;
        length = bits & 0x7FFFFF;
        doubles[0] = decimal(current = buffer.getLong());
        mask = (1 << deltaSize) - 1;
        decodeDeltas(1);
    }

    private void decodeDeltas(final int idx) {
        if (idx == length) return;
        final int remainingBits = (length - idx) * deltaSize;

        if (remainingBits >= Long.SIZE) decodeBits(idx, buffer.getLong(), Long.SIZE);
        else if (remainingBits >= Integer.SIZE) decodeBits(idx, buffer.getInt(), Integer.SIZE);
        else if (remainingBits >= Short.SIZE) decodeBits(idx, buffer.getShort(), Short.SIZE);
        else decodeBits(idx, buffer.get(), Byte.SIZE);
    }

    private void decodeBits(int idx, final long bits, final int typeSize) {
        for (int offset = typeSize - deltaSize; offset >= 0 && idx < length; offset -= deltaSize)
            doubles[idx++] = decimal(current += ((bits >>> offset) & mask));
        decodeDeltas(idx);
    }

    private double decimal(final long value) {
        return (double) value / divisor;
    }

}
