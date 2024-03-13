package model.aicommands;

import model.Ai;
import model.GameStorageCell;
import model.GameSystem;

import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;


/**
 * Represents an AI command to swap the values of two cells.
 * <p>
 * This command swaps entry A of a cell 'first' with entry B of a cell 'second'.
 * 'first' is determined by adding the current index to the current cells Entry A.
 * 'second' is determined by adding the current index to the current cells Entry B.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class SwapCommand implements AiCommand {
    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        GameStorageCell currentCell = model.getGameStorage().getCells().get(index);
        int firstIndex = index + currentCell.getEntryA();
        int firstEntryA = model.getGameStorage().getCells().get(firstIndex).getEntryA();
        int secondIndex = index + currentCell.getEntryB();
        int secondEntryB = model.getGameStorage().getCells().get(secondIndex).getEntryB();
        model.getGameStorage().getCells().get(firstIndex).setEntryA(secondEntryB);
        model.getGameStorage().getCells().get(secondIndex).setEntryB(firstEntryA);

        String name = executingAi.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + executingAi.getId();
        model.getGameStorage().getCells().get(firstIndex).postInitChangedBy(name);
        model.getGameStorage().getCells().get(secondIndex).postInitChangedBy(name);
        executingAi.updatePointerIndex();
    }
}
