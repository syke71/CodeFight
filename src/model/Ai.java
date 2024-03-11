package model;

import java.util.ArrayList;

/**
 * Represents an AI entity in the game.
 * <p>
 * An AI has a name, a symbol, and a bomb symbol used for displaying its presence in the game environment.
 * It also tracks its current position in the game storage, its round counter, and whether its currently alive or not.
 * An ID can be assigned to account for duplicates when starting the game.
 * Ai additionally maintains a list of arguments for executing commands.
 * </p>
 * @author uenqh
 */
public class Ai {
    private static final int DEFAULT_ID = -1;
    private final ArrayList<AiArguments> arguments;
    private String name;
    private String symbol;
    private String bombSymbol;
    private boolean alive;
    private int pointerIndex;
    private int id;
    private int roundCounter;

    /**
     * Constructs an AI with the specified name.
     *
     * @param name The name of the AI.
     */
    public Ai(String name) {
        this.name = name;
        this.roundCounter = 0;
        this.id = DEFAULT_ID;
        this.arguments = new ArrayList<>();
    }

    /**
     * Creates a deep copy of the AI instance.
     *
     * @return A deep copy of the AI.
     */
    public Ai copy() {
        Ai clone = new Ai(this.name);
        clone.arguments.addAll(this.arguments);
        return clone;
    }

    /**
     * Adds an argument to the AI's list of arguments.
     *
     * @param command The command to be added.
     * @param entryA  The first entry value.
     * @param entryB  The second entry value.
     */
    public void addArgument(String command, int entryA, int entryB) {
        AiArguments argument = new AiArguments(command, entryA, entryB);
        this.arguments.add(argument);
    }

    /**
     * Updates the pointer index of the AI.
     *
     * @param position The new pointer index value.
     */
    public void updatePointerIndex(int position) {
        this.pointerIndex = position;
    }

    /**
     * Updates the round counter of the AI by incrementing it.
     */
    public void updateRoundCounter() {
        this.roundCounter++;
    }

    /**
     * Updates the pointer index of the AI by incrementing it.
     */
    public void updatePointerIndex() {
        this.pointerIndex++;
    }

    /**
     * Retrieves the list of arguments associated with the AI.
     *
     * @return The list of arguments.
     */
    public ArrayList<AiArguments> getArguments() {
        return this.arguments;
    }

    /**
     * Retrieves the name of the AI.
     *
     * @return The name of the AI.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the AI.
     *
     * @param newName The new name of the AI.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Retrieves the pointer index of the AI.
     *
     * @return The pointer index of the AI.
     */
    public int getPointerIndex() {
        return this.pointerIndex;
    }

    /**
     * Retrieves the round counter of the AI.
     *
     * @return The round counter of the AI.
     */
    public int getRoundCounter() {
        return this.roundCounter;
    }

    /**
     * Retrieves the alive status of the AI.
     *
     * @return {@code true} if the AI is alive, {@code false} otherwise.
     */
    public boolean getAliveStatus() {
        return this.alive;
    }

    /**
     * Toggles the alive status of the AI.
     * If the AI is alive, it becomes dead, and vice versa.
     */
    public void toggleAliveStatus() {
        this.alive = !this.alive;
    }

    /**
     * Retrieves the ID of the AI.
     *
     * @return The ID of the AI.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the ID of the AI.
     *
     * @param newId The new ID of the AI.
     */
    public void setId(int newId) {
        this.id = newId;
    }

    /**
     * Retrieves the symbol of the AI.
     *
     * @return The symbol of the AI.
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Sets the symbol of the AI.
     *
     * @param newSymbol The new symbol of the AI.
     */
    public void setSymbol(String newSymbol) {
        this.symbol = newSymbol;
    }

    /**
     * Retrieves the bomb symbol of the AI.
     *
     * @return The bomb symbol of the AI.
     */
    public String getBombSymbol() {
        return this.bombSymbol;
    }

    /**
     * Sets the bomb symbol of the AI.
     *
     * @param newBombSymbol The new bomb symbol of the AI.
     */
    public void setBombSymbol(String newBombSymbol) {
        this.bombSymbol = newBombSymbol;
    }
}
