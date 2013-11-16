package eu.codearte.encoder;

import static eu.codearte.encoder.Utils.prices;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 10/11/13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class PerfTest {

    private static final int ITERATIONS = 5;
    private static final int WARMUP = 1;
//    private static final int ITERATIONS = 5_000_000;
//    private static final int WARMUP = 1_000_000;

    private static final double[] doubles = prices(20, 1.1235813d, 10, 6);
    public static double[] result;

    public static void main(String[] args) {
        testEncoder(new JavaArrayEncoder());
        testEncoder(new KryoArrayEncoder());
        testEncoder(new ByteBufferArrayEncoder(20));
        testEncoder(new DeltaDoubleArrayEncoder(20, 6));
    }

    private static void testEncoder(DoubleArrayEncoder encoder) {
        System.out.println(encoder.getClass().getSimpleName());
        runTests(encoder, WARMUP);
        System.out.println("------ WARMED UP ------");
        for (int i = 0; i < 5; i++) printResults(encoder, runTests(encoder, ITERATIONS));
        System.out.println("-----------------------");
    }

    private static void printResults(DoubleArrayEncoder encoder, long[] longs) {
        System.out.println("Encode (ns): " + longs[0] / ITERATIONS);
        System.out.println("Decode (ns): " + longs[1] / ITERATIONS);
    }

    private static long[] runTests(DoubleArrayEncoder encoder, int iterations) {
        final long[] times = new long[2];

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            encoder.encode(doubles);
            encoder.reset();
        }
        times[0] = System.nanoTime() - start;

        encoder.encode(doubles);

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            result = encoder.decode();
            encoder.reset();
        }
        times[1] = System.nanoTime() - start;
        return times;
    }

}
