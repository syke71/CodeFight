package usercommands;

import model.Ai;
import model.GameSystem;

import static model.ConstantErrorMessages.REQUIRES_GAME_RUNNING_MESSAGE;

/**
 * Represents a command to end the currently running game.
 *
 * @author uenqh
 */
public class EndGameCommand implements Command {
    private static final int NUMBER_OF_ARGUMENTS = 0;
    private static final boolean REQUIRES_GAME_RUNNING = true;
    private static final String DESCRIPTION_MESSAGE = "This will end the currently running game.";
    private static final String DESCRIPTION_FORMAT = "%s : %s %s";
    private static final String RUNNING_AIS_FORMAT = "Running AIs: %s";
    private static final String STOPPED_AIS_FORMAT = "Stopped AIs: %s";
    private static final String NAME_PARTITION = ", ";
    private static final String LINE_BREAK = "\n";

    /**
     * Executes the command to end the currently running game.
     *
     * @param model             The GameSystem instance.
     * @param commandArguments The command arguments (not used).
     * @return The result of the command execution.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        StringBuilder stoppedAis = new StringBuilder();
        StringBuilder runningAis = new StringBuilder();
        String message = "";
        for (Ai ai : model.getInGameAis()) {
            if (model.getAliveAis().contains(ai)) {
                runningAis.append(ai.getName()).append(NAME_PARTITION);
            } else {
                stoppedAis.append(ai.getName()).append(NAME_PARTITION);
            }
        }
        if (!runningAis.isEmpty()) {
            runningAis.replace(runningAis.length() - NAME_PARTITION.length(), runningAis.length(), "");
            message += String.format(RUNNING_AIS_FORMAT, runningAis.toString());
            if (!stoppedAis.isEmpty()) {
                message += LINE_BREAK;
            }
        }
        if (!stoppedAis.isEmpty()) {
            stoppedAis.replace(stoppedAis.length() - NAME_PARTITION.length(), stoppedAis.length(), "");
            message += String.format(STOPPED_AIS_FORMAT, stoppedAis);
        }
        model.resetGame();
        return new CommandResult(CommandResultType.SUCCESS, message);
    }

    /**
     * Retrieves the number of arguments required for the command.
     *
     * @return The number of arguments required (0 in this case).
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Specifies whether the command requires the game to be running.
     *
     * @return True if the game must be running, false otherwise.
     */
    @Override
    public boolean requiresGameRunning() {
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
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_RUNNING_MESSAGE, DESCRIPTION_MESSAGE);
    }
}
