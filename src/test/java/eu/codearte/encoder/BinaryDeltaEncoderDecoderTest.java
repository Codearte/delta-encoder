package eu.codearte.encoder;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static eu.codearte.encoder.Utils.doubles;

/**
 * Created with IntelliJ IDEA.
 * User: Wojciech Kudla
 * Date: 06/11/13
 * Time: 21:40
 */
public class BinaryDeltaEncoderDecoderTest {

    private static final int LENGTH = 20;
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
        final double[] doubles = doubles(LENGTH, 1.1235813d, true, LENGTH, 6);
        final int[] deltas = new int[LENGTH];

        System.out.println("Array: " + Arrays.toString(doubles));
        encoder.encode(doubles, deltas, 6, buffer);

        System.out.println(LENGTH + " doubles took: " + buffer.position());
        System.out.println("Would normally take: " + LENGTH * Double.SIZE);

        buffer.position(0);
        final double[] decodedArray = new double[20];

        decoder.decode(buffer, decodedArray);
        System.out.println("Decoded array: " + Arrays.toString(decodedArray));
    }

}

