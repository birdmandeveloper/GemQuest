package tech.GemQuest.ai;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.entity.Entity;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes(); // PAY ATTENTION STEVICK
    }

    public void instantiateNodes() {
        node = new Node[gp.getMaxWorldColumns()][gp.getMaxWorldRows()];

        int col = 0;
        int row = 0;

        while (col < gp.getMaxWorldColumns() && row < gp.getMaxWorldRows()) {
            node[col][row] = new Node(col, row);

            col++;
            if(col == gp.getMaxWorldColumns()) {
                col = 0;
                row++;
            }
        }
    }

    // Reset previous nodes
    public void resetNodes() {
        int col = 0;
        int row = 0;

        while (col < gp.getMaxWorldColumns() && row < gp.getMaxWorldRows()) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if(col == gp.getMaxWorldColumns()) {
                col = 0;
                row++;
            }
        }

        // Reset others
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {
        // Start fresh
        resetNodes();

        // Set Start and Goal nodes
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.getMaxWorldColumns() && row < gp.getMaxWorldRows()) {
            // Set solid nodes and check tiles
            int tileNum = gp.getTileManager().getMapTileNumbers()[gp.getCurrentMap()][col][row];
            if(gp.getTileManager().getTiles()[tileNum].isCollision()) {
                node[col][row].solid = true;
            }

            // Check interactive tiles
            for(int i = 0; i < gp.getInteractiveTiles().length; i++) {
                if(gp.getInteractiveTiles()[gp.getCurrentMap()][i].isDestructible() && gp.getInteractiveTiles()[gp.getCurrentMap()][i] != null) {
                    int itCol = gp.getInteractiveTiles()[gp.getCurrentMap()][i].getWorldX() / gp.getTileSize();
                    int itRow = gp.getInteractiveTiles()[gp.getCurrentMap()][i].getWorldY() / gp.getTileSize();
                    node[itCol][itRow].solid = true;
                }
            }

            // Set cost
            getCost(node[col][row]);

            col++;
            if(col == gp.getMaxWorldColumns()) {
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {
        // G COST
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H COST
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F COST
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while(!goalReached && step < 300) {
            int col = currentNode.col;
            int row = currentNode.row;

            // Check current node
            currentNode.checked = true;
            openList.remove(currentNode);

            // OPEN NODES
            if(row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            if(col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            if(row + 1 < gp.getMaxWorldRows()) {
                openNode(node[col][row + 1]);
            }
            if(col + 1 < gp.getMaxWorldColumns()) {
                openNode(node[col + 1][row]);
            }

            // Find best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++) {
                // Check if F COST is better
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }

                // If F COST is equal, go by G COST
                else if (openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if(openList.size() == 0) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }

            // Increase counter to avoid getting stuck
            step++;
        }

        return goalReached;
    }

    public void openNode(Node node) {
        if(!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {
        Node currentNode = goalNode;

        while(currentNode != startNode) {
            pathList.add(0, currentNode);
            currentNode = currentNode.parent;
        }
    }
}
