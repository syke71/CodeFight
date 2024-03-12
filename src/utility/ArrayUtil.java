package utility;

/**
 * Utility class for array operations.
 *
 * @author uenqh
 */
public final class ArrayUtil {

    private ArrayUtil() {
    }

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
     * @return a new String array containing the specified range of elements from the source array
     */
    public static String[] copyOfRangeCircularArray(String[] source, int from, int to) {
        int newLength = Math.abs(to - from);
        String[] output = new String[newLength];
        for (int i = 0; i < newLength; i++) {
            output[i] = source[(from + i) % source.length];
        }
        return output;
    }
}
