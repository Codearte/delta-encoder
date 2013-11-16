package eu.codearte.encoder;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 10/11/13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class DeltaDoubleArrayEncoder implements DoubleArrayEncoder {

    private final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    private final int[] temp;
    private final double[] doubles;
    private final BinaryDeltaEncoder encoder = new BinaryDeltaEncoder();
    private final BinaryDeltaDecoder decoder = new BinaryDeltaDecoder();
    private final int precision;

    public DeltaDoubleArrayEncoder(final int size, final int precision) {
        this.precision = precision;
        temp = new int[size];
        doubles = new double[size];
    }

    @Override
    public int encode(double[] values) {
        encoder.encode(values, temp, precision, buffer);
        return encoder.buffer.position();
    }

    @Override
    public double[] decode() {
        decoder.decode(this.buffer, doubles);
        return doubles;
    }

    @Override
    public void reset() {
        encoder.buffer.position(0);
        decoder.buffer.position(0);
    }

}
