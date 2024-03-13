package utility;

import java.util.ArrayList;

/**
 * CircularArrayList is a subclass of ArrayList that implements circular behavior for index access.
 * This class ensures that indices wrap around when they exceed the size of the list.
 *
 * @param <E> the type of elements in this list
 * @author uenqh
 */
public class CircularArrayList<E> extends ArrayList<E> {
    private final int size;

    /**
     * Constructs a CircularArrayList with the specified size.
     *
     * @param size the size of the circular array list
     */
    public CircularArrayList(int size) {
        // super();
        this.size = size;
    }

    /**
     * Returns the element at the specified index in a circular manner.
     * If the index is out of bounds, it wraps around to the appropriate index within the bounds.
     *
     * @param index the index of the element to return
     * @return the element at the specified index
     */
    @Override
    public E get(int index) {
        return super.get(modulo(index));
    }

    /**
     * Sets the element at the specified index to the specified element in a circular manner.
     * If the index is out of bounds, it wraps around to the appropriate index within the bounds.
     *
     * @param index   the index at which to set the element
     * @param element the element to be set
     * @return the element previously at the specified index
     */
    @Override
    public E set(int index, E element) {
        return super.set(modulo(index), element);
    }

    private int modulo(int input) {
        int output = input;

        // Handle negative indices
        if (output < 0) {
            output += size * ((-output / size) + 1);
        }

        return output % size;
    }

    /**
     * Copies a range of elements from a circular ArrayList to a new ArrayList.
     *
     * @param source the source ArrayList
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive
     * @return a new String ArrayList containing the specified range of elements from the source ArrayList
     */
    public static CircularArrayList<String> copyOfRange(CircularArrayList<String> source, int from, int to) {
        int newLength = Math.abs(to - from);
        CircularArrayList<String> output = new CircularArrayList<>(source.size);
        for (int i = 0; i < newLength; i++) {
            output.add(source.get((from + i) % source.size()));
        }
        return output;
    }
}
