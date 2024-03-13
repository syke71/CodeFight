package edu.kit.uenqh.userinput;

import edu.kit.uenqh.model.GameSystem;

/**
 * This interface represents an executable command.
 *
 * @author Programmieren-Team
 * @author uenqh
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param model            the edu.kit.uenqh.model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    CommandResult execute(GameSystem model, String[] commandArguments);

    /**
     * Returns the number of arguments that the command expects.
     *
     * @return the number of arguments that the command expects
     */
    int getNumberOfArguments();

    /**
     * Returns whether the command requires the game to be running or not.
     *
     * @return whether the command requires the game to be running or not.
     */
    boolean requiredGameStatus();

    /**
     * Returns a short description of a given command.
     *
     * @return a short description of a given command in String format.
     * @param commandName the commands name.
     */
    String getDescription(String commandName);
}
