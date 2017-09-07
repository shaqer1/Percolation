import edu.princeton.cs.algs4.UF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.List;

public class Percolation {
    private int n;
    private boolean weighted = false;
    private int nSquare;
    private int [] gridVals;
    private Object unionFinder;
    private List<Pair> topElements = new ArrayList<>();
    private List<Pair> bottomElements = new ArrayList<>();
    private int numOpened;

    public Percolation(int n, String typeUnion){
        try{
            this.n = n;
            this.nSquare = n*n;
            numOpened = 0;
            gridVals = new int[n*n];
            if(typeUnion.equalsIgnoreCase("FAST")){
                weighted = true;
                unionFinder = new WeightedQuickUnionUF(nSquare);
            }else{
                unionFinder = new UF(nSquare);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    *int 0 is closed black
    * int 1 is open white
    * int 2 is open blue
    * */
    public Percolation(int n) {
        this(n, "SLOW");
    }
    public void open(int x, int y){
        if(checkValidity(x,y)){
            Pair p = new Pair(x, y, this.n);
            if(gridVals[p.getValue()]  < 1) {
                numOpened++;
                gridVals[p.getValue()] = 1;
            }
            connectPair(p);
            addToLists(p);
        }
    }

    private void addToLists(Pair p) {/*
        int min = (bottom)? 0:(this.n*(this.n-1)), max = (!bottom)?this.nSquare: this.n;*/
        if(p.getValue() < this.n && p.getValue() >= 0){
            bottomElements.add(p);
        }else if(p.getValue()<this.nSquare && p.getValue() >= (this.nSquare-1)){
            topElements.add(p);
        }
    }

    private boolean checkValidity(int x, int y) {
        return x<this.n && x>=0 && y<this.n && y>=0;
    }

    private void connectPair(Pair pair) {
        List <Pair> adjacentOpenNodes = getAdjacentOpenNodes(pair);
        for(Pair adjacentOpenNode : adjacentOpenNodes){
            if(weighted){
                ((WeightedQuickUnionUF) unionFinder).union(adjacentOpenNode.getValue(),pair.getValue());
            }else{
                ((UF) unionFinder).union(adjacentOpenNode.getValue(),pair.getValue());
            }
        }
    }

    public boolean isOpen(int x, int y) {
        return checkValidity(x,y) && gridVals[new Pair(x,y,this.n).getValue()] >= 1;
    }
    public boolean isFull(int x, int y) {
        return checkValidity(x,y) && checkForPath(new Pair(x, y, this.n));
    }
    private boolean checkForPath(Pair finalPair) {/*
        List <Pair> adjacentOpenNodes = getOpenNodesSurfaceOrBottom(false);*/
        for(Pair adjacentOpenNode : topElements){
            if((weighted)?((WeightedQuickUnionUF) unionFinder).connected(adjacentOpenNode.getValue(), finalPair.getValue())
                    : ((UF)unionFinder).connected(adjacentOpenNode.getValue(), finalPair.getValue())){
                return true;
            }
        }
        return false;

    }

    private List<Pair> getAdjacentOpenNodes(Pair p) {
        List <Pair> adjacentList = new ArrayList<>();
        int [] arr = {-1*this.n,this.n};
        for (int i : arr) {
            if(p.getValue() + i < this.nSquare && p.getValue() + i >=0
                    && gridVals[p.getValue()+i] !=0){
                    adjacentList.add(new Pair(p.getValue()+i, this.n));
            }
        }
        if((p.getValue()-1)%this.n != this.n - 1 && isOpen(new Pair(p.getValue()-1,this.n))){
            adjacentList.add(new Pair(p.getValue()-1, this.n));

        }
        if((p.getValue()+1)%this.n != 0 && isOpen(new Pair(p.getValue()+1,this.n))){
            adjacentList.add(new Pair(p.getValue()+1, this.n));
        }
        return adjacentList;
    }

    private boolean isOpen(Pair p) {
        return isOpen(p.getRow(),p.getCol());
    }

    public boolean percolates(){/*
        List <Pair> openNodesBottomRow = getOpenNodesSurfaceOrBottom(true);*/
        for (Pair anOpenNodesBottomRow : bottomElements) {
            if (isFull(anOpenNodesBottomRow.getRow(), anOpenNodesBottomRow.getCol())) {
                return true;
            }
        }
        return false;
    }
    /*private List<Pair> getOpenNodesSurfaceOrBottom(boolean bottom) {
        List<Pair> list = new ArrayList<>();
        int min = (bottom)? 0:(this.n*(this.n-1)), max = (!bottom)?this.nSquare: this.n;
        for (int i = min; i < max; i++) {
            Pair p = new Pair(i,this.n);
            if(isOpen(p.getRow(),p.getCol())){
                list.add(p);
            }
        }
        return list;
    }*///TODO:add lists for elements on top and bottom to prevent this make faster in isOpen

    public int countOpenCells() {
        return numOpened;
    }

    private class Pair {
        private int row;
        private int col;
        private int value;
        Pair(int row, int col, int n){
            this.row = row;
            this.col = col;
            this.value = this.getRow()*n + this.getCol();
        }

        Pair(int value, int n) {
            this(value/n,value%n, n);
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
            return "Row: " + row + ", " + "Column: " + col + ", Val: " + value;
        }
        int getValue() {
            return value;
        }
    }

    public static void main(String[] args) {
        //System.out.println(args[0]);
        try{
            In input = new In(args[0]);
            int n = input.readInt();
            Percolation percolation = new Percolation(n, "fast");
            input.readLine();
            while(input.hasNextLine()){
                String s = input.readLine();
                percolation.open(Integer.parseInt(s.substring(0,s.indexOf(" "))),
                        Integer.parseInt(s.substring(s.indexOf(" ")+1)));
            }
            percolation.printBoard();
            StdOut.println((percolation.percolates())?"Yes":"No");
            //percolation.printBoard();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void printBoard() {
        StringBuilder s = new StringBuilder();
        for (int j = gridVals.length - 1; j >= 0; j--) {
            if (s.length() == 0) {
                s = new StringBuilder("|\n");
            }
            switch (gridVals[j]) {
                case 0:
                    s.insert(0, "black ");
                    break;
                case 1:
                    s.insert(0, "white ");
                    break;
                case 2:
                    s.insert(0, "blue  ");
                    break;
            }
            if (j%this.n == 0) {
                s.insert(0, "| ");
                System.out.print(s);
                s = new StringBuilder();
            }
        }
        System.out.println("_________________________________" +
                "\n---------------------------------" +
                "\n_________________________________\n");
    }
}
