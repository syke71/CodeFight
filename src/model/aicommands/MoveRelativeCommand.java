package model.aicommands;

import model.Ai;
import model.GameStorageCell;
import model.GameSystem;

import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;



/**
 * Represents an AI command to move the contents of one cell to another cell relative to the current position.
 * <p>
 * This command the source whose index is determined by adding the entry value A of the cell to the current index.
 * The target index is calculated by adding the entry value B of the current cell to the current index.
 * The contents (command, entry A, and entry B) of the source cell are then copied to the target cell.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class MoveRelativeCommand implements AiCommand {
    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        GameStorageCell currentCell = model.getGameStorage().getCells().get(index);

        GameStorageCell source = model.getGameStorage().getCells().get(index + (long) currentCell.getEntryA());

        long targetIndex = index + (long)  currentCell.getEntryB();
        model.getGameStorage().getCells().get(targetIndex).setCommand(source.getCommand());
        model.getGameStorage().getCells().get(targetIndex).setEntryA(source.getEntryA());
        model.getGameStorage().getCells().get(targetIndex).setEntryB(source.getEntryB());

        String name = executingAi.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + executingAi.getId();
        model.getGameStorage().getCells().get(targetIndex).postInitChangedBy(name);
        executingAi.updatePointerIndex();
    }
}
