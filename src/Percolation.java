import edu.princeton.cs.algs4.UF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.List;

public class Percolation {
    private int n;
    private int [][] grid;
    private WeightedQuickUnionUF unionFinder;
    /*
    *int 0 is closed black
    * int 1 is open white
    * int 2 is open blue
    * */
    public Percolation(int n){
        this.n = n;
        grid = new int[n][n];
        unionFinder = new WeightedQuickUnionUF(n*n);
    }
    public void open(int x, int y){
        int row = grid.length -x-1;
        if(row < grid.length && row >=0 &&
                y < grid[row].length && y >= 0){
            grid[row][y] = 1;
            connectPair(new Pair(row, y));
        }
    }

    private void connectPair(Pair pair) {
        List <Pair> adjacentOpenNodes = getAdjacentOpenNodes(pair.getRow(),pair.getCol());
        for(Pair adjacentOpenNode : adjacentOpenNodes){
            unionFinder.union(adjacentOpenNode.getValue(),pair.getValue());
        }
    }
    public boolean isOpen(int x, int y) {
        int row = grid.length - x - 1;
        return row < grid.length && row >= 0
                && y < grid[row].length && y >= 0
                && grid[row][y] == 1;
    }
    public boolean isFull(int x, int y) {
        return x < grid.length && x >= 0 && y < grid[x].length && y >= 0
                && checkForPath(new Pair(x, y));
    }
    private boolean checkForPath(Pair finalPair) {
        boolean result = false;
        List <Pair> adjacentOpenNodes = getOpenNodesSurfaceOrBottom(false);
        for(Pair adjacentOpenNode : adjacentOpenNodes){
            if(unionFinder.connected(adjacentOpenNode.getValue(), finalPair.getValue())){
                result = true;
                break;
            }
        }
        return result;

    }

    private List<Pair> getAdjacentOpenNodes(int row, int col/*, Stack<Pair> pairsInPath*/) {
        List <Pair> adjacentList = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if(row + i < grid.length && row + i >=0
                        && col +j <grid[row+i].length && col+j>=0 && grid[row+i][col+j] !=0){
                    adjacentList.add(new Pair(row+i,col+j));
                }
            }

        }
        return adjacentList;
    }
    public boolean percolates(){
        boolean result = false;
        List <Pair> openNodesBottomRow = getOpenNodesSurfaceOrBottom(true);
        for (Pair anOpenNodesBottomRow : openNodesBottomRow) {
            if (isFull(anOpenNodesBottomRow.getRow(), anOpenNodesBottomRow.getCol())) {
                result = true;
                break;
            }
        }
        return result;
    }
    private List<Pair> getOpenNodesSurfaceOrBottom(boolean bottom) {
        List<Pair> list = new ArrayList<>();
        for (int i = 0; i < grid[0].length; i++) {
            if(grid[(bottom)?grid.length-1:0][i] == 1){
                list.add(new Pair((bottom)?grid.length-1:0,i));
            }
        }
        return list;
    }
    private class Pair {
        private int row;
        private int col;
        Pair(int row, int col){
            this.row = row;
            this.col = col;
        }
        int getRow() {
            return row;
        }
        public void setRow(int row) {
            this.row = row;
        }
        int getCol() {
            return col;
        }
        public void setCol(int col) {
            this.col = col;
        }
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Pair && this.getRow() == ((Pair) obj).getRow() && this.getCol() == ((Pair) obj).getCol();
        }
        @Override
        public String toString() {
            return "Row: " + row + ", " + "Column: " + col;
        }
        int getValue() {
            return this.getRow()*n + this.getCol();
        }
    }

    public static void main(String[] args) {
        //System.out.println(args[0]);
        try{
            In input = new In(args[0]);
            int n = input.readInt();
            Percolation percolation = new Percolation(n);
            input.readLine();
            while(input.hasNextLine()){
                String s = input.readLine();
                percolation.open(Integer.parseInt(s.substring(0,s.indexOf(" "))),
                        Integer.parseInt(s.substring(s.indexOf(" ")+1)));
            }
            //percolation.printBoard();
            StdOut.println((percolation.percolates())?"Yes":"No");
            //percolation.printBoard();

            //TODO:change return type and color board
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void printBoard() {
        for (int[] aGrid : grid) {
            for (int j = 0; j < aGrid.length; j++) {
                String s = "";
                if (j == 0) {
                    s += "| ";
                }
                switch (aGrid[j]) {
                    case 0:
                        s += "black ";
                        break;
                    case 1:
                        s += "white ";
                        break;
                    case 2:
                        s += "blue  ";
                        break;
                }
                if (j == aGrid.length - 1) {
                    s = s.substring(0, s.length() - 1);
                    s += " |\n";
                }
                System.out.print(s);
            }
        }
    }
}
