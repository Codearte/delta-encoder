package eu.codearte.encoder;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import static eu.codearte.encoder.Utils.doubles;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 09/11/13
 * Time: 07:51
 * To change this template use File | Settings | File Templates.
 */
public class DeltaEncodingPerformanceTest {

    @Test
    public void testSize() throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(100000);
        final BinaryDeltaEncoder encoder = new BinaryDeltaEncoder();
        for (int i = 10; i <= 320; i *= 2) {
            final double[] doubles = doubles(i, 1.1235813d, true, 10, 6);
            buf.clear();
            final int byteBufSize = serialiseWithByteBuffer(doubles, null, buf);
            buf.clear();
            final int deltaSize = serialiseWithDelta(doubles, null, buf, encoder, new int[i]);
            System.out.println("Array size: " + i + "\t\tbuffer: " + byteBufSize + "\t\tdelta: " + deltaSize);
        }
    }

    private int serialiseWithJava(double[] ask, double[] bid, ByteArrayOutputStream baos, ObjectOutputStream out) throws IOException {
        out.writeObject(ask);
        return baos.size();
    }

    private int serialiseWithKryo(double[] ask, double[] bid, Kryo kryo, Output output) {
        kryo.writeObject(output, ask);
        return output.position();
    }

    private int serialiseWithByteBuffer(double[] ask, double[] bid, ByteBuffer buf) {
        for (int i = 0; i < ask.length; i++) {
            buf.putDouble(ask[i]);
        }
        return buf.position();
    }

    private int serialiseWithDelta(double[] ask, double[] bid, ByteBuffer buffer, BinaryDeltaEncoder encoder, int[] temp) {
        encoder.encode(ask, temp, 6, buffer);
        return encoder.buffer.position();
    }

}
