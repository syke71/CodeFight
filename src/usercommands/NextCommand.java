package usercommands;

import model.Ai;
import model.GameSystem;

import java.util.ArrayList;

import static model.ConstantErrorMessages.REQUIRES_GAME_RUNNING_MESSAGE;
import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;
import static model.Constants.STOP_COMMAND_NAME;

/**
 * Represents a command to manually cycle through the game loop.
 *
 * @author uenqh
 */
public class NextCommand implements Command {

    private static final int NUMBER_OF_ARGUMENTS = -1;
    private static final int MAX_NUMBER_OF_ARGUMENT = 1;
    private static final boolean REQUIRES_GAME_RUNNING = true;
    private static final String DESCRIPTION_MESSAGE = "'%s' is used to manually cycle through the games loop.";
    private static final String DESCRIPTION_FORMAT = "%s: %s %s";
    private static final int ARGUMENT_INDEX = 0;
    private static final int STANDARD_STEP_AMOUNT = 1;
    private static final String WRONG_ARGUMENT_FORMAT_MESSAGE = "the entered argument should be a number or empty!";
    private static final String WRONG_ARGUMENT_AMOUNT_MESSAGE = "please only enter one number or leave the argument blank!";
    private static final String AI_STOPPED_AFTER_X_STEPS_FORMAT = "%s executed %s steps until stopping.";
    private static final String EMPTY_MESSAGE = null;

    /**
     * Executes the command to cycle through the game loop.
     *
     * @param model            The GameSystem instance.
     * @param commandArguments The command arguments (optional).
     * @return The result of the command execution.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        int stepAmount = STANDARD_STEP_AMOUNT;
        if (!checkEmptyArgument(commandArguments)) {
            if (!checkValidInputLength(commandArguments)) {
                return new CommandResult(CommandResultType.FAILURE, WRONG_ARGUMENT_AMOUNT_MESSAGE);
            }
            if (!checkValidInputType(commandArguments)) {
                return new CommandResult(CommandResultType.FAILURE, WRONG_ARGUMENT_FORMAT_MESSAGE);
            }
            stepAmount = Integer.parseInt(commandArguments[ARGUMENT_INDEX]);
        }

        Ai currentAi;
        ArrayList<Ai> newAiDropOuts = new ArrayList<>();
        int step = 0;
        while (!model.getAliveAis().isEmpty() && step != stepAmount) {
            // fetch currently active AI
            currentAi = model.getAliveAis().pollFirst();
            if (currentAi.getRoundCounter() == 0) {
                while (model.getGameStorage().getCells().get(currentAi.getPointerIndex()).getCommand().equals(STOP_COMMAND_NAME)) {
                    currentAi.updatePointerIndex();
                }
            }
            model.getAiCommandHandler().execute(currentAi);

            // check if executed command was a 'STOP' command
            if (currentAi.getAliveStatus()) {
                currentAi.updateRoundCounter();
                model.getAliveAis().add(currentAi);
            } else {
                newAiDropOuts.add(currentAi);
            }
            step++;
        }

        if (newAiDropOuts.isEmpty()) {
            return new CommandResult(CommandResultType.SUCCESS, EMPTY_MESSAGE);
        }
        return new CommandResult(CommandResultType.SUCCESS, buildReturnMessage(model, newAiDropOuts));
    }

    /**
     * Retrieves the number of arguments required for the command.
     *
     * @return The number of arguments required (-1 indicating optional).
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Specifies whether the command requires the game to be running.
     *
     * @return True, as this command requires the game to be running.
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
        String message = String.format(DESCRIPTION_MESSAGE, commandName);
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_RUNNING_MESSAGE, message);
    }

    private boolean checkValidInputType(String[] commandArguments) {
        try {
            Integer.parseInt(commandArguments[ARGUMENT_INDEX]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean checkEmptyArgument(String[] commandArguments) {
        return commandArguments.length == 0;
    }
    private boolean checkValidInputLength(String[] commandArguments) {
        return commandArguments.length <= MAX_NUMBER_OF_ARGUMENT;
    }

    private String buildReturnMessage(GameSystem model, ArrayList<Ai> newAiDropOuts) {
        StringBuilder stringBuilder = new StringBuilder();
        int stepCounter;
        int i = 0;
        for (Ai ai : newAiDropOuts) {
            stepCounter = ai.getRoundCounter();
            String name = ai.getName();
            if (ai.getId() != -1) {
                name += BETWEEN_NAME_AND_ID_PLACEHOLDER + ai.getId();
            }
            stringBuilder.append(AI_STOPPED_AFTER_X_STEPS_FORMAT.formatted(name, stepCounter));
            if (i++ != newAiDropOuts.size() - 1) {
                stringBuilder.append("\n");
            }
        }
        if (model.getInGameAis().isEmpty()) {
            model.toggleGameStatus();
        }
        return stringBuilder.toString();
    }
}
