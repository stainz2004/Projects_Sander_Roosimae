package ee.taltech.examplegame.server.game;

public class CollisionCategories {
    public static final short NOTHING_BIT = 0;
    public static final short PLAYER_BIT = 0x0001; // 0000000000000001
    public static final short GROUND_BIT = 0x0002;  // 0000000000000010
    public static final short OBJECT_BIT = 0x0004;  // 0000000000000100
}
