package usercommands;

import model.GameSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static model.Constants.ADD_AI_COMMAND_NAME;
import static model.Constants.END_GAME_COMMAND_NAME;
import static model.Constants.ERROR_PREFIX;
import static model.Constants.HELP_COMMAND_NAME;
import static model.Constants.NEXT_COMMAND_NAME;
import static model.Constants.QUIT_COMMAND_NAME;
import static model.Constants.REMOVE_AI_COMMAND_NAME;
import static model.Constants.SET_INIT_MODE_COMMAND_NAME;
import static model.Constants.SHOW_AI_COMMAND_NAME;
import static model.Constants.SHOW_MEMORY_COMMAND_NAME;
import static model.Constants.START_GAME_COMMAND_NAME;
import static model.Constants.WRONG_ARGUMENTS_COUNT_FORMAT;

/**
 * This class handles the user input and executes the commands.
 *
 * @author Programmieren-Team
 * @author uenqh
 */
public class CommandHandler {

    private static final String COMMAND_SEPARATOR_REGEX = " ";
    private static final String COMMAND_NOT_FOUND_FORMAT = "command '%s' not found!";
    private static final String GAME_MUST_BE_RUNNING_FORMAT = "the game must be running to use the command '%s'!";
    private static final String GAME_MUST_BE_STOPPED_FORMAT = "the game must be stopped to use the command '%s'!";
    private final GameSystem gameSystem;
    private final Map<String, Command> commands;
    private final ArrayList<String> commandsList;
    private boolean running = false;

    /**
     * Constructs a new CommandHandler.
     *
     * @param gameSystem the game system that this instance manages
     */
    public CommandHandler(GameSystem gameSystem) {
        this.gameSystem = Objects.requireNonNull(gameSystem);
        this.commands = new HashMap<>();
        this.commandsList = new ArrayList<>();
        this.initCommands();
    }

    /**
     * Starts the interaction with the user.
     */
    public void handleUserInput() {
        this.running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        }
    }

    /**
     * Quits the interaction with the user.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);

        executeCommand(commandName, commandArguments);
    }

    private void executeCommand(String commandName, String[] commandArguments) {
        if (!commands.containsKey(commandName)) {
            System.err.printf(ERROR_PREFIX + COMMAND_NOT_FOUND_FORMAT + "%n", commandName);
        } else if (commands.get(commandName).getNumberOfArguments() != commandArguments.length
            && commands.get(commandName).getNumberOfArguments() != -1) {
            System.err.printf(ERROR_PREFIX + WRONG_ARGUMENTS_COUNT_FORMAT + "%n", commandName);
        } else if (!(commandName.equals(HELP_COMMAND_NAME) || commandName.equals(QUIT_COMMAND_NAME))
            && !doRunRequirementsMatch(commandName)) {
            if (gameSystem.getGameStatus()) {
                System.err.printf(ERROR_PREFIX + GAME_MUST_BE_STOPPED_FORMAT + "%n", commandName);
            } else {
                System.err.printf(ERROR_PREFIX + GAME_MUST_BE_RUNNING_FORMAT + "%n", commandName);
            }
        } else {
            CommandResult result = commands.get(commandName).execute(gameSystem, commandArguments);
            String output = switch (result.getType()) {
                case SUCCESS -> result.getMessage();
                case FAILURE -> ERROR_PREFIX + result.getMessage();
            };
            if (output != null) {
                switch (result.getType()) {
                    case SUCCESS -> System.out.println(output);
                    case FAILURE -> System.err.println(output);
                    default -> throw new IllegalStateException("Unexpected value: " + result.getType());
                }
            }
        }
    }

    private boolean doRunRequirementsMatch(String commandName) {
        return commands.get(commandName).requiredGameStatus() == gameSystem.getGameStatus();
    }

    private void initCommands() {
        this.addCommand(ADD_AI_COMMAND_NAME, new AddAiCommand());
        this.addCommand(REMOVE_AI_COMMAND_NAME, new RemoveAiCommand());
        this.addCommand(SET_INIT_MODE_COMMAND_NAME, new SetInitModeCommand());
        this.addCommand(START_GAME_COMMAND_NAME, new StartGameCommand());
        this.addCommand(END_GAME_COMMAND_NAME, new EndGameCommand());
        this.addCommand(NEXT_COMMAND_NAME, new NextCommand());
        this.addCommand(SHOW_MEMORY_COMMAND_NAME, new ShowMemoryCommand());
        this.addCommand(SHOW_AI_COMMAND_NAME, new ShowAiCommand());
        this.addCommand(QUIT_COMMAND_NAME, new QuitCommand());
        this.addCommand(HELP_COMMAND_NAME, new HelpCommand());

        this.commandsList.sort(Comparator.naturalOrder());
    }
    private void addCommand(String commandName, Command command) {
        this.commands.put(commandName, command);
        this.commandsList.add(commandName);
    }

    /**
     * Retrieves the list of available commands.
     *
     * @return The list of available commands.
     */
    public ArrayList<String> getCommandsList() {
        return this.commandsList;
    }

    /**
     * Retrieves the map of commands.
     *
     * @return The map of commands.
     */
    public Map<String, Command> getCommandsMap() {
        return this.commands;
    }
}
