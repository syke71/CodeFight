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
}
