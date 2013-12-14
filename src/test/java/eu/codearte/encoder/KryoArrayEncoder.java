package eu.codearte.encoder;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers;

import java.nio.ByteBuffer;

import static com.esotericsoftware.kryo.Kryo.NULL;

/**
 * User: qdlt
 * Date: 10/11/13
 * Time: 10:55
 */
public class KryoArrayEncoder implements DoubleArrayEncoder {

    private final Kryo kryo = new Kryo();
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(10*1024);
    private final ByteBufferOutput output = new ByteBufferOutput();
    private final ByteBufferInput input = new ByteBufferInput();
    private final double[] cached = new double[200];

    {
        output.setBuffer(buffer, buffer.capacity());
        input.setBuffer(buffer, 0, buffer.capacity());
        kryo.register(double[].class, new DefaultArraySerializers.DoubleArraySerializer() {
            @Override
            public double[] read(Kryo kryo, Input input, Class<double[]> type) {
                int length = input.readVarInt(true);
                if (length == NULL) return null;
                for (int i = 0; i < cached.length; i++) {
                    cached[i] = input.readDouble();
                }
                return cached;
            }
        });
    }

    @Override
    public int encode(double[] values) {
        kryo.writeObject(output, values);
        return output.position();
    }

    @Override
    public double[] decode() {
        final double[] result = kryo.readObject(input, double[].class);
        return result;
    }

    @Override
    public void reset() {
        output.flush();
        output.setPosition(0);
        input.rewind();
        buffer.position(0);
    }

}
