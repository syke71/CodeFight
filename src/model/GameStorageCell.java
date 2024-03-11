package model;

/**
 * Represents a single cell in the game storage.
 * <p>
 * The GameStorageCell class encapsulates data related to a single cell in the game storage.
 * </p>
 *
 * @author uenqh
 */
public class GameStorageCell {

    private int entryA;
    private int entryB;
    private String command;
    private String lastEditedBy;
    private boolean wasChangedAfterInit;

    /**
     * Constructs a GameStorageCell object.
     */
    public GameStorageCell() {
        this.lastEditedBy = "";
        this.command = "";
        this.wasChangedAfterInit = false;
    }

    /**
     * Retrieves the value of entry A in the cell.
     *
     * @return The value of entry A.
     */
    public int getEntryA() {
        return this.entryA;
    }

    /**
     * Retrieves the value of entry B in the cell.
     *
     * @return The value of entry B.
     */
    public int getEntryB() {
        return this.entryB;
    }

    /**
     * Retrieves the command associated with the cell.
     *
     * @return The command associated with the cell.
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Sets the value of entry A in the cell.
     *
     * @param newEntryA The new value of entry A.
     */
    public void setEntryA(int newEntryA) {
        this.entryA = newEntryA;
    }

    /**
     * Sets the value of entry B in the cell.
     *
     * @param newEntryB The new value of entry B.
     */
    public void setEntryB(int newEntryB) {
        this.entryB = newEntryB;
    }

    /**
     * Sets the command associated with the cell.
     *
     * @param command The command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }


    /**
     * Retrieves the name of the last AI that edited the cell.
     *
     * @return The name of the last AI that edited the cell.
     */
    public String getLastChangedBy() {
        return this.lastEditedBy;
    }

    /**
     * Updates the name of the AI that edited the cell.
     *
     * @param aiName The name of the AI that edited the cell.
     */
    public void changedBy(String aiName) {
        this.lastEditedBy = aiName;
    }

    /**
     * Updates the name of the AI that edited the cell and marks the cell as changed after initialization.
     *
     * @param aiName The name of the AI that edited the cell.
     */
    public void postInitChangedBy(String aiName) {
        changedBy(aiName);
        setWasChangedAfterInitTrue();
    }

    /**
     * Sets the flag indicating whether the cell was changed after initialization to true.
     */
    private void setWasChangedAfterInitTrue() {
        this.wasChangedAfterInit = true;
    }

    /**
     * Retrieves the status indicating whether the cell was changed after initialization.
     *
     * @return {@code true} if the cell was changed after initialization, otherwise {@code false}.
     */
    public boolean getWasChangedAfterInitStatus() {
        return this.wasChangedAfterInit;
    }
}
