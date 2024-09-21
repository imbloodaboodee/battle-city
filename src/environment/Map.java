package environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {

//    public static final int BOARD_WIDTH = 16 * 33;
//    public static final int BOARD_HEIGHT = 16 * 28;
    private static final int ROW_SHIFT = 1;
    private static final int COL_SHIFT = 2;
    private static final int BASE_POS = 14;

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
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6},
            {6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}};


    public static ArrayList<ArrayList<Integer>> readFromFile(String fileName) {
        ArrayList<ArrayList<Integer>> tempMap = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (!currentLine.isEmpty()) {
                    ArrayList<Integer> row = new ArrayList<>();
                    String[] values = currentLine.trim().split("");
                    for (String string : values) {
                        if (!string.isEmpty()) {
                            switch (string) {
                                case "#":
                                    row.add(1);  // Brick
                                    break;
                                case "@":
                                    row.add(2);  // Steel
                                    break;
                                case "%":
                                    row.add(5);  // Base
                                    break;
                                case "~":
                                    row.add(4);  // River
                                    break;
                                default:
                                    row.add(0);  // Blank(.)
                                    break;
                            }
                        }
                    }
                    tempMap.add(row);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading map file: " + e.getMessage());
            e.printStackTrace();
        }
        return tempMap;
    }

    public static int[][] arrayListToArray(ArrayList<ArrayList<Integer>> arrayList) {
        int[][] intArray = arrayList.stream()
                .map(u -> u.stream().mapToInt(i -> i).toArray())
                .toArray(int[][]::new);
        return intArray;
    }

    public static int[][] createNewStageMap(int stage) {
        int[][] newLevel = level0;
        ArrayList<ArrayList<Integer>> levelReadFromFile = readFromFile(
                "stages/" + String.valueOf(stage));
        int[][] array = arrayListToArray(levelReadFromFile);
        for (int i = ROW_SHIFT; i < array.length + ROW_SHIFT; i++) {
            for (int j = COL_SHIFT; j < array[0].length + COL_SHIFT; j++) {
                newLevel[i][j] = array[i - ROW_SHIFT][j - COL_SHIFT];
            }
        }
        newLevel[array.length - 1][BASE_POS] = BlockType.BASE.getValue();
        return newLevel;
    }
}
