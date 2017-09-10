import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.List;

public class Percolation {
    private int n;
    private boolean weighted = false;
    private int nSquare;
    private int [] gridVals;
    private Object unionFinder;
    //private List<Pair> topElements = new ArrayList<>();
    //private List<Pair> bottomElements = new ArrayList<>();
    //private Object isFullUnionFinder;
    private int numOpened;
    private boolean percolates;
    private int dummyBottom;
    private int dummyTop;

    public Percolation(int n, String typeUnion){//TODO:if top digits match bottom true
        try{
            percolates = false;
            this.n = n;
            this.nSquare = n*n;
            dummyBottom = this.nSquare;
            dummyTop = this.nSquare + 1;
            numOpened = 0;
            gridVals = new int[n*n];
            if(typeUnion.equalsIgnoreCase("FAST")){
                weighted = true;
                unionFinder = new WeightedQuickUnionUF(nSquare +3);
                //isFullUnionFinder = new WeightedQuickUnionUF(nSquare + 3);
            }else{
                unionFinder = new QuickFindUF(nSquare+ 3);
                //isFullUnionFinder = new QuickFindUF(nSquare + 3);
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
            int p = getCellValue(x, y);
            if(gridVals[p]  < 1) {
                numOpened++;
                gridVals[p] = 1;
            }
            loveThyNeighbor(x,y);
            addToLists(getCellValue(x,y));
        }
    }

    private void loveThyNeighbor(int x, int y) {
        List <Integer> adjacentOpenNodes = getAdjacentOpenNodes(getCellValue(x, y));
        for(int adjacentOpenNode : adjacentOpenNodes){
            if(weighted){
                ((WeightedQuickUnionUF) unionFinder).union(adjacentOpenNode,getCellValue(x,y));
                //((WeightedQuickUnionUF) isFullUnionFinder).union(adjacentOpenNode.getValue(),pair.getValue());
            }else{
                ((QuickFindUF) unionFinder).union(adjacentOpenNode,getCellValue(x,y));
                //((QuickFindUF) isFullUnionFinder).union(adjacentOpenNode.getValue(),pair.getValue());
            }
        }
    }

    private List<Integer> getAdjacentOpenNodes(int p) {
        List <Integer> adjacentList = new ArrayList<>();
        int [] arr = {-1*this.n,this.n};
        for (int i : arr) {
            if(p + i < this.nSquare && p + i >=0
                    && gridVals[p+i] !=0){
                adjacentList.add(p+i);
            }
        }
        if((p-1)%this.n != this.n - 1 && isOpen(p-1)){
            adjacentList.add(p-1);
        }
        if((p+1)%this.n != 0 && isOpen(p+1)){
            adjacentList.add(p+1);
        }
        return adjacentList;
    }

    private void addToLists(int p) {
        int dummyToConnect = -1;
        boolean added = false;
        if(p < this.n && p >= 0){
            //bottomElements.add(p);
            added = true;
            dummyToConnect = dummyBottom;
        }else if(p<this.nSquare && p >= (this.nSquare-this.n)){
            //topElements.add(p);
            added = true;
            dummyToConnect = dummyTop;
            if(weighted){
                //((WeightedQuickUnionUF) isFullUnionFinder).union(p.getValue(),dummyToConnect.getValue());
            }else{
                //((QuickFindUF) isFullUnionFinder).union(p.getValue(),dummyToConnect.getValue());
            }
        }
        if(added && dummyToConnect != -1){
            if(weighted){
                ((WeightedQuickUnionUF) unionFinder).union(p,dummyToConnect);
            }else{
                ((QuickFindUF) unionFinder).union(p,dummyToConnect);
            }
        }else if (added){
            StdOut.println("incorrect initialization of dummy bottom and tops");
        }
        /*if((weighted)?((WeightedQuickUnionUF) unionFinder).connected(dummyTop.getValue(), dummyBottom.getValue())
                : ((UF)unionFinder).connected(dummyTop.getValue(), dummyBottom.getValue())){
            printBoard();
            StdOut.println("percolates");
        }*/
    }

    private boolean checkValidity(int x, int y) {
        return x<this.n && x>=0 && y<this.n && y>=0;
    }
    public boolean isOpen(int x, int y) {
        return checkValidity(x,y) && gridVals[getCellValue(x,y)] >= 1;
    }
    public boolean isFull(int x, int y) {
        int finalPair = getCellValue(x,y);
        return checkValidity(x,y) && (weighted)?((WeightedQuickUnionUF) /*isFullUnionFinder*/unionFinder).connected(dummyTop, finalPair)
                : ((QuickFindUF)/*isFullUnionFinder*/unionFinder).connected(dummyTop, finalPair)/*checkForPath(finalPair)*/;
    }

    private int getCellValue(int x, int y) {
        return  y*n + x;
    }

    /*private boolean checkForPath(Pair finalPair) {
        *//*for(Pair adjacentOpenNode : topElements){
            if((weighted)?((WeightedQuickUnionUF) unionFinder).connected(adjacentOpenNode.getValue(), finalPair.getValue())
                    : ((QuickFindUF)unionFinder).connected(adjacentOpenNode.getValue(), finalPair.getValue())){
                return true;
            }
        }*//*
        return false;

    }*/

    private boolean isOpen(int p) {
        return isOpen(p%this.n,p/this.n);
    }

    public boolean percolates(){//TODO: adding wrong elements to bottom
        if((weighted)?((WeightedQuickUnionUF) unionFinder).connected(dummyTop, dummyBottom)
                : ((QuickFindUF)unionFinder).connected(dummyTop, dummyBottom)){
            percolates = true;
        }
        /*if(percolates) {
            return true;
        }
        for (Pair anOpenNodesBottomRow : bottomElements) {
            if (isFull(anOpenNodesBottomRow.getRow(), anOpenNodesBottomRow.getCol())) {
                percolates = true;
            }
        }*/
        return percolates;
    }

    public int countOpenCells() {
        return numOpened;
    }

    /*private class Pair {
        private int row;
        private int col;
        private int value;
        Pair(int row, int col, int n){
            this.row = row;
            this.col = col;
            this.value = this.getCol()*n + this.getRow();
        }

        Pair(int value, int n) {
            this(value%n,value/n, n);
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
    }*/

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
            //percolation.printBoard();
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
                s = new StringBuilder("\u001B[0m" + "|\n");
            }
            switch (gridVals[j]) {
                case 0:
                    s.insert(0, "\u001B[0m" + "bk ");
                    break;
                case 1:
                    s.insert(0, "\u001B[40m" + "wh ");
                    break;
                case 2:
                    s.insert(0, "\u001B[0m" + "bl ");
                    break;
            }
            if (j%this.n == 0) {
                s.insert(0, "\u001B[0m" + "| ");
                System.out.print(s);
                s = new StringBuilder();
            }
        }
        System.out.println("_________________________________" +
                "\n---------------------------------" +
                "\n_________________________________\n");
    }
}