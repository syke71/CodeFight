package model;

import model.aicommands.AiCommandHandler;
import usercommands.CommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static model.Constants.BETWEEN_NAME_AND_ID_PLACEHOLDER;
import static model.Constants.CURRENT_AI_SYMBOL_INDEX;
import static model.Constants.NEXT_AIS_SYMBOL_INDEX;
import static model.Constants.SHOW_STORAGE_SYMBOL_INDEX;
import static model.Constants.UNCHANGED_FIELD_SYMBOL_INDEX;

/**
 * Represents the game system which manages the game's state, AI players, and commands.
 * <p>
 * The GameSystem class controls the game state, including the initialization of the game storage,
 * starting and resetting the game, adding and removing AI players, and managing game commands.
 * </p>
 *
 * @author uenqh
 */
public class GameSystem {

    private static final InitMode STANDARD_INIT_MODE = InitMode.INIT_MODE_STOP;
    private static final int STANDARD_SEED = 0;
    private static final int AMOUNT_OF_SPECIFIC_AI_SYMBOLS_PER_AI = 2;
    private static final int AMOUNT_OF_GENERAL_AI_SYMBOLS = 4;
    private static final int AI_STANDARD_SYMBOL_INDEX = 0;
    private static final int AI_BOMB_SYMBOL_INDEX = 1;
    private final Map<String, Ai> aiMap;
    private final ArrayList<Ai> inGameAis;
    private final LinkedList<Ai> aliveAis;
    private final CommandHandler commandHandler;
    private final AiCommandHandler aiCommandHandler;
    private GameStorage gameStorage;
    private InitMode initMode;
    private boolean gameStarted;
    private int seed;
    private final int maxAmountOfAis;
    private final String[] specificAiSymbols;
    private final String[] generalAiSymbols;

    /**
     * Constructs a GameSystem object with the specified parameters.
     *
     * @param gameStorageSize     The size of the game storage.
     * @param aiIndependentSymbols The symbols independent of specific AI players.
     * @param aiSpecificSymbols   The symbols specific to each AI player.
     */
    public GameSystem(int gameStorageSize, String[] aiIndependentSymbols, String[] aiSpecificSymbols) {

        this.generalAiSymbols = new String[AMOUNT_OF_GENERAL_AI_SYMBOLS];
        this.generalAiSymbols[UNCHANGED_FIELD_SYMBOL_INDEX] = aiIndependentSymbols[UNCHANGED_FIELD_SYMBOL_INDEX];
        this.generalAiSymbols[SHOW_STORAGE_SYMBOL_INDEX] = aiIndependentSymbols[SHOW_STORAGE_SYMBOL_INDEX];
        this.generalAiSymbols[CURRENT_AI_SYMBOL_INDEX] = aiIndependentSymbols[CURRENT_AI_SYMBOL_INDEX];
        this.generalAiSymbols[NEXT_AIS_SYMBOL_INDEX] = aiIndependentSymbols[NEXT_AIS_SYMBOL_INDEX];
        this.specificAiSymbols = Arrays.copyOf(aiSpecificSymbols, aiSpecificSymbols.length);
        this.maxAmountOfAis = aiSpecificSymbols.length / AMOUNT_OF_SPECIFIC_AI_SYMBOLS_PER_AI;

        this.aiMap = new HashMap<>();
        this.inGameAis = new ArrayList<>();
        this.aliveAis = new LinkedList<>();
        this.gameStorage = new GameStorage(gameStorageSize);
        this.commandHandler = new CommandHandler(this);
        this.aiCommandHandler = new AiCommandHandler(this);
        this.gameStarted = false;
        this.seed = STANDARD_SEED;
        this.initMode = STANDARD_INIT_MODE;
        this.formatGameStorage();
    }

    /**
     * Starts the game with the specified AI players.
     *
     * @param ais An array of AI player names.
     */
    public void startGame(String[] ais) {
        this.toggleGameStatus();
        this.formatGameStorage();
        this.loadAiArray(ais);
        this.loadAiArgumentsIntoGameStorage();
    }

    private void loadAiArgumentsIntoGameStorage() {
        int storageSize = this.gameStorage.getSize();
        int numberOfAis = this.inGameAis.size();
        int currentPosition;
        Ai currentAi;

        for (int i = 0; i < this.inGameAis.size(); i++) {
            currentAi = inGameAis.get(i);
            currentPosition = (storageSize / numberOfAis) * i;
            currentAi.updatePointerIndex(currentPosition);

            for (int j = 0; j < currentAi.getArguments().size(); j++) {
                this.gameStorage.getCells().get(currentPosition + j).setCommand(currentAi.getArguments().get(j).getCommand());
                this.gameStorage.getCells().get(currentPosition + j).setEntryA(currentAi.getArguments().get(j).getEntryA());
                this.gameStorage.getCells().get(currentPosition + j).setEntryB(currentAi.getArguments().get(j).getEntryB());
                this.gameStorage.getCells().get(currentPosition + j).changedBy(currentAi.getName()
                    + BETWEEN_NAME_AND_ID_PLACEHOLDER
                    + currentAi.getId());
            }
        }
    }

    private void loadAiArray(String[] ais) {
        for (String ai : ais) {
            loadAi(ai);
        }
        checkDuplicateInGameAis();
    }
    private void loadAi(String name) {
        if (this.aiMap.containsKey(name)) {
            Ai clone = aiMap.get(name).copy();
            int inGameAiNumber = inGameAis.size();
            clone.setSymbol(getSymbols(inGameAiNumber)[AI_STANDARD_SYMBOL_INDEX]);
            clone.setBombSymbol(getSymbols(inGameAiNumber)[AI_BOMB_SYMBOL_INDEX]);
            clone.toggleAliveStatus();
            this.inGameAis.add(clone);
            this.aliveAis.add(clone);
        }
    }

    private String[] getSymbols(int inGameAiNumber) {
        String[] symbols = new String[2];

        int index = inGameAiNumber * AMOUNT_OF_SPECIFIC_AI_SYMBOLS_PER_AI + AI_STANDARD_SYMBOL_INDEX;
        symbols[AI_STANDARD_SYMBOL_INDEX] = this.specificAiSymbols[index];

        index = inGameAiNumber * AMOUNT_OF_SPECIFIC_AI_SYMBOLS_PER_AI + AI_BOMB_SYMBOL_INDEX;
        symbols[AI_BOMB_SYMBOL_INDEX] = this.specificAiSymbols[index];

        return symbols;
    }

    private void checkDuplicateInGameAis() {
        Map<String, Integer> counts = new HashMap<>();
        for (Ai ai : this.inGameAis) {
            int count = counts.getOrDefault(ai.getName(), 0);
            counts.put(ai.getName(), count + 1);
        }
        for (Ai ai : this.inGameAis) {
            int count = counts.get(ai.getName());
            if (count == 1) {
                counts.remove(ai.getName());
            }
        }

        for (int i = this.inGameAis.size() - 1; i >= 0; i--) {
            int count = counts.getOrDefault(this.inGameAis.get(i).getName(), 0);
            if (count > 0) {
                count--;
                this.inGameAis.get(i).setId(count);
                counts.put(this.inGameAis.get(i).getName(), count);
            }
        }
    }

    private void clearInGameAis() {
        this.inGameAis.clear();
        this.aliveAis.clear();
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        this.toggleGameStatus();
        this.clearInGameAis();
        this.gameStorage = new GameStorage(this.gameStorage.getSize());
        this.setInitMode(STANDARD_INIT_MODE);
        this.formatGameStorage();
    }

    private void formatGameStorage() {
        GameStorageInitializer initializer = new GameStorageInitializer(this.seed);
        initializer.format(this);
    }

    /**
     * Adds an AI player to the game.
     *
     * @param ai The AI player to add.
     */
    public void addAi(Ai ai) {
        this.aiMap.put(ai.getName(), ai);
    }

    /**
     * Removes an AI player from the game.
     *
     * @param name The name of the AI player to remove.
     */
    public void removeAi(String name) {
        this.aiMap.remove(name);
    }

    /**
     * Sets the initialization mode of the game.
     *
     * @param initMode The initialization mode to set.
     */
    public void setInitMode(InitMode initMode) {
        this.initMode = initMode;
    }

    /**
     * Retrieves the command handler associated with the game system.
     *
     * @return The command handler.
     */
    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    /**
     * Retrieves the AI command handler associated with the game system.
     *
     * @return The AI command handler.
     */
    public AiCommandHandler getAiCommandHandler() {
        return this.aiCommandHandler;
    }

    /**
     * Retrieves the game storage.
     *
     * @return The game storage.
     */
    public GameStorage getGameStorage() {
        return this.gameStorage;
    }

    /**
     * Retrieves the initialization mode of the game.
     *
     * @return The initialization mode.
     */
    public InitMode getInitMode() {
        return this.initMode;
    }

    /**
     * Retrieves the maximum amount of AI players allowed in the game.
     *
     * @return The maximum amount of AI players.
     */
    public int getMaxAmountOfAis() {
        return this.maxAmountOfAis;
    }

    /**
     * Retrieves the seed used for randomization in the game.
     *
     * @return The seed value.
     */
    public int getSeed() {
        return this.seed;
    }

    /**
     * Sets the seed used for randomization in the game.
     *
     * @param newSeed The new seed value.
     */
    public void setSeed(int newSeed) {
        this.seed = newSeed;
    }

    /**
     * Retrieves the map of AI players associated with their names.
     *
     * @return The map of AI players.
     */
    public Map<String, Ai> getAiMap() {
        return this.aiMap;
    }

    /**
     * Retrieves the list of AI players currently in the game.
     *
     * @return The list of AI players.
     */
    public ArrayList<Ai> getInGameAis() {
        return this.inGameAis;
    }

    /**
     * Retrieves the list of AI players currently alive in the game.
     *
     * @return The list of alive AI players.
     */
    public LinkedList<Ai> getAliveAis() {
        return this.aliveAis;
    }

    /**
     * Retrieves the status of the game.
     *
     * @return True if the game is started, false otherwise.
     */
    public boolean getGameStatus() {
        return this.gameStarted;
    }

    /**
     * Toggles the status of the game (started or stopped).
     */
    public void toggleGameStatus() {
        this.gameStarted = !this.gameStarted;
    }

    /**
     * Retrieves the array of general AI symbols.
     *
     * @return The array of general AI symbols.
     */
    public String[] getGeneralAiSymbols() {
        return this.generalAiSymbols;
    }
}
