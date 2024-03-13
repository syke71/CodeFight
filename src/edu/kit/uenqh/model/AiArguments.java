package edu.kit.uenqh.model;

/**
 * Represents the arguments associated with an AI command.
 * <p>
 * An instance of this class encapsulates the command name and its two corresponding entry values.
 * </p>
 *
 * @author uenqh
 */
public class AiArguments {

    private final String command;
    private final int entryA;
    private final int entryB;

    /**
     * Constructs an AiArguments object with the specified command and entry values.
     *
     * @param command The name of the command.
     * @param entryA  The first entry value.
     * @param entryB  The second entry value.
     */
    public AiArguments(String command, int entryA, int entryB) {
        this.command = command;
        this.entryA = entryA;
        this.entryB = entryB;
    }

    /**
     * Retrieves the command associated with the arguments.
     *
     * @return The command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Retrieves the first entry value.
     *
     * @return The first entry value.
     */
    public int getEntryA() {
        return this.entryA;
    }

    /**
     * Retrieves the second entry value.
     *
     * @return The second entry value.
     */
    public int getEntryB() {
        return this.entryB;
    }

}
