
package model.aicommands;

import model.Ai;
import model.GameStorageCell;
import model.GameSystem;

/**
 * Represents an AI command to add two values.
 * <p>
 * This command retrieves the current cell pointed to by the executing AI, adds the values in entry A and B,
 * and updates entry B with the result.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class AddCommand implements AiCommand {

    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        GameStorageCell cell = model.getGameStorage().getCells().get(index);
        int result = cell.getEntryA() + cell.getEntryB();
        model.getGameStorage().getCells().get(index).setEntryB(result);

        model.getGameStorage().getCells().get(index).postInitChangedBy(executingAi.getName());
        executingAi.updatePointerIndex();
    }
}
