package utility;

import java.lang.reflect.Array;

/**
 * Utility class for array operations.
 *
 * @author uenqh
 */
public class ArrayUtil {

    /**
     * Copies a range of elements from a circular array to a new array.
     *
     * <p>
     * This method is partially based on the copyOfRange() method described in java.util.Arrays
     * </p>
     *
     * @param source the source array
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive
     * @param <E> the type of elements in the array
     * @return a new array containing the specified range of elements from the source array
     */
    public static <E> E[] copyOfRangeCircularArray(E[] source, int from, int to) {
        int newLength = Math.abs(to - from);
        @SuppressWarnings("unchecked")
        E[] output = (E[]) Array.newInstance(source.getClass().getComponentType(), newLength);
        for (int i = 0 ; i < newLength; i++) {
            output[i] = source[(from + i) % source.length];
        }
        return output;
    }
}

