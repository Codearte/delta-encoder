package eu.codearte.encoder;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 10/11/13
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public interface DoubleArrayEncoder {

    int encode(double[] values);
    double[] decode();
    void reset();
}
