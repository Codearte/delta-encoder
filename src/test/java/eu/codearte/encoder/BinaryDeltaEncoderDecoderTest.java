package eu.codearte.encoder;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static eu.codearte.encoder.Utils.prices;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 06/11/13
 * Time: 21:40
 * To change this template use File | Settings | File Templates.
 */
public class BinaryDeltaEncoderDecoderTest {

    private final BinaryDeltaEncoder encoder = new BinaryDeltaEncoder();
    private final BinaryDeltaDecoder decoder = new BinaryDeltaDecoder();

    @Test
    public void shouldEncode() {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
        final double[] doubles = {1.1234514999, 1.123451511, 1.123453, 1.123454, 1.123455, 1.123456, 1.123457, 1.123458, 1.123459, 1.123461, 1.123465};
        final int[] deltas = new int[11];
        encoder.encode(doubles, deltas, 6, buffer);
        buffer.position(0);
        final double[] results = new double[11];
        decoder.decode(buffer, results);
        System.out.println(Arrays.toString(results));
    }

    @Test
    public void compressionTest() {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        final double[] ask = prices(20, 1.1235813d, true, 10, 6);
        final double[] bid = prices(20, 1.1235821d, true, 10, 6);
        final int[] deltas = new int[20];

        System.out.println("ASK: " + Arrays.toString(ask));
        System.out.println("BID: " + Arrays.toString(bid));

        encoder.encode(ask, deltas, 6, buffer);
        encoder.encode(bid, deltas, 6, buffer);

        System.out.println("40 doubles took: " + buffer.position());
        System.out.println("Would normally take: " + 40 * Double.SIZE);

        buffer.position(0);
        final double[] decodedAsk = new double[20];
        final double[] decodedBid = new double[20];

        decoder.decode(buffer, decodedAsk);
        decoder.decode(buffer, decodedBid);

        System.out.println("Decoded ASK: " + Arrays.toString(decodedAsk));
        System.out.println("Decoded BID: " + Arrays.toString(decodedBid));
    }

}

