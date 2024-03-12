package usercommands;

import model.Ai;
import model.GameStorage;
import model.GameStorageCell;
import model.GameSystem;
import utility.ArrayUtil;
import utility.CircularArrayList;

import java.util.Arrays;
import java.util.LinkedList;

import static model.ConstantErrorMessages.REQUIRES_GAME_RUNNING_MESSAGE;
import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;
import static model.Constants.CURRENT_AI_SYMBOL_INDEX;
import static model.Constants.JUMP_CHECK_CELL_COMMAND_NAME;
import static model.Constants.JUMP_COMMAND_NAME;
import static model.Constants.NEXT_AIS_SYMBOL_INDEX;
import static model.Constants.SHOW_MEMORY_COMMAND_NAME;
import static model.Constants.SHOW_STORAGE_SYMBOL_INDEX;
import static model.Constants.STOP_COMMAND_NAME;
import static model.Constants.UNCHANGED_FIELD_SYMBOL_INDEX;

/**
 * Represents a command to display the current memory state of the game system.
 * <p>
 * This command provides a quick overview or detailed display of the current game storage.
 * If detailed mode is selected, a table format is used to present the storage content with additional information.
 * </p>
 *
 * @author uenqh
 */
public class ShowMemoryCommand implements Command {
    private static final int NUMBER_OF_ARGUMENTS = -1;
    private static final int LEAST_NUMBER_OF_ARGUMENTS = 0;
    private static final int MOST_NUMBER_OF_ARGUMENTS = 1;
    private static final boolean REQUIRES_GAME_RUNNING = true;
    private static final String DESCRIPTION_MESSAGE = "Using '%s', you can either get";
    private static final String OPTIONS_MESSAGE = "a quick Overview or detailed display of the current game storage.";
    private static final String DESCRIPTION_FORMAT = "%s: %s %s %s";

    private static final int DISPLAY_POSITION_INDEX = 0;
    private static final int STANDARD_DISPLAY_SIZE = 10;
    private static final int STANDARD_TABLE_COLUMN_AMOUNT = 7;
    private static final int SYMBOL_COLUMN_INDEX = 0;
    private static final int CELL_POSITION_COLUMN_INDEX = 1;
    private static final String CELL_POSITION_SYMBOL = ":";
    private static final int COMMAND_NAME_COLUMN_INDEX = 2;
    private static final int MIDDLE_BAR_FIRST_INDEX = 3;
    private static final int MIDDLE_BAR_SECOND_INDEX = 5;
    private static final String MIDDLE_BAR = "|";
    private static final int ENTRY_A_COLUMN_INDEX = 4;
    private static final int ENTRY_B_COLUMN_INDEX = 6;
    private static final String LINE_BREAK = "\n";
    private static final String COLUMN_PARTITION = " ";
    private static final String PADDING_FORMAT = "%%%ds";

    private static final String WRONG_ARGUMENT_AMOUNT_MESSAGE = "please only enter one number or leave the argument blank!";
    private static final String WRONG_ARGUMENT_TYPE_FORMAT = "only numbers are allowed for the command '%s'!";
    private static final String ARGUMENT_OUT_OF_BOUNDS_MESSAGE = "the entered number is not within the storage size!";

    private static final int NAME_INDEX = 0;
    private static final int ID_INDEX = 1;

    private static final String COMMON_ERROR_MESSAGE = "An error occurred in ShowMemoryCommand.java";


    /**
     * Executes the ShowMemoryCommand, displaying the current memory state of the game system.
     * <p>
     * If the command is executed without arguments, a simple overview of the game storage is provided.
     * If an argument is provided, a detailed view starting from the specified position is displayed.
     * </p>
     *
     * @param model            The game system instance.
     * @param commandArguments The arguments provided with the command (optional).
     * @return A CommandResult indicating the success or failure of the command execution and the message to display.
     */
    @Override
    public CommandResult execute(GameSystem model, String[] commandArguments) {

        if (!checkValidArgumentSize(model, commandArguments)) {
            return new CommandResult(CommandResultType.FAILURE, WRONG_ARGUMENT_AMOUNT_MESSAGE);
        }
        if (checkArgumentsExist(commandArguments)) {
            if (!checkValidArgumentType(model, commandArguments)) {
                return new CommandResult(CommandResultType.FAILURE,
                    String.format(WRONG_ARGUMENT_TYPE_FORMAT, SHOW_MEMORY_COMMAND_NAME));
            }
            if (!checkArgumentWithinBounds(model, commandArguments)) {
                return new CommandResult(CommandResultType.FAILURE, ARGUMENT_OUT_OF_BOUNDS_MESSAGE);
            }
        }

        GameStorage gameStorage = model.getGameStorage();
        CircularArrayList<String> gameStorageToString = new CircularArrayList<>(gameStorage.getSize());

        // Standard case (5) : Cell has not been changed yet
        for (int i = 0; i < gameStorage.getSize(); i++) {
            gameStorageToString.add(model.getGeneralAiSymbols()[UNCHANGED_FIELD_SYMBOL_INDEX]);
        }
        //  Case 4 : Cell has been altered by an AI
        for (int i = 0; i < gameStorageToString.size(); i++) {
            GameStorageCell currentCell = gameStorage.getCells().get(i);
            if (!currentCell.getLastChangedBy().isEmpty()) {
                String symbol = getSymbol(model, currentCell.getLastChangedBy());
                gameStorageToString.set(i, symbol);
            }
        }
        // Case 3 : Cell is an 'AI-Bomb'
        for (int i = 0; i < gameStorageToString.size(); i++) {
            GameStorageCell currentCell = gameStorage.getCells().get(i);
            if (!currentCell.getLastChangedBy().isEmpty()) {
                if (isAiBomb(currentCell)) {
                    String bombSymbol = getBombSymbol(model, currentCell.getLastChangedBy());
                    gameStorageToString.set(i, bombSymbol);
                }
            }
        }
        if (!model.getAliveAis().isEmpty()) {
            // Case 2 : Cell will be executed in current cycle
            for (int i : getNextCommandIndexOfOtherAis(model)) {
                gameStorageToString.set(i, model.getGeneralAiSymbols()[NEXT_AIS_SYMBOL_INDEX]);
            }
            // Case 1 : Cell will be executed in next turn
            gameStorageToString.set(getNextCommandIndexOfNextAi(model), model.getGeneralAiSymbols()[CURRENT_AI_SYMBOL_INDEX]);
        }

        String message;
        if (checkArgumentsExist(commandArguments)) {
            message = createDetailedMemory(model, gameStorageToString.toArray(new String[0]),
                commandArguments[DISPLAY_POSITION_INDEX]);
        } else {
            message = String.join("", gameStorageToString);
        }
        return new CommandResult(CommandResultType.SUCCESS, message);
    }

    /**
     * Gets the number of arguments this command expects.
     *
     * @return The number of arguments expected by the command.
     */
    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    /**
     * Indicates whether this command requires the game to be running.
     *
     * @return True if the command requires the game to be running, false otherwise.
     */
    @Override
    public boolean requiredGameStatus() {
        return REQUIRES_GAME_RUNNING;
    }

    /**
     * Provides a description of the command for display to users.
     *
     * @param commandName The name of the command.
     * @return A description of the command.
     */
    @Override
    public String getDescription(String commandName) {
        String message = String.format(DESCRIPTION_MESSAGE, commandName);
        return String.format(DESCRIPTION_FORMAT, commandName, REQUIRES_GAME_RUNNING_MESSAGE, message, OPTIONS_MESSAGE);
    }

    private boolean checkValidArgumentSize(GameSystem model, String[] commandArguments) {
        return commandArguments.length <= MOST_NUMBER_OF_ARGUMENTS;
    }

    private boolean checkValidArgumentType(GameSystem model, String[] commandArguments) {
        try {
            Integer.parseInt(commandArguments[0]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean checkArgumentWithinBounds(GameSystem model, String[] commandArguments) {
        int position = Integer.parseInt(commandArguments[0]);
        return (model.getGameStorage().getSize() - 1) > position && position >= 0;
    }

    private boolean checkArgumentsExist(String[] commandArguments) {
        return commandArguments.length != LEAST_NUMBER_OF_ARGUMENTS;
    }

    private int getNextCommandIndexOfNextAi(GameSystem model) {
        return model.getAliveAis().peekFirst().getPointerIndex();
    }

    private int[] getNextCommandIndexOfOtherAis(GameSystem model) {
        int[] indexArray = new int[model.getAliveAis().size() - 1];
        int counter = 0;
        for (Ai ai : model.getAliveAis()) {
            // skips first entry since it gets a special symbol.
            if (counter != 0) {
                indexArray[counter - 1] = ai.getPointerIndex();
            }
            counter++;
        }
        return indexArray;
    }

    private String getSymbol(GameSystem model, String editorAndId) {
        String[] splittedNameAndId = editorAndId.split(BETWEEN_NAME_AND_ID_PLACEHOLDER);
        String name = splittedNameAndId[NAME_INDEX];
        int id = Integer.parseInt(splittedNameAndId[ID_INDEX]);

        for (Ai ai : model.getInGameAis()) {
            if (ai.getName().equals(name) && ai.getId() == id) {
                return ai.getSymbol();
            }
        }
        return COMMON_ERROR_MESSAGE;
    }

    private boolean isAiBomb(GameStorageCell cell) {
        if (!cell.getWasChangedAfterInitStatus()) {
            return false;
        }
        if (cell.getCommand().equals(STOP_COMMAND_NAME)) {
            return true;
        }
        if (cell.getCommand().equals(JUMP_COMMAND_NAME) && cell.getEntryA() == 0) {
            return true;
        }
        return cell.getCommand().equals(JUMP_CHECK_CELL_COMMAND_NAME) && cell.getEntryA() == 0 && cell.getEntryB() == 0;
    }

    private String getBombSymbol(GameSystem model, String editorAndId) {
        String[] splittedNameAndId = editorAndId.split(BETWEEN_NAME_AND_ID_PLACEHOLDER);
        String name = splittedNameAndId[NAME_INDEX];
        int id = Integer.parseInt(splittedNameAndId[ID_INDEX]);

        for (Ai ai : model.getInGameAis()) {
            if (ai.getName().equals(name) && ai.getId() == id) {
                return ai.getBombSymbol();
            }
        }
        return COMMON_ERROR_MESSAGE;
    }

    private String createDetailedMemory(GameSystem model, String[] simpleView, String argument) {
        GameStorage storage = model.getGameStorage();
        int displayPosition = determineDisplayPosition(storage, argument);
        int rowAmount = determineRowAmount(storage, argument);

        String[] cutSimpleView = ArrayUtil.copyOfRangeCircularArray(simpleView, displayPosition, displayPosition + rowAmount);
        int[] longestEntryPerColumn = new int[STANDARD_DISPLAY_SIZE];
        String[][] memoryTable2D = new String[rowAmount][STANDARD_TABLE_COLUMN_AMOUNT];

        int rowPosition;
        for (int i = 0; i < STANDARD_TABLE_COLUMN_AMOUNT; i++) {
            // Find the longest entity in each column for padding
            for (int j = 0; j < rowAmount; j++) {
                rowPosition = (displayPosition + j) % storage.getSize();
                String entry = switch (i) {
                    case SYMBOL_COLUMN_INDEX -> cutSimpleView[j];
                    case CELL_POSITION_COLUMN_INDEX -> rowPosition + CELL_POSITION_SYMBOL;
                    case COMMAND_NAME_COLUMN_INDEX -> storage.getCells().get(rowPosition).getCommand();
                    case MIDDLE_BAR_FIRST_INDEX, MIDDLE_BAR_SECOND_INDEX -> MIDDLE_BAR;
                    case ENTRY_A_COLUMN_INDEX -> String.valueOf(storage.getCells().get(rowPosition).getEntryA());
                    case ENTRY_B_COLUMN_INDEX -> String.valueOf(storage.getCells().get(rowPosition).getEntryB());
                    default -> "";
                };
                longestEntryPerColumn[i] = Math.max(longestEntryPerColumn[i], entry.length());
            }
            // Prepare padding for each column
            for (int j = 0; j < rowAmount; j++) {
                memoryTable2D[j][i] = String.format(PADDING_FORMAT, longestEntryPerColumn[i]);
            }
            // Insert Table content
            for (int j = 0; j < rowAmount; j++) {
                rowPosition = (displayPosition + j) % storage.getSize();
                String entry = switch (i) {
                    case SYMBOL_COLUMN_INDEX -> cutSimpleView[j];
                    case CELL_POSITION_COLUMN_INDEX -> rowPosition  + CELL_POSITION_SYMBOL;
                    case COMMAND_NAME_COLUMN_INDEX -> storage.getCells().get(rowPosition).getCommand();
                    case MIDDLE_BAR_FIRST_INDEX, MIDDLE_BAR_SECOND_INDEX -> MIDDLE_BAR;
                    case ENTRY_A_COLUMN_INDEX -> String.valueOf(storage.getCells().get(rowPosition).getEntryA());
                    case ENTRY_B_COLUMN_INDEX -> String.valueOf(storage.getCells().get(rowPosition).getEntryB());
                    default -> "";
                };
                memoryTable2D[j][i] = memoryTable2D[j][i].formatted(entry);
            }
        }
        LinkedList<String> message = new LinkedList<>();
        if (model.getGameStorage().getSize() > STANDARD_DISPLAY_SIZE) {
            message.addAll(Arrays.asList(simpleView));
            int offset = displayPosition;
            message.add(offset, model.getGeneralAiSymbols()[SHOW_STORAGE_SYMBOL_INDEX]);
            offset += STANDARD_DISPLAY_SIZE + 1;
            offset = offset % storage.getSize();
            message.add(offset, model.getGeneralAiSymbols()[SHOW_STORAGE_SYMBOL_INDEX]);
        } else {
            message.add(Arrays.toString(cutSimpleView));
        }
        message.add(LINE_BREAK);
        message.add(createDetailedMemory(memoryTable2D));
        return String.join("", message);
    }

    private String createDetailedMemory(String[][] memoryTable2D) {
        StringBuilder memoryTable = new StringBuilder();
        for (String[] strings : memoryTable2D) {
            for (int i = 0; i < strings.length; i++) {
                memoryTable.append(strings[i]);
                // last column doesn't need a space behind it
                if (i != strings.length - 1) {
                    memoryTable.append(COLUMN_PARTITION);
                }
            }
            memoryTable.append(LINE_BREAK);
        }
        memoryTable.replace(memoryTable.length() - LINE_BREAK.length(), memoryTable.length(), "");
        return memoryTable.toString();
    }

    private int determineDisplayPosition(GameStorage gameStorage, String argument) {
        return useStandardDisplaySize(gameStorage) ? Integer.parseInt(argument) : 0;
    }
    private int determineRowAmount(GameStorage gameStorage, String argument) {
        return useStandardDisplaySize(gameStorage) ? STANDARD_DISPLAY_SIZE : gameStorage.getSize();
    }
    private boolean useStandardDisplaySize(GameStorage gameStorage) {
        return gameStorage.getSize() > STANDARD_DISPLAY_SIZE;
    }



    private void printError() {
        System.err.println(COMMON_ERROR_MESSAGE);
    }
}
