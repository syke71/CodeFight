
package edu.kit.uenqh.model.aicommands;

import edu.kit.uenqh.model.Ai;
import edu.kit.uenqh.model.GameStorageCell;
import edu.kit.uenqh.model.GameSystem;

import static edu.kit.uenqh.model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;

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

        String name = executingAi.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + executingAi.getId();
        model.getGameStorage().getCells().get(index).postInitChangedBy(name);
        executingAi.updatePointerIndex();
    }
}