package model.aicommands;

import model.Ai;
import model.GameStorageCell;
import model.GameSystem;

import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;


/**
 * Represents an AI command to move the contents of one cell to another indirectly specified cell.
 * <p>
 * This command determines the source by adding the current cell Index to the entry value A of the same cell.
 * The Intermediate is determined by adding the current cell Index to the entry value B of the same cell.
 * The target index is based on the entry value B of the intermediate cell.
 * The contents (command, entry A, and entry B) of the source cell are then copied to the target cell.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class MoveIndirectCommand implements AiCommand {
    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        GameStorageCell currentCell = model.getGameStorage().getCells().get(index);
        GameStorageCell source = model.getGameStorage().getCells().get(index + currentCell.getEntryA());
        int intermediateIndex = index + currentCell.getEntryB();
        GameStorageCell intermediate = model.getGameStorage().getCells().get(intermediateIndex);
        int targetIndex = intermediateIndex + intermediate.getEntryB();
        model.getGameStorage().getCells().get(targetIndex).setCommand(source.getCommand());
        model.getGameStorage().getCells().get(targetIndex).setEntryA(source.getEntryA());
        model.getGameStorage().getCells().get(targetIndex).setEntryB(source.getEntryB());

        String name = executingAi.getName() + BETWEEN_NAME_AND_ID_PLACEHOLDER + executingAi.getId();
        model.getGameStorage().getCells().get(targetIndex).postInitChangedBy(name);
        executingAi.updatePointerIndex();
    }
}
