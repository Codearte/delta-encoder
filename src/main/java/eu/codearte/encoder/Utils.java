package eu.codearte.encoder;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 09/11/13
 * Time: 07:54
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    private static final Random RANDOM = new Random();


    public static double[] prices(final int length, final double basePrice, final int maxDifferenceInPips, final int precision) {
        final double[] result = new double[length];
        final double divisor = Math.pow(10, precision);
        result[0] = basePrice;
        for (int i = 1; i < length; i++)
            result[i] = result[i - 1] + (double) RANDOM.nextInt(maxDifferenceInPips) / divisor;
        return result;
    }

    public static int pow(int base, int exp) {
        int result = 1;
        while (exp > 0) {
            if ((exp & 1) != 0) result *= base;
            exp >>= 1;
            base *= base;
        }
        return result;
    }
}
