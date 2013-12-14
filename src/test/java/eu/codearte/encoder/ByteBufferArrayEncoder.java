package eu.codearte.encoder;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 10/11/13
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class ByteBufferArrayEncoder implements DoubleArrayEncoder {

    private final ByteBuffer buffer = ByteBuffer.allocate(10 * 1024);
    private final double[] temp;

    public ByteBufferArrayEncoder(final int size) {
        temp = new double[size];
    }

    @Override
    public int encode(double[] values) {
        for (int i = 0; i < values.length; i++)
            buffer.putDouble(values[i]);
        return buffer.position();
    }

    @Override
    public double[] decode() {
        for (int i = 0; i < temp.length; i++)
            temp[i] = buffer.getDouble();
        return temp;
    }

    @Override
    public void reset() {
        buffer.position(0);
    }


}
