import edu.princeton.cs.algs4.UF;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Precolation {
    private int n;
    private int [][] grid;
    private UF unionFinder;
    /*
    *int 0 is closed black
    * int 1 is open white
    * int 2 is open blue
    * */
    public Precolation(int n){
        this.n = n;
        grid = new int[n][n];
        unionFinder = new UF(n);
    }
    public void open(int x, int y){
        int col, row = grid.length -x-1;
        if(row < grid.length && row >=0 &&
                (col = grid[row].length -y-1)< grid[row].length && col >= 0){
            grid[row][col] = 1;

        }
    }
    public boolean isOpen(int x, int y) {
        int col, row = grid.length - x - 1;
        return row < grid.length && row >= 0
                && (col = grid[row].length - y - 1) < grid[row].length && col >= 0
                && grid[row][col] == 1;
    }
    public boolean isFull(int x, int y) {
        int col, row = grid.length -x-1;
        if(row < grid.length && row >=0 &&
                (col = grid[row].length -y-1)< grid[row].length && col >= 0){
            return checkForPath(new Pair(row,col), new Stack<>()).isBoolPath();
        }
    }
    /*public boolean isFull(int x, int y, boolean flag){*//*
        int col, row = grid.length -x-1;
        if(flag
                && row < grid.length && row >=0 &&
                (col = grid[row].length -y-1)< grid[row].length && col >= 0 && grid[row][col] != 0){
            return checkForPath(new Pair(row,col), new Stack<Pair>());
        }else if (!flag){
            return checkForPath(new Pair(x,y), new Stack<Pair>());
        }
        return false;*//*
    }*/


    private PathResponse <Pair> checkForPath(Pair p,  Stack <Pair> pairsInPath) {

    }

    private List<Pair> getAdjacentOpenNodes(int row, int col, Stack<Pair> pairsInPath) {
        List <Pair> adjacentList = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if(!(i==0 && j==0) && !pairsInPath.contains(new Pair(row+i,col+j))
                        && row + i < grid.length && row + i >=0 && col +j <grid[row+i].length && col+j>=0 && grid[row+i][col+j] !=0){
                    adjacentList.add(new Pair(row+i,col+j));
                }
            }

        }
        return adjacentList;
    }
    public boolean percolates(){/*
        boolean result = false;
        List <Pair> openNodesFirstRow = getOpenNodesFirstRow();
        for (int i = 0; i < openNodesFirstRow.size(); i++) {
            if(isFull(openNodesFirstRow.get(i).getRow(), openNodesFirstRow.get(i).getCol(), false)){
                result = true;
                break;
            }
        }
        return result;*/
    }
    private List<Pair> getOpenNodesFirstRow() {
        List<Pair> list = new ArrayList<>();
        for (int i = 0; i < grid[0].length; i++) {
            if(grid[0][i] == 1){
                list.add(new Pair(0,i));
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
    }

    public static void main(String[] args) {
        System.out.println(args[0]);
        try{/*
            In input = new In(args[0]);*/
            int n = StdIn.readInt();
            Precolation precolation = new Precolation(n);
            StdIn.readLine();
            while(StdIn.hasNextLine()){
                String s = StdIn.readLine();
                precolation.open(Integer.parseInt(s.substring(0,s.indexOf(" "))),
                        Integer.parseInt(s.substring(s.indexOf(" ")+1)));
            }
            precolation.printBoard();
            StdOut.println(precolation.percolates());
            precolation.printBoard();
            //TODO:change return type and color board
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void printBoard() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                String s = "";
                if(j == 0 ){
                    s+="| ";
                }
                switch (grid[i][j]){
                    case 0:
                        s+="black ";
                        break;
                    case 1:
                        s+="white ";
                        break;
                    case 2:
                        s+="blue  ";
                        break;
                }
                if(j == grid[i].length -1){
                    s = s.substring(s.length()-1);
                    s+=" |\n";
                }
                System.out.print(s);
            }
        }
    }

    private class PathResponse <T>{
        private boolean boolPath;
        private Stack <T> stackPath;

        public boolean isBoolPath() {
            return boolPath;
        }

        public void setBoolPath(boolean boolPath) {
            this.boolPath = boolPath;
        }

        public Stack<T> getStackPath() {
            return stackPath;
        }

        public void setStackPath(Stack<T> stackPath) {
            this.stackPath = stackPath;
        }
    }
}
