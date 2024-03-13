package edu.kit.uenqh.model.aicommands;

import edu.kit.uenqh.model.Ai;
import edu.kit.uenqh.model.GameSystem;

/**
 * Represents a command to be executed by an AI in the game system.
 * <p>
 * Implementations of this interface define specific actions to be performed by an AI when executed in the game system.
 * Each command receives the game system and the executing AI as parameters and performs its designated task.
 * </p>
 *
 * @author uenqh
 */
public interface AiCommand {

    /**
     * Executes the command, performing its designated task on the game system using the executing AI.
     *
     * @param model        the game system on which the command is executed
     * @param executingAi  the AI executing the command
     */
    void execute(GameSystem model, Ai executingAi);
}
