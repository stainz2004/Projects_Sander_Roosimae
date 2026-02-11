package ee.taltech.examplegame.server.Ai;

import lombok.Getter;

import java.util.*;

/**
 * The type A star.
 */
@Getter
public class AStar {

    private final int maxX;
    private final int maxY;
    private final int[][] grid;
    private final int[][] neighbours = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    /**
     * Instantiates a new A star.
     *
     * @param grid the grid
     */
    public AStar(int[][] grid) {
        this.grid = grid;
        this.maxX = grid[0].length;
        this.maxY = grid.length;
    }

    /**
     * The type Node.
     */
    public class Node {
        public int x;
        public int y;
        int gScore;
        int hScore;
        Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.gScore = 0;
            this.hScore = 0;
            this.parent = null;
        }

        void updateHScore(int dstX, int dstY) {
            this.hScore = Math.abs(x - dstX) + Math.abs(y - dstY);
        }

        int getFScore() {
            return this.gScore + this.hScore;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node node)) return false;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(x + (y * maxY));
        }
    }

    /**
     * Find path list.
     *
     * @param srcX the src x
     * @param srcY the src y
     * @param dstX the dst x
     * @param dstY the dst y
     * @return the list
     */
    public List<Node> findPath(int srcX, int srcY, int dstX, int dstY) {
        List<Node> path = new LinkedList<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getFScore));
        openSet.add(new Node(srcX, srcY));
        Set<Node> closedSet = new HashSet<>();

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == dstX && current.y == dstY) {
                while (current != null) {
                    path.add(current);
                    current = current.parent;
                }

                return path;
            }

            closedSet.add(current);

            for (int[] neighbour : neighbours) {
                int x = current.x + neighbour[0];
                int y = current.y + neighbour[1];

                if (x < 0 || x >= maxX || y < 0 || y >= maxY || grid[y][x] == 1)
                    continue;

                Node neighbor = new Node(x, y);
                int newGScore = current.gScore + 1;

                if (closedSet.contains(neighbor))
                    continue;

                if (!openSet.contains(neighbor) || newGScore < neighbor.gScore) {
                    neighbor.parent = current;
                    neighbor.gScore = newGScore;
                    neighbor.updateHScore(dstX, dstY);
                    openSet.add(neighbor);
                }
            }
        }

        return null;
    }

}
