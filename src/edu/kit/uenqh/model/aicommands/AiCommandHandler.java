package edu.kit.uenqh.model.aicommands;

import edu.kit.uenqh.model.Ai;
import edu.kit.uenqh.model.GameSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static edu.kit.uenqh.model.Constants.ADD_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.ADD_RELATIVE_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.COMPARE_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.JUMP_CHECK_CELL_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.JUMP_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.MOVE_INDIRECT_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.MOVE_RELATIVE_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.STOP_COMMAND_NAME;
import static edu.kit.uenqh.model.Constants.SWAP_COMMAND_NAME;

/**
 * Handles the execution of AI commands in the game system.
 * <p>
 * This class manages the execution of various AI commands within the game system. It maintains a mapping of command names
 * to their corresponding implementations and provides methods to execute commands based on the AI's current pointer index.
 * </p>
 *
 * @author uenqh
 */
public class AiCommandHandler {


    // Instance variables
    private final GameSystem gameSystem;
    private final Map<String, AiCommand> aiCommandMap;
    private final ArrayList<String> aiCommandNameArrayList;

    /**
     * Constructs an AiCommandHandler with the specified game system.
     *
     * @param gameSystem The game system to associate with this command handler.
     * @throws NullPointerException if the game system is null.
     */
    public AiCommandHandler(GameSystem gameSystem) {
        this.gameSystem = Objects.requireNonNull(gameSystem);
        this.aiCommandMap = new HashMap<>();
        this.aiCommandNameArrayList = new ArrayList<>();
        this.initAiCommands();
    }

    // Initializes the mapping of command names to their implementations
    private void initAiCommands() {
        this.addCommand(STOP_COMMAND_NAME, new StopCommand());
        this.addCommand(MOVE_RELATIVE_COMMAND_NAME, new MoveRelativeCommand());
        this.addCommand(MOVE_INDIRECT_COMMAND_NAME, new MoveIndirectCommand());
        this.addCommand(ADD_COMMAND_NAME, new AddCommand());
        this.addCommand(ADD_RELATIVE_COMMAND_NAME, new AddRelativeCommand());
        this.addCommand(JUMP_COMMAND_NAME, new JumpCommand());
        this.addCommand(JUMP_CHECK_CELL_COMMAND_NAME, new JumpZCommand());
        this.addCommand(COMPARE_COMMAND_NAME, new CompareCommand());
        this.addCommand(SWAP_COMMAND_NAME, new SwapCommand());
    }

    // Adds a command to the mapping
    private void addCommand(String name, AiCommand command) {
        this.aiCommandMap.put(name, command);
        this.aiCommandNameArrayList.add(name);
    }

    /**
     * Gets the mapping of command names to their implementations.
     *
     * @return The mapping of command names to their implementations.
     */
    public Map<String, AiCommand> getAiCommandMap() {
        return this.aiCommandMap;
    }

    /**
     * Gets the list of command names.
     *
     * @return The list of command names.
     */
    public ArrayList<String> getAiCommandNameArrayList() {
        return this.aiCommandNameArrayList;
    }

    /**
     * Executes the command pointed to by the specified AI.
     *
     * @param executingAi The AI executing the command.
     */
    public void execute(Ai executingAi) {
        int pointer = executingAi.getPointerIndex();
        String command = this.gameSystem.getGameStorage().getCells().get(pointer).getCommand();
        this.getAiCommandMap().get(command).execute(this.gameSystem, executingAi);
    }

}
