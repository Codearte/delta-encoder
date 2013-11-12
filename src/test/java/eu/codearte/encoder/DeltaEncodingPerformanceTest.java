package eu.codearte.encoder;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeMemoryOutput;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import static eu.codearte.encoder.Utils.prices;

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
        final double[] ask = prices(20, 1.1235813d, 10, 6);
        final double[] bid = prices(20, 1.1235821d, 10, 6);
        System.out.println("Size in bytes:");

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
        System.out.println("Java: " + serialiseWithJava(ask, bid, baos, objectOutputStream));

        final Kryo kryo = new Kryo();
        final UnsafeMemoryOutput output = new UnsafeMemoryOutput(1024);
        System.out.println("Kryo: " + serialiseWithKryo(ask, bid, kryo, output));

        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println("Bytebuffer: " + serialiseWithByteBuffer(ask, bid, byteBuffer));

        final ByteBuffer deltaBuffer = ByteBuffer.allocate(1024);
        final BinaryDeltaEncoder encoder = new BinaryDeltaEncoder();
        final int[] temp = new int[ask.length];
        System.out.println("Delta: " + serialiseWithDelta(ask, bid, deltaBuffer, encoder, temp));
    }

    private int serialiseWithJava(double[] ask, double[] bid, ByteArrayOutputStream baos, ObjectOutputStream out) throws IOException {
        out.writeObject(ask);
        out.writeObject(bid);
        return baos.size();
    }

    private int serialiseWithKryo(double[] ask, double[] bid, Kryo kryo, Output output) {
        kryo.writeObject(output, ask);
        kryo.writeObject(output, bid);
        return output.position();
    }

    private int serialiseWithByteBuffer(double[] ask, double[] bid, ByteBuffer buf) {
        for (int i = 0; i < 20; i++) {
            buf.putDouble(ask[i]);
            buf.putDouble(bid[i]);
        }
        return buf.position();
    }

    private int serialiseWithDelta(double[] ask, double[] bid, ByteBuffer buffer, BinaryDeltaEncoder encoder, int[] temp) {
        encoder.encode(ask, temp, 6, buffer);
        encoder.encode(bid, temp, 6, buffer);
        return buffer.position();
    }

}
