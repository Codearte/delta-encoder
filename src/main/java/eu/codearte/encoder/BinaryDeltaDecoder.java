package eu.codearte.encoder;

import java.nio.ByteBuffer;

import static eu.codearte.encoder.Constants.DELTA_SIZE_BITS;
import static eu.codearte.encoder.Constants.LENGTH_BITS;

public class BinaryDeltaDecoder {

    private ByteBuffer buffer;
    private double[] doubles;
    private long current;
    private int multiplier, deltaSize, length, mask;

    public void decode(final ByteBuffer buffer, final double[] doubles) {
        this.buffer = buffer; this.doubles = doubles;
        final long bits = this.buffer.getLong();
        // division by reciprocal approximation
        multiplier = Utils.pow(10, -(bits >>> (LENGTH_BITS + DELTA_SIZE_BITS))); // ~20ns
        deltaSize = (int) (bits >>> LENGTH_BITS) & 0x3FFFFFF;
        length = (int) (bits & 0xFFFFFFFF);
        doubles[0] = decimal(current = this.buffer.getLong());
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
        return multiplier * value;
    }

    public static void main(String[] args) {
        System.out.println();
    }

}
