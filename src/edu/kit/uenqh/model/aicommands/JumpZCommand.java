package edu.kit.uenqh.model.aicommands;

import edu.kit.uenqh.model.Ai;
import edu.kit.uenqh.model.GameStorageCell;
import edu.kit.uenqh.model.GameSystem;

/**
 * Represents an AI command to jump to a specific cell index if a condition is met.
 * <p>
 * This command compares a so called checkCell's entry B with the COMPARING_AMOUNT constant
 * to determine if a JUMP command will be executed.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class JumpZCommand implements AiCommand {
    private static final int COMPARING_AMOUNT = 0;
    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        GameStorageCell currentCell = model.getGameStorage().getCells().get(index);
        GameStorageCell checkCell = model.getGameStorage().getCells().get(index + currentCell.getEntryB());
        if (checkCell.getEntryB() == COMPARING_AMOUNT) {
            new JumpCommand().execute(model, executingAi);
        } else {
            executingAi.updatePointerIndex();
        }
    }
}