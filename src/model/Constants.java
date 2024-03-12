package model;

/**
 * Provides constants used throughout the application.
 * @author uenqh
 */
public final class Constants {

    /**
     * Prefix for error messages.
     */
    public static final String ERROR_PREFIX = "Error, ";

    /**
     * Format string for reporting wrong number of arguments for a command.
     */
    public static final String WRONG_ARGUMENTS_COUNT_FORMAT = "wrong number of arguments for command '%s'!";

    /**
     * Command name for setting the initialization mode.
     */
    public static final String SET_INIT_MODE_COMMAND_NAME = "set-init-mode";

    /**
     * Command name for starting the game.
     */
    public static final String START_GAME_COMMAND_NAME = "start-game";

    /**
     * Command name for showing memory.
     */
    public static final String SHOW_MEMORY_COMMAND_NAME = "show-memory";

    /**
     * Command name for quitting the game.
     */
    public static final String QUIT_COMMAND_NAME = "quit";

    /**
     * Command name for displaying help information.
     */
    public static final String HELP_COMMAND_NAME = "help";

    /**
     * Command name for adding an AI.
     */
    public static final String ADD_AI_COMMAND_NAME = "add-ai";

    /**
     * Command name for removing an AI.
     */
    public static final String REMOVE_AI_COMMAND_NAME = "remove-ai";

    /**
     * Command name for ending the game.
     */
    public static final String END_GAME_COMMAND_NAME = "end-game";

    /**
     * Command name for executing the next command.
     */
    public static final String NEXT_COMMAND_NAME = "next";

    /**
     * Command name for showing AI information.
     */
    public static final String SHOW_AI_COMMAND_NAME = "show-ai";



    /**
     * Command name for the "JMP" command.
     */
    public static final String JUMP_COMMAND_NAME = "JMP";

    /**
     * Command name for the "JMZ" command.
     */
    public static final String JUMP_CHECK_CELL_COMMAND_NAME = "JMZ";

    /**
     * Command name for the "STOP" command.
     */
    public static final String STOP_COMMAND_NAME = "STOP";

    /**
     * Command name for the "MOV_R" command.
     */
    public static final String MOVE_RELATIVE_COMMAND_NAME = "MOV_R";

    /**
     * Command name for the "MOV_I" command.
     */
    public static final String MOVE_INDIRECT_COMMAND_NAME = "MOV_I";

    /**
     * Command name for the "ADD" command.
     */
    public static final String ADD_COMMAND_NAME = "ADD";

    /**
     * Command name for the "ADD_R" command.
     */
    public static final String ADD_RELATIVE_COMMAND_NAME = "ADD_R";

    /**
     * Command name for the "CMP" command.
     */
    public static final String COMPARE_COMMAND_NAME = "CMP";

    /**
     * Command name for the "SWAP" command.
     */
    public static final String SWAP_COMMAND_NAME = "SWAP";



    /**
     * Index of the symbol representing an unchanged field in memory display.
     */
    public static final int UNCHANGED_FIELD_SYMBOL_INDEX = 0;

    /**
     * Index of the symbol representing the start of memory display.
     */
    public static final int SHOW_STORAGE_SYMBOL_INDEX = 1;

    /**
     * Index of the symbol representing the current AI in memory display.
     */
    public static final int CURRENT_AI_SYMBOL_INDEX = 2;

    /**
     * Index of the symbol representing the next AIs in memory display.
     */
    public static final int NEXT_AIS_SYMBOL_INDEX = 3;
    /**
     * Placeholder used to separate name and ID in certain contexts.
     */
    public static final String BETWEEN_NAME_AND_ID_PLACEHOLDER = "#";

    /**
     * Amount of arguments each AI may have.
     */
    public static final int AMOUNT_OF_ARGUMENTS_PER_AI = 3;

    /**
     * Minimum amount of AIs necessary to start a game.
     */
    public static final int MINIMUM_NUMBER_OF_AIS_PER_GAME = 2;

    /**
     * A String used to append a line break.
     */
    public static final String NEXT_LINE = "\n";

    private Constants() {
    }
}
