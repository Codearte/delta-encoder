package eu.codearte.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: qdlt
 * Date: 10/11/13
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class JavaArrayEncoder implements DoubleArrayEncoder {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    private final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public JavaArrayEncoder() {
        try {
            output = new ObjectOutputStream(baos);
            input = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int encode(double[] values) {
        try {
            output.writeObject(values);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return baos.size();
    }

    @Override
    public double[] decode() {
        try {
            return (double[]) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    @Override
    public void reset() {
        baos.reset();
    }

}
