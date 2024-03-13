package model.aicommands;

import model.Ai;
import model.GameSystem;

/**
 * Represents an AI command to jump to a specific cell index.
 * <p>
 * This command lets the executing Ai jump to a different cell.
 * The target index is determined by taking the current cell index and adding entry A.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class JumpCommand implements AiCommand {
    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        int jumpDistance = model.getGameStorage().getCells().get(index).getEntryA();
        int targetIndex = modulo((long) index + jumpDistance, model.getGameStorage().getSize());
        executingAi.updatePointerIndex(targetIndex);
    }

    private int modulo(long input, int size) {
        if (input < 0) {
            return (int) (input % size) + size;
        }
        return (int) (input % size);
    }
}
