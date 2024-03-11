package model;

import utility.CircularArrayList;

/**
 * Represents the storage of game cells in the game system.
 * <p>
 * The GameStorage class manages a circular list of GameStorageCell objects,
 * representing the memory cells used by the game system.
 * </p>
 *
 * @author uenqh
 */
public class GameStorage {
    private final CircularArrayList<GameStorageCell> storage;
    private final int size;

    /**
     * Constructs a GameStorage object with the specified size.
     *
     * @param size The size of the game storage.
     */
    public GameStorage(int size) {
        this.size = size;
        this.storage = new CircularArrayList<GameStorageCell>(this.size);
        this.initGameStorageCells(size);
    }

    /**
     * Initializes the game storage with empty cells.
     *
     * @param size The size of the game storage.
     */
    private void initGameStorageCells(int size) {
        for (int i = 0; i < size; i++) {
            storage.add(new GameStorageCell());
        }
    }

    /**
     * Retrieves the list of game storage cells.
     *
     * @return The circular list of game storage cells.
     */
    public CircularArrayList<GameStorageCell> getCells() {
        return this.storage;
    }

    /**
     * Retrieves the size of the game storage.
     *
     * @return The size of the game storage.
     */
    public int getSize() {
        return this.size;
    }
}
