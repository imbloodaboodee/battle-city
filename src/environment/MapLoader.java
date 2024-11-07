package environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapLoader {
    private static final int ROW_SHIFT = 1;
    private static final int COL_SHIFT = 2;
    private static final int BASE_POS = 14;

    private static final Map<Character, Integer> CHAR_TO_BLOCK_MAP = new HashMap<>();
    static {
        CHAR_TO_BLOCK_MAP.put('#', 1);  // Brick
        CHAR_TO_BLOCK_MAP.put('@', 2);  // Steel
        CHAR_TO_BLOCK_MAP.put('%', 5);  // Base
        CHAR_TO_BLOCK_MAP.put('~', 4);  // River
        CHAR_TO_BLOCK_MAP.put('.', 0);  // Blank
    }

    public static int[][] getMap(int stage) {
        return createNewStageMap(stage);
    }

    public static final int[][] level0 = {
            {6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 6, 6, 6, 6},
            {6, 6, 2, 2, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 2, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}};


    public static int[][] readMapFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            return br.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(MapLoader::parseLineToRow)
                    .toArray(int[][]::new);
        } catch (IOException e) {
            System.err.println("Error reading map file: " + e.getMessage());
            e.printStackTrace();
            return level0; // Fallback to default map if there's an error
        }
    }

    private static int[] parseLineToRow(String line) {
        return line.trim().chars()
                .map(c -> CHAR_TO_BLOCK_MAP.getOrDefault((char) c, 0)) // Map each character to its block type
                .toArray();
    }

    public static int[][] createNewStageMap(int stage) {
        int[][] baseLevel = deepCopyArray(level0);
        int[][] fileLevel = readMapFromFile("./src/assets/stages/" + stage);

        for (int i = 0; i < fileLevel.length; i++) {
            for (int j = 0; j < fileLevel[i].length; j++) {
                baseLevel[i + ROW_SHIFT][j + COL_SHIFT] = fileLevel[i][j];
            }
        }

        baseLevel[fileLevel.length - 1][BASE_POS] = BlockType.BASE.getValue();
        return baseLevel;
    }

    private static int[][] deepCopyArray(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }
}
