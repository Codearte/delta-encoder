package eu.codearte.encoder;

import java.nio.ByteBuffer;

import static eu.codearte.encoder.Constants.*;

public class BinaryDeltaEncoder {

    public static final int MAX_LENGTH_MASK = ~(1 << (LENGTH_BITS - 1));

    public ByteBuffer buffer;
    public double[] doubles;
    public int[] deltas;
    public int deltaSize, multiplier, idx;

    public void encode(final double[] values, final int[] temp, final int precision, final ByteBuffer buf) {
        if (precision >= 1 << PRECISION_BITS) throw new IllegalArgumentException();
        if ((values.length & MAX_LENGTH_MASK) != values.length) throw new IllegalArgumentException();
        doubles = values; deltas = temp; buffer = buf;
        multiplier =  Utils.pow(10, precision);

        calculateDeltaVector();
        if (deltaSize > DELTA_SIZE_BITS) throw new IllegalArgumentException();
        buffer.putLong((long) precision << (LENGTH_BITS + DELTA_SIZE_BITS) | (long) deltaSize << LENGTH_BITS | values.length);
        buffer.putLong(roundAndPromote(values[0]));
        idx = 1;
        encodeDeltas();
    }

    private void calculateDeltaVector() {
        long maxDelta = 0, currentValue = roundAndPromote(doubles[0]);
        for (int i = 1; i < doubles.length; i++) {
            deltas[i] = (int) (-currentValue + (currentValue = roundAndPromote(doubles[i])));
            if (deltas[i] > maxDelta) maxDelta = deltas[i];
        }
        deltaSize = Long.SIZE - Long.numberOfLeadingZeros(maxDelta);
    }

    private void encodeDeltas() {
        if (idx >= doubles.length) return;
        final int remainingBits = (doubles.length - idx) * deltaSize;

        if (remainingBits >= Long.SIZE || deltaSize > Integer.SIZE) buffer.putLong(encodeBits(Long.SIZE));
        else if (remainingBits >= Integer.SIZE || deltaSize > Short.SIZE) buffer.putInt((int) encodeBits(Integer.SIZE));
        else if (remainingBits >= Short.SIZE || deltaSize > Byte.SIZE) buffer.putShort((short) encodeBits(Short.SIZE));
        else buffer.put((byte) encodeBits(Byte.SIZE));
        encodeDeltas();
    }

    private long encodeBits(final int typeSize) {
        long bits = 0L;
        for (int pos = typeSize - deltaSize; pos >= 0 && idx < deltas.length; pos -= deltaSize)
            bits |= (long) deltas[idx++] << pos;
        return bits;
    }

    private long roundAndPromote(final double value) {
        return (long) (value * multiplier + .5d);
    }
}
