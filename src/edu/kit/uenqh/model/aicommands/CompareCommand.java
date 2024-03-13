package edu.kit.uenqh.model.aicommands;

import edu.kit.uenqh.model.Ai;
import edu.kit.uenqh.model.GameStorageCell;
import edu.kit.uenqh.model.GameSystem;


/**
 * Represents an AI command to compare the entry values of two cells.
 * <p>
 * This command compares an entry A from a cell 'first' to an entry B from a cell 'second'.
 * 'first' is determined by adding the current index to the current cells Entry A.
 * 'second' is determined by adding the current index to the current cells Entry B.
 * If the entries do not match, then the executing AI skips the next AI-Command.
 * Additionally, the command history of the target cell is updated to reflect the change made by the executing AI.
 * </p>
 *
 * @author uenqh
 */
public class CompareCommand implements AiCommand {
    @Override
    public void execute(GameSystem model, Ai executingAi) {
        int index = executingAi.getPointerIndex();
        GameStorageCell currentCell = model.getGameStorage().getCells().get(index);
        GameStorageCell first = model.getGameStorage().getCells().get(index + currentCell.getEntryA());
        GameStorageCell second = model.getGameStorage().getCells().get(index + currentCell.getEntryB());

        if (first.getEntryA() != second.getEntryB()) {
            executingAi.updatePointerIndex();
        }
        executingAi.updatePointerIndex();
    }
}
