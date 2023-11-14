package tech.GemQuest.ai;

public class Node {
    //A*ALGORITHM FOR AI
    //VARIABLES
    Node parent;
    public int col;
    public int row;
    int gCost;
    int hCost;
    int fCost;
    boolean solid;
    boolean open;
    boolean checked;

    //CONSTRUCTOR
    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }
}
