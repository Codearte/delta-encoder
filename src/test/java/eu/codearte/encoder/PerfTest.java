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

    private static final int ITERATIONS = 5_000_000;
    private static final int WARMUP = 1_000_000;
//    private static final int ITERATIONS = 1;
//    private static final int WARMUP = 1;

    private static final double[] ask = prices(20, 1.1235813d, 10, 6);
    private static final double[] bid = prices(20, 1.1235821d, 10, 6);
    public static double[] result;

    public static void main(String[] args) {
//        printResults("Java", testEncoder(new JavaArrayEncoder()));
        printResults("Kryo", testEncoder(new KryoArrayEncoder()));
//        printResults("Buffer", testEncoder(new ByteBufferArrayEncoder(20)));
        printResults("Delta", testEncoder(new DeltaDoubleArrayEncoder(20, 6)));
    }

    private static void printResults(String encoder, long[] longs) {
        System.out.println(encoder);
        System.out.println("Encode (ns): " + longs[0] / ITERATIONS);
        System.out.println("Decode (ns): " + longs[1] / ITERATIONS);
    }

    private static long[] testEncoder(DoubleArrayEncoder encoder) {
        runTests(encoder, WARMUP);
        System.out.println("------ WARMED UP ------");
        return runTests(encoder, ITERATIONS);
    }

    private static long[] runTests2(DoubleArrayEncoder encoder, int iterations) {
        encoder.encode(ask);
        encoder.reset();
        result = encoder.decode();
        return new long[2];
    }

    private static long[] runTests(DoubleArrayEncoder encoder, int iterations) {
        final long[] times = new long[2];

//        System.out.println(Arrays.toString(ask));


        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            encoder.encode(ask);
            encoder.reset();
        }
        times[0] = System.nanoTime() - start;

        encoder.encode(ask);
        encoder.reset();

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            result = encoder.decode();
            encoder.reset();
        }
        times[1] = System.nanoTime() - start;
//        System.out.println(Arrays.toString(result));
        return times;
    }

}
