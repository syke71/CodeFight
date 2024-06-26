package model.aicommands;

import model.Ai;
import model.GameStorageCell;
import model.GameSystem;

import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;

/**
 * Represents an AI command to add a value to the entry of a cell relative to the current AI pointer.
 * <p>
 * This Command determines the target by taking the current cells index and adding entry B.
 * The new value is determined by taking the targets entry B value and adding the current cells entry A.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class AddRelativeCommand implements AiCommand {

    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        GameStorageCell source = model.getGameStorage().getCells().get(index);
        int targetIndex = index + source.getEntryB();
        int result = source.getEntryA() + model.getGameStorage().getCells().get(targetIndex).getEntryB();
        model.getGameStorage().getCells().get(targetIndex).setEntryB(result);

        String name = executingAi.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + executingAi.getId();
        model.getGameStorage().getCells().get(targetIndex).postInitChangedBy(name);
        executingAi.updatePointerIndex();
    }
}
