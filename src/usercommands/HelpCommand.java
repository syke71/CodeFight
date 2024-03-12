package usercommands;

import model.GameSystem;

import java.util.ArrayList;
import java.util.Map;

import static model.ConstantErrorMessages.NO_RUNNING_REQUIREMENTS_MESSAGE;
import static model.Constants.HELP_COMMAND_NAME;
import static model.Constants.QUIT_COMMAND_NAME;

/**
 * Represents a command to display a short description of all currently available commands.
 *
 * @author uenqh
 */
public class HelpCommand implements Command {

    private static final int NUMBER_OF_ARGUMENTS = 0;
    private static final boolean REQUIRES_GAME_RUNNING = false;
    private static final String DESCRIPTION_MESSAGE = "displays a short description of all currently available commands.";
    private static final String DESCRIPTION_FORMAT = "%s: %s %s";
    private static final String LINE_BREAK = "\n";

    /**
     * Executes the command to display the help message.
     *
     * @param model             The GameSystem instance.
     * @param commandArguments  The command arguments (not used).
     * @return The result of the command execution.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        ArrayList<String> commandsList = model.getCommandHandler().getCommandsList();
        Map<String, Command> commandMap = model.getCommandHandler().getCommandsMap();

        StringBuilder message = new StringBuilder();
        // the commands list is always sorted on initialization
        for (String s : commandsList) {
            if (commandMap.get(s).requiredGameStatus() == model.getGameStatus() || commandIsAlwaysUsable(s)) {
                message.append(commandMap.get(s).getDescription(s));
                message.append(LINE_BREAK);
            }
        }
        message.delete(message.length() - LINE_BREAK.length(), message.length());
        return new CommandResult(CommandResultType.SUCCESS, message.toString());
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

    private boolean commandIsAlwaysUsable(String commandName) {
        return commandName.equals(QUIT_COMMAND_NAME) || commandName.equals(HELP_COMMAND_NAME);
    }

}
