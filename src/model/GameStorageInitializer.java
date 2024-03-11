package model;

import java.util.Random;

import static model.Constants.STOP_COMMAND_NAME;


/**
 * Initializes the game storage with specific commands or random values.
 * <p>
 * The GameStorageInitializer class is responsible for formatting the game storage with either
 * a specific command or random values for each cell.
 * </p>
 *
 * @author uenqh
 */
public class GameStorageInitializer {

    private static final int STANDARD_ENTRY = 0;
    private static final String COMMON_ERROR_MESSAGE = "Unexpected error in GameStorageInitializer.java";
    private final Random numberGenerator;

    /**
     * Constructs a GameStorageInitializer object with the specified seed for random number generation.
     *
     * @param seed The seed for random number generation.
     */
    public GameStorageInitializer(int seed) {
        this.numberGenerator = new Random(seed);
    }

    /**
     * Formats the game storage based on the provided mode.
     *
     * @param model The GameSystem object representing the game model.
     */
    public void format(GameSystem model) {
        InitMode initMode = model.getInitMode();

        switch (initMode) {
            case INIT_MODE_STOP:
                formatStopGameStorage(model);
                break;
            case INIT_MODE_RANDOM:
                formatRandomGameStorage(model);
                break;
            default:
                System.err.println(COMMON_ERROR_MESSAGE);
                break;
        }
    }

    private void formatStopGameStorage(GameSystem model) {
        for (GameStorageCell cell : model.getGameStorage().getCells()) {
            cell.setCommand(STOP_COMMAND_NAME);
            cell.setEntryA(STANDARD_ENTRY);
            cell.setEntryB(STANDARD_ENTRY);
        }
    }

    private void formatRandomGameStorage(GameSystem model) {
        int bound = model.getAiCommandHandler().getAiCommandNameArrayList().size();
        int random;

        for (GameStorageCell cell : model.getGameStorage().getCells()) {
            random = this.numberGenerator.nextInt(bound);
            cell.setCommand(model.getAiCommandHandler().getAiCommandNameArrayList().get(random));

            random = this.numberGenerator.nextInt(bound);
            cell.setEntryA(random);

            random = this.numberGenerator.nextInt(bound);
            cell.setEntryB(random);
        }
    }
}
