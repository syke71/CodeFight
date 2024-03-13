package edu.kit.uenqh.userinput;

import edu.kit.uenqh.model.GameSystem;

import static edu.kit.uenqh.model.ConstantErrorMessages.REQUIRES_GAME_STOPPED_MESSAGE;

/**
 * Represents a command to remove previously added AIs.
 *
 * @author uenqh
 */
public class RemoveAiCommand implements Command {

    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final boolean REQUIRES_GAME_RUNNING = false;
    private static final String DESCRIPTION_MESSAGE = "With it, you can remove previously added AIs.";
    private static final String DESCRIPTION_FORMAT = "%s: %s %s";
    private static final int AI_NAME_INDEX = 0;
    private static final String UNKNOWN_AI_NAME = "The entered AI does not exist!";

    /**
     * Executes the command to remove an AI.
     *
     * @param model            The GameSystem instance.
     * @param commandArguments The command arguments (the name of the AI to be removed).
     * @return The result of the command execution.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        String aiName = commandArguments[AI_NAME_INDEX];
        if (!checkIfNameExists(model, aiName)) {
            return new CommandResult(CommandResultType.FAILURE, UNKNOWN_AI_NAME);
        }
        model.removeAi(aiName);
        return new CommandResult(CommandResultType.SUCCESS, aiName);
    }

    /**
     * Retrieves the number of arguments required for the command.
     *
     * @return The number of arguments required (1).
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Specifies whether the command requires the game to be running.
     *
     * @return False, as this command requires the game to be not running.
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
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_STOPPED_MESSAGE, DESCRIPTION_MESSAGE);
    }

    private boolean checkIfNameExists(GameSystem model, String name) {
        return model.getAiMap().containsKey(name);
    }
}
