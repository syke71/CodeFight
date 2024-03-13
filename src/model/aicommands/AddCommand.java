
package model.aicommands;

import model.Ai;
import model.GameStorageCell;
import model.GameSystem;

import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;

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
        int result = modulo((long) cell.getEntryA() + cell.getEntryB(), model.getGameStorage().getSize());
        model.getGameStorage().getCells().get(index).setEntryB(result);

        String name = executingAi.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + executingAi.getId();
        model.getGameStorage().getCells().get(index).postInitChangedBy(name);
        executingAi.updatePointerIndex();
    }

    private int modulo(long input, int size) {
        if (input < 0) {
            return (int) (input % size) + size;
        }
        return (int) (input % size);
    }

}
