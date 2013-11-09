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
        System.out.println("Java: " + serialiseWithJava(ask, bid));
        System.out.println("Kryo: " + serialiseWithKryo(ask, bid));
        System.out.println("Delta: " + serialiseWithDelta(ask, bid));
    }

    private int serialiseWithJava(double[] ask, double[] bid) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(ask);
        out.writeObject(bid);
        return baos.size();
    }

    private int serialiseWithKryo(double[] ask, double[] bid) {
        Kryo kryo = new Kryo();
        Output output = new UnsafeMemoryOutput(1024);
        kryo.writeObject(output, ask);
        kryo.writeObject(output, bid);
        return output.position();
    }

    private int serialiseWithDelta(double[] ask, double[] bid) {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        final BinaryDeltaEncoder encoder = new BinaryDeltaEncoder();
        final int[] temp = new int[ask.length];
        encoder.encode(ask, temp, 6, buffer);
        encoder.encode(bid, temp, 6, buffer);
        return buffer.position();
    }

}
