package model;


/**
 * Provides constant error messages used in the application.
 * @author uenqh
 */
public final class ConstantErrorMessages {

    /**
     * Error message indicating that a command can only be used while the game is running.
     */
    public static final String REQUIRES_GAME_RUNNING_MESSAGE = "This command can only be used while the game is running.";

    /**
     * Error message indicating that a command can only be used while the game is stopped.
     */
    public static final String REQUIRES_GAME_STOPPED_MESSAGE = "This command can only be used while the game is stopped.";

    /**
     * Error message indicating that a command can always be used.
     */
    public static final String NO_RUNNING_REQUIREMENTS_MESSAGE = "This command can always be used.";

    private ConstantErrorMessages() {
    }
}
