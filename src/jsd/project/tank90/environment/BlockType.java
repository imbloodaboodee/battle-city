package jsd.project.tank90.environment;

public enum BlockType {
    BLANK(0), BRICK(1), STEEL(2), BASE(3), RIVER(4), TREE(5), EDGE(6), TANK(7), STAR(
            8), BOMB(9), CLOCK(10), SHOVEL(11), SHIELD(12);
    private final int value;

    private BlockType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BlockType getTypeFromInt(int value) {
        return BlockType.values()[value];
    }
}
