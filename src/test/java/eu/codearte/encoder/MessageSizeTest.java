package eu.codearte.encoder;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static eu.codearte.encoder.Utils.doubles;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 23/11/13
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class MessageSizeTest {

    public static void main(String[] args) {
        final int[] temp = new int[100];
        final double[] decoded = new double[100];
        final BinaryDeltaEncoder encoder = new BinaryDeltaEncoder();
        final BinaryDeltaDecoder decoder = new BinaryDeltaDecoder();
        final ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 1024);
        for (int i = 15; i < 16; i++) {
            buf.clear();
            double[] prices = doubles(100, 10.0d, true, 1 << i, 6);
            encoder.encode(prices, temp, 6, buf);
            buf.position(0);
            decoder.decode(buf, decoded);
            System.out.println("Delta bits: " + i + "\nDelta size: " + (1 << i) + "\nBuffer size: " + buf.position());
            System.out.println("Original: " + Arrays.toString(prices));
            System.out.println("Decoded: " + Arrays.toString(decoded));
        }
    }

}
