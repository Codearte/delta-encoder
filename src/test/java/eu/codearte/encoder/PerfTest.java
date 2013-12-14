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

//    private static final int ITERATIONS = 5;
//    private static final int WARMUP = 1;
    private static final int ITERATIONS = 1_000_000;
    private static final int WARMUP = 100_000;

    private static final double[] doubles = prices(10, 1.1235813d, true, 10, 6);
    public static double[] result;

    public static void main(String[] args) {
//        testEncoder(new JavaArrayEncoder());
        testEncoder(new DeltaDoubleArrayEncoder(200, 6));
        testEncoder(new ByteBufferArrayEncoder(200));
        testEncoder(new KryoArrayEncoder());
    }

    private static void testEncoder(DoubleArrayEncoder encoder) {
        System.out.println(encoder.getClass().getSimpleName());
        runTests(encoder, WARMUP);
        System.out.println("------ WARMED UP ------");
        for (int i = 0; i < 5; i++) printResults(encoder, runTests(encoder, ITERATIONS));
        System.out.println("-----------------------");
    }

    private static void printResults(DoubleArrayEncoder encoder, long[] longs) {
        System.out.print("Encode (ns): " + longs[0] / ITERATIONS);
        System.out.println("\t\tDecode (ns): " + longs[1] / ITERATIONS);
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
        encoder.reset();

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            result = encoder.decode();
            encoder.reset();
        }
        times[1] = System.nanoTime() - start;
        return times;
    }

}
