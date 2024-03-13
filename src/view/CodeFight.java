package view;

import model.GameSystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static model.Constants.ERROR_PREFIX;

/**
 * Entry point for the CodeFight game.
 * <p>
 * This class contains the main method to start the CodeFight game. It validates the startup arguments,
 * initializes the game system, and handles user input.
 * </p>
 * <p>
 * The startup arguments should include the storage size and AI-specific tokens. The storage size must be
 * within a specific range defined by {@code MIN_STORAGE_SIZE} and {@code MAX_STORAGE_SIZE}.
 * </p>
 * <p>
 * Upon successful initialization, the game system is created, and the user is prompted with a welcome message.
 * </p>
 *
 * @author uenqh
 */
public final class CodeFight {

    private static final int MIN_AMOUNT_OF_ARGUMENTS = 9;
    private static final int STORAGE_SIZE_INDEX = 0;
    private static final int MIN_STORAGE_SIZE = 7;
    private static final int MAX_STORAGE_SIZE = 1337;
    private static final int AI_INDEPENDENT_TOKEN_AMOUNT_START_INDEX = 5;
    private static final String INVALID_STARTUP_ARGUMENTS = "the entered start up arguments are invalid!";
    private static final String START_UP_MESSAGE = "Welcome to CodeFight 2024. Enter 'help' for more details.";

    private CodeFight() {

    }

    /**
     * Main method to start the CodeFight game.
     *
     * @param args the startup arguments including storage size and AI-specific tokens
     */
    public static void main(String[] args) {
        if (checkValidArgsFormat(args)) {
            System.out.println(START_UP_MESSAGE);
            int storageSize = Integer.parseInt(args[STORAGE_SIZE_INDEX]);
            String[] aiIndependentTokens = Arrays.copyOfRange(args, 1, AI_INDEPENDENT_TOKEN_AMOUNT_START_INDEX);
            String[] aiSpecificTokens = Arrays.copyOfRange(args, AI_INDEPENDENT_TOKEN_AMOUNT_START_INDEX, args.length);

            final GameSystem gameSystem = new GameSystem(storageSize, aiIndependentTokens, aiSpecificTokens);
            gameSystem.getCommandHandler().handleUserInput();

        } else {
            System.out.println(ERROR_PREFIX + INVALID_STARTUP_ARGUMENTS);
        }
    }

    private static boolean checkValidArgsFormat(String[] args) {
        // Check for minimum amount of arguments
        if (args.length < MIN_AMOUNT_OF_ARGUMENTS) {
            return false;
        }
        // args.length may never be even
        if (args.length % 2 == 0) {
            return false;
        }
        // Check for valid storage size input
        int storageSize;
        try {
            storageSize = Integer.parseInt(args[STORAGE_SIZE_INDEX]);
        } catch (NumberFormatException e) {
            return false;
        }
        if (!areAllStringsUnique(args)) {
            return false;
        }
        return storageSize >= MIN_STORAGE_SIZE && storageSize <= MAX_STORAGE_SIZE;
    }

    private static boolean areAllStringsUnique(String[] array) {

        Set<String> seenStrings = new HashSet<>();

        for (String str : array) {
            if (seenStrings.contains(str)) {
                return false;
            }
            seenStrings.add(str);
        }

        return true;
    }
}
