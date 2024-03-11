package model.aicommands;

import model.Ai;
import model.GameSystem;

/**
 * Represents an AI command to stop the execution of an AI.
 * <p>
 * This command toggles the alive status of the executing AI, stopping it from further participating in the game.
 * </p>
 *
 * @author uenqh
 */
public class StopCommand implements AiCommand {
    @Override
    public void execute(GameSystem model, Ai executingAi) {
        executingAi.toggleAliveStatus();
    }
}
