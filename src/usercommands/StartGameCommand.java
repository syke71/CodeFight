package usercommands;

import model.GameStorage;
import model.GameStorageCell;
import model.GameSystem;

import java.util.Arrays;

import static model.ConstantErrorMessages.REQUIRES_GAME_STOPPED_MESSAGE;
import static model.Constants.START_GAME_COMMAND_NAME;
import static model.Constants.STOP_COMMAND_NAME;
import static model.Constants.WRONG_ARGUMENTS_COUNT_FORMAT;

/**
 * Represents a command to start a new game with the specified AIs in a {@link GameSystem}.
 * This command requires the game to be stopped before execution.
 *
 * @author uenqh
 */
public class StartGameCommand implements Command {

    private static final int NUMBER_OF_ARGUMENTS = -1;
    private static final int LEAST_NUMBER_OF_ARGUMENTS = 2;
    private static final boolean REQUIRES_GAME_RUNNING = false;
    private static final String DESCRIPTION_MESSAGE = "With it, you can start a new game using at least %s previously added AIs.";
    private static final String DESCRIPTION_FORMAT = "%s: %s %s";
    private static final String UNKNOWN_AI_MESSAGE = "the entered AI names could not be found!";
    private static final String INVALID_AI_ARGUMENT_LENGTH_FORMAT =
        "given the current settings, the entered AI '%s' has more parameters to load than the storage size allows!";
    private static final String GAME_REQUIRES_NON_STOP_COMMAND = "the game requires at least one not-STOP command to start!";
    private static final String GAME_STARTED_MESSAGE = "Game started.";

    /**
     * Executes the start game command with the provided game system and command arguments.
     * Checks if the number of arguments is valid, if the specified AIs exist,
     * and if the AIs have valid argument lengths.
     *
     * @param model            The {@link GameSystem} to execute the command on.
     * @param commandArguments The arguments provided with the command.
     * @return A {@link CommandResult} indicating the result of the execution.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        if (!checkValidInputLength(model, commandArguments)) {
            String message = String.format(WRONG_ARGUMENTS_COUNT_FORMAT, START_GAME_COMMAND_NAME);
            return new CommandResult(CommandResultType.FAILURE, message);
        }
        if (!checkIfAisExist(model, commandArguments)) {
            return new CommandResult(CommandResultType.FAILURE, UNKNOWN_AI_MESSAGE);
        }
        String invalidAi = checkValidAiArgumentLength(model, commandArguments);
        if (invalidAi != null) {
            return new CommandResult(CommandResultType.FAILURE, String.format(INVALID_AI_ARGUMENT_LENGTH_FORMAT, invalidAi));
        }

        String[] aiList = Arrays.copyOf(commandArguments, commandArguments.length);
        model.startGame(aiList);
        if (checkIfGameHasOnlyStopCommands(model)) {
            model.resetGame();
            return new CommandResult(CommandResultType.FAILURE, GAME_REQUIRES_NON_STOP_COMMAND);
        }
        return new CommandResult(CommandResultType.SUCCESS, GAME_STARTED_MESSAGE);
    }

    /**
     * Retrieves the number of arguments required for the command.
     *
     * @return The number of arguments required for the command.
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Indicates whether the command requires the game to be running for execution.
     *
     * @return True if the game must be running, false otherwise.
     */
    @Override
    public boolean requiredGameStatus() {
        return REQUIRES_GAME_RUNNING;
    }

    /**
     * Generates a description of the command including usage instructions and requirements.
     *
     * @param commandName The name of the command.
     * @return A description of the command.
     */
    @Override
    public String getDescription(String commandName) {
        String message = String.format(DESCRIPTION_MESSAGE, LEAST_NUMBER_OF_ARGUMENTS);
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_STOPPED_MESSAGE, message);
    }

    private boolean checkValidInputLength(GameSystem model, String[] commandArguments) {
        return commandArguments.length >= LEAST_NUMBER_OF_ARGUMENTS && commandArguments.length <= model.getMaxAmountOfAis();
    }

    private boolean checkIfAisExist(GameSystem model, String[] commandArguments) {
        for (String s : commandArguments) {
            if (!model.getAiMap().containsKey(s)) {
                return false;
            }
        }
        return true;
    }

    private String checkValidAiArgumentLength(GameSystem model, String[] commandArguments) {
        int storageSize = model.getGameStorage().getSize();
        int numberOfAis = commandArguments.length;
        int currentPosition;
        int nextPosition;
        int availableSpace;

        for (int i = 0; i < commandArguments.length; i++) {
            currentPosition = (storageSize / numberOfAis) * i;
            nextPosition = (storageSize / numberOfAis) * (i + 1);
            availableSpace = nextPosition - currentPosition;
            if (i == commandArguments.length - 1) {
                availableSpace = storageSize - currentPosition;
            }
            if (model.getAiMap().get(commandArguments[i]).getArguments().size() > availableSpace) {
                return commandArguments[i];
            }
        }
        return null;
    }

    private boolean checkIfGameHasOnlyStopCommands(GameSystem model) {
        GameStorage storage = model.getGameStorage();
        for (GameStorageCell cell : storage.getCells()) {
            if (!cell.getCommand().equals(STOP_COMMAND_NAME)) {
                return false;
            }
        }
        return true;
    }
}
