package usercommands;

import model.GameSystem;

import static model.ConstantErrorMessages.NO_RUNNING_REQUIREMENTS_MESSAGE;

/**
 * Represents a command to quit the program immediately.
 *
 * @author uenqh
 */
public class QuitCommand implements Command {
    private static final int NUMBER_OF_ARGUMENTS = 0;
    private static final boolean REQUIRES_GAME_RUNNING = false;
    private static final String DESCRIPTION_MESSAGE = "This quits the programm immediately.";
    private static final String DESCRIPTION_FORMAT = "%s: %s %s";
    private static final String QUIT_MESSAGE = null;

    /**
     * Executes the command to quit the program.
     *
     * @param model            The GameSystem instance.
     * @param commandArguments The command arguments (not used).
     * @return The result of the command execution.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        model.getCommandHandler().quit();
        return new CommandResult(CommandResultType.SUCCESS, QUIT_MESSAGE);
    }

    /**
     * Retrieves the number of arguments required for the command.
     *
     * @return The number of arguments required (0).
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Specifies whether the command requires the game to be running.
     *
     * @return False, although this command can be used whenever and is handled separately.
     */
    @Override
    public boolean requiredGameStatus() {
        return REQUIRES_GAME_RUNNING;
    }

    /**
     * Retrieves the description of the command.
     *
     * @param commandName The name of the command.
     * @return The description of the command.
     */
    @Override
    public String getDescription(String commandName) {
        return String.format(DESCRIPTION_FORMAT, commandName, NO_RUNNING_REQUIREMENTS_MESSAGE, DESCRIPTION_MESSAGE);
    }
}
