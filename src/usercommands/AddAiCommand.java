package usercommands;

import model.Ai;
import model.GameSystem;

import java.util.regex.Pattern;

import static model.ConstantErrorMessages.REQUIRES_GAME_STOPPED_MESSAGE;
import static model.Constants.INTEGER_REGEX;

/**
 * A command to add an AI to the game.
 *
 * @author uenqh
 */
public class AddAiCommand implements Command {

    private static final int AI_NAME_INDEX = 0;
    private static final int AI_ARGUMENTS_INDEX = 1;
    private static final int MINIMUM_NUMBER_OF_AIS_PER_GAME = 2;
    private static final int NUMBER_OF_ARGUMENTS = 2;
    private static final int AMOUNT_OF_ARGUMENTS_PER_AI = 3;
    private static final int COMMAND_NAME_INDEX = 0;
    private static final int ENTRY_A_INDEX = 1;
    private static final int ENTRY_B_INDEX = 2;
    private static final boolean REQUIRES_GAME_RUNNING = false;
    private static final String DESCRIPTION_MESSAGE = "With it, you can add uniquely named AIs";
    private static final String DESCRIPTION_FORMAT = "%s: %s %s and %s";
    private static final String ARGUMENT_SEPARATOR_REGEX = ",";
    private static final String WRONG_ARGUMENT_LENGTH_MESSAGE = "the entered AI has too many specified arguments for this storage!";
    private static final String WRONG_ARGUMENT_MESSAGE = "the entered AI has the wrong format! ";
    private static final String ARGUMENT_FORMAT_MESSAGE = "the argument format should be: [Name] [Command name],[int],[int] ";
    private static final String CANNOT_OVERWRITE_AI_MESSAGE = "you cannot overwrite an already existing AI!";
    private static final String COMMON_ERROR_MESSAGE = "Unexpected error in AddAiCommand.java";

    /**
     * Executes the add AI command.
     *
     * @param model            The GameSystem model.
     * @param commandArguments The command arguments.
     * @return The result of the command execution.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        String aiName = commandArguments[AI_NAME_INDEX];
        String[] aiArguments = commandArguments[AI_ARGUMENTS_INDEX].trim().split(ARGUMENT_SEPARATOR_REGEX);

        if (!checkValidArgumentLength(model, aiArguments)) {
            return new CommandResult(CommandResultType.FAILURE, WRONG_ARGUMENT_LENGTH_MESSAGE);
        }
        if (!checkValidArgumentFormat(model, aiArguments)) {
            return new CommandResult(CommandResultType.FAILURE, WRONG_ARGUMENT_MESSAGE + ARGUMENT_FORMAT_MESSAGE);
        }
        if (checkIfNameAlreadyExists(model, aiName)) {
            return new CommandResult(CommandResultType.FAILURE, CANNOT_OVERWRITE_AI_MESSAGE);
        }

        Ai ai = new Ai(aiName);

        String aiCommand;
        int entryA;
        int entryB;

        for (int i = 0; i < (aiArguments.length / AMOUNT_OF_ARGUMENTS_PER_AI); i++) {

            aiCommand = aiArguments[AMOUNT_OF_ARGUMENTS_PER_AI * i + COMMAND_NAME_INDEX];
            entryA = Integer.parseInt(aiArguments[AMOUNT_OF_ARGUMENTS_PER_AI * i + ENTRY_A_INDEX]);
            entryB = Integer.parseInt(aiArguments[AMOUNT_OF_ARGUMENTS_PER_AI * i + ENTRY_B_INDEX]);

            ai.addArgument(aiCommand, entryA, entryB);
        }
        model.addAi(ai);
        return new CommandResult(CommandResultType.SUCCESS, aiName);
    }

    /**
     * Retrieves the number of required command arguments.
     *
     * @return The number of required command arguments.
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Specifies if the game must be running for this command to execute.
     *
     * @return True if the game must be running, false otherwise.
     */
    @Override
    public boolean requiredGameStatus() {
        return REQUIRES_GAME_RUNNING;
    }

    /**
     * Retrieves the description of the command.
     *
     * @param commandName The name of the command.
     * @return The command description.
     */
    @Override
    public String getDescription(String commandName) {
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_STOPPED_MESSAGE,
            DESCRIPTION_MESSAGE, ARGUMENT_FORMAT_MESSAGE);
    }

    private boolean checkValidArgumentLength(GameSystem model, String[] arguments) {
        int argumentCount = arguments.length / AMOUNT_OF_ARGUMENTS_PER_AI;
        int availableSpace = model.getGameStorage().getSize();
        return argumentCount <= Math.ceil(availableSpace / (double) MINIMUM_NUMBER_OF_AIS_PER_GAME);
    }

    private boolean checkValidArgumentFormat(GameSystem model, String[] arguments) {
        if (arguments.length % AMOUNT_OF_ARGUMENTS_PER_AI != 0) {
            return false;
        }
        for (int i = 0; i < arguments.length; i++) {
            switch (i % AMOUNT_OF_ARGUMENTS_PER_AI) {
                case COMMAND_NAME_INDEX -> {
                    if (!model.getAiCommandHandler().getAiCommandMap().containsKey(arguments[i])) {
                        return false;
                    }
                }
                case ENTRY_A_INDEX, ENTRY_B_INDEX -> {
                    if (!Pattern.matches(INTEGER_REGEX, arguments[i])) {
                        return false;
                    }
                }
                default -> printError();
            }
        }
        return true;
    }

    private boolean checkIfNameAlreadyExists(GameSystem model, String name) {
        return model.getAiMap().containsKey(name);
    }

    private void printError() {
        System.err.println(COMMON_ERROR_MESSAGE);
    }
}
