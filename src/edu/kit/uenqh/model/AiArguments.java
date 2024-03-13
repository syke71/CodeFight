package edu.kit.uenqh.model;

/**
 * Represents arguments for an AI command.
 * <p>
 * This record contains a command and two integer entries.
 * </p>
 *
 * @author uenqh
 *
 * @param command the command associated with the arguments
 * @param entryA  the first entry value
 * @param entryB  the second entry value
 */
public record AiArguments(String command, int entryA, int entryB) {

    /**
     * Constructs a new {@code AiArguments} instance.
     */
    public AiArguments {
    }

    /**
     * Retrieves the command associated with the arguments.
     *
     * @return The command.
     */
    @Override
    public String command() {
        return this.command;
    }

    /**
     * Retrieves the first entry value.
     *
     * @return The first entry value.
     */
    @Override
    public int entryA() {
        return this.entryA;
    }

    /**
     * Retrieves the second entry value.
     *
     * @return The second entry value.
     */
    @Override
    public int entryB() {
        return this.entryB;
    }

}
