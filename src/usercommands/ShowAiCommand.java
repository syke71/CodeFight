package usercommands;

import model.Ai;
import model.GameStorageCell;
import model.GameSystem;

import static model.ConstantErrorMessages.REQUIRES_GAME_RUNNING_MESSAGE;
import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;

/**
 * Represents a command to show the current status of a specific AI in the game system.
 * <p>
 * This command displays the current status of a given AI.
 * </p>
 *
 * @author uenqh
 */
public class ShowAiCommand implements Command {

    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final boolean REQUIRES_GAME_RUNNING = true;
    private static final String DESCRIPTION_MESSAGE = "'%s' shows you the current status of any currently playing AI.";
    private static final String DESCRIPTION_FORMAT = "%s : %s %s";
    private static final int AI_NAME_INDEX = 0;
    private static final int UNIQUE_AI_ID = -1;
    private static final String RUNNING_MESSAGE = "RUNNING";
    private static final String STOPPED_MESSAGE = "STOPPED";
    private static final String STATUS_FORMAT = "%s (%s@%s)";
    private static final String NEXT_ACTION_FORMAT = "Next Command: %s @ %s";
    private static final String GAME_STORAGE_CELL_FORMAT = "%s|%s|%s";
    private static final String BREAK_LINE = "\n";
    private static final String UNKNOWN_AI_NAME = "The entered AI does not exist!";

    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {
        String aiName = commandArguments[AI_NAME_INDEX];

        if (!checkNameExists(model, aiName)) {
            return new CommandResult(CommandResultType.FAILURE, UNKNOWN_AI_NAME);
        }

        Ai ai = getAi(model, aiName);
        String status = isAlive(model, aiName) ? RUNNING_MESSAGE : STOPPED_MESSAGE;
        String message = String.format(STATUS_FORMAT, aiName, status, ai.getRoundCounter());

        if (isAlive(model, aiName)) {
            GameStorageCell cell = model.getGameStorage().getCells().get(ai.getPointerIndex());
            String nextAction = String.format(GAME_STORAGE_CELL_FORMAT, cell.getCommand(), cell.getEntryA(), cell.getEntryB());
            int adjustedPoint = ai.getPointerIndex() % model.getGameStorage().getSize();
            message += BREAK_LINE + String.format(NEXT_ACTION_FORMAT, nextAction, adjustedPoint);
        }

        return new CommandResult(CommandResultType.SUCCESS, message);
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    @Override
    public boolean requiresGameRunning() {
        return REQUIRES_GAME_RUNNING;
    }

    @Override
    public String getDescription(String commandName) {
        String message = String.format(DESCRIPTION_MESSAGE, commandName);
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_RUNNING_MESSAGE, message);
    }

    private boolean checkNameExists(GameSystem model, String aiName) {
        return getAi(model, aiName) != null;
    }

    private Ai getAi(GameSystem model, String aiName) {
        for (Ai ai : model.getInGameAis()) {
            if (ai.getId() == UNIQUE_AI_ID) {
                if (ai.getName().equals(aiName)) {
                    return ai;
                }
            } else {
                String nameWithID = ai.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + ai.getId();
                if (nameWithID.equals(aiName)) {
                    return ai;
                }
            }
        }
        return null;
    }

    private boolean isAlive(GameSystem model, String aiName) {
        for (Ai ai : model.getAliveAis()) {
            if (aiName.equals(ai.getName())) {
                return true;
            } else if (aiName.equals(ai.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + ai.getId())) {
                return true;
            }
        }
        return false;
    }
}
