package usercommands;

import model.GameSystem;
import model.InitMode;

import java.util.regex.Pattern;

import static model.ConstantErrorMessages.REQUIRES_GAME_STOPPED_MESSAGE;
import static model.Constants.INTEGER_REGEX;
import static model.Constants.SET_INIT_MODE_COMMAND_NAME;
import static model.Constants.WRONG_ARGUMENTS_COUNT_FORMAT;

/**
 * Represents a command to set the initialization mode for the game storage.
 *
 * @author uenqh
 */
public class SetInitModeCommand implements Command {

    private static final int NUMBER_OF_ARGUMENTS = -1;
    private static final int INIT_MODE_STOP_NEEDED_NUMBER_OF_ARGUMENTS = 1;
    private static final int INIT_MODE_RANDOM_NEEDED_NUMBER_OF_ARGUMENTS = 2;
    private static final int INIT_MODE_NAME_INDEX = 0;
    private static final int SEED_INDEX = 1;
    private static final boolean REQUIRES_GAME_RUNNING = false;
    private static final String INIT_MODES_LISTING_FORMAT = " and ";
    private static final String DESCRIPTION_MESSAGE = "Using '%s', you get to chose between two storage initialization options :";
    private static final String DESCRIPTION_FORMAT = "%s : %s %s %s.";

    private static final int MAX_SEED = 1337;
    private static final int MIN_SEED = -1337;
    private static final String UNKNOWN_INIT_TYPE_MESSAGE = "the entered init type does not exist!";
    private static final String WRONG_SEED_TYPE_MESSAGE = "the entered seed should be a number!";
    private static final String SEED_OUT_OF_BOUNCE_FORMAT = "the entered seed is out bounce for '%s'!";
    private static final String INIT_MODE_DID_NOT_CHANGE_MESSAGE = "";
    private static final String SUCCESSFUL_MODE_CHANGE_FORMAT = "Changed init mode from %s to %s";
    private static final String SPACE = " ";
    private static final String COMMON_ERROR_MESSAGE = "Unexpected error in SetInitModeCommand.java";



    /**
     * Executes the command to set the initialization mode for the game storage.
     *
     * @param model            The GameSystem instance.
     * @param commandArguments The command arguments (init mode and optional seed).
     * @return The result of the command execution.
     */
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        String initModeName = commandArguments[INIT_MODE_NAME_INDEX];
        InitMode newMode = InitMode.valueOf(initModeName);

        if (!checkValidInitType(model, initModeName)) {
            return new CommandResult(CommandResultType.FAILURE, UNKNOWN_INIT_TYPE_MESSAGE);
        }

        if (!checkValidNumberOfArguments(newMode, commandArguments)) {
            String message = WRONG_ARGUMENTS_COUNT_FORMAT.formatted(SET_INIT_MODE_COMMAND_NAME);
            return new CommandResult(CommandResultType.FAILURE, message);
        }

        int newSeed = newMode == InitMode.INIT_MODE_RANDOM ? parseSeed(commandArguments[SEED_INDEX]) : 0;
        if (newMode == InitMode.INIT_MODE_RANDOM && newSeed == Integer.MIN_VALUE) {
            return new CommandResult(CommandResultType.FAILURE, WRONG_SEED_TYPE_MESSAGE);
        } else if (newSeed != 0 && !checkSeedInBounds(newSeed)) {
            return new CommandResult(CommandResultType.FAILURE, String.format(SEED_OUT_OF_BOUNCE_FORMAT, newSeed));
        }

        String oldState;
        String newState;
        if (model.getInitMode().equals(InitMode.INIT_MODE_RANDOM)) {
            oldState = model.getInitMode() + SPACE + model.getSeed();
        } else {
            oldState = model.getInitMode().toString();
        }
        if (newMode.equals(InitMode.INIT_MODE_RANDOM)) {
            newState = newMode + SPACE + newSeed;
        } else {
            newState = newMode.toString();
        }
        if (model.getInitMode() == InitMode.INIT_MODE_STOP && newMode == InitMode.INIT_MODE_STOP) {
            return new CommandResult(CommandResultType.SUCCESS, INIT_MODE_DID_NOT_CHANGE_MESSAGE);
        } else if (model.getInitMode() == InitMode.INIT_MODE_STOP) {
            model.setInitMode(newMode);
            model.setSeed(newSeed);
        } else if (newMode == InitMode.INIT_MODE_STOP) {
            model.setInitMode(newMode);
        } else {
            model.setSeed(newSeed);
        }
        return new CommandResult(CommandResultType.SUCCESS, String.format(SUCCESSFUL_MODE_CHANGE_FORMAT, oldState, newState));
    }


    /**
     * Retrieves the number of arguments required for the command.
     *
     * @return The number of arguments required (-1).
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Specifies whether the command requires the game to be running.
     *
     * @return False, as this command does not require the game to be running.
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
        String message = String.format(DESCRIPTION_MESSAGE, commandName);
        String gameStorageModes = InitMode.INIT_MODE_STOP + INIT_MODES_LISTING_FORMAT + InitMode.INIT_MODE_RANDOM;
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_STOPPED_MESSAGE, message, gameStorageModes);
    }

    private boolean checkValidInitType(GameSystem model, String initMode) {
        return InitMode.INIT_MODE_RANDOM.toString().equals(initMode) || InitMode.INIT_MODE_STOP.toString().equals(initMode);
    }

    private boolean checkValidNumberOfArguments(InitMode mode, String[] commandArguments) {
        switch (mode) {
            case INIT_MODE_STOP -> {
                if (commandArguments.length != INIT_MODE_STOP_NEEDED_NUMBER_OF_ARGUMENTS) {
                    return false;
                }
            }
            case INIT_MODE_RANDOM -> {
                if (commandArguments.length != INIT_MODE_RANDOM_NEEDED_NUMBER_OF_ARGUMENTS) {
                    return false;
                }
            }
            default -> System.err.println(COMMON_ERROR_MESSAGE);
        }
        return true;
    }

    private boolean checkIfSeedIsInteger(String seedString) {
        return Pattern.matches(INTEGER_REGEX, seedString);
    }

    private int parseSeed(String seedString) {
        try {
            return Integer.parseInt(seedString);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    private boolean checkSeedInBounds(int seed) {
        return seed >= MIN_SEED && seed <= MAX_SEED;
    }
}
