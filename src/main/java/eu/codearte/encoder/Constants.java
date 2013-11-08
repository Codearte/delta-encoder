package eu.codearte.encoder;

public interface Constants {
    int PRECISION_BITS = 4;
    int DELTA_SIZE_BITS = 5;
    int LENGTH_BITS = Integer.SIZE - PRECISION_BITS - DELTA_SIZE_BITS;
}
