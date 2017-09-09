import edu.princeton.cs.algs4.StdOut;
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
    private boolean percolates;
    private Pair dummyBottom;
    private Pair dummyTop;

    public Percolation(int n, String typeUnion){//TODO:if top digits match bottom true
        try{
            percolates = false;
            this.n = n;
            this.nSquare = n*n;
            dummyBottom = new Pair(this.nSquare +1, this.n);
            dummyTop = new Pair(this.nSquare + 2, this.n);
            numOpened = 0;
            gridVals = new int[n*n];
            if(typeUnion.equalsIgnoreCase("FAST")){
                weighted = true;
                unionFinder = new WeightedQuickUnionUF(nSquare +3);
            }else{
                unionFinder = new UF(nSquare+ 3);
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
            loveThyNeighbor(p);
            addToLists(p);
        }
    }

    private void addToLists(Pair p) {
        Pair dummyToConnect = null;
        boolean added = false;
        if(p.getValue() < this.n && p.getValue() >= 0){
            bottomElements.add(p);
            added = true;
            dummyToConnect = dummyBottom;
        }else if(p.getValue()<this.nSquare && p.getValue() >= (this.nSquare-this.n)){
            topElements.add(p);
            added = true;
            dummyToConnect = dummyTop;
        }
        if(added && dummyToConnect != null){
            if(weighted){
                ((WeightedQuickUnionUF) unionFinder).union(p.getValue(),dummyToConnect.getValue());
            }else{
                ((UF) unionFinder).union(p.getValue(),dummyToConnect.getValue());
            }
        }else if (added){
            StdOut.println("incorrect initialization of dummy bottom and tops");
        }
    }

    private boolean checkValidity(int x, int y) {
        return x<this.n && x>=0 && y<this.n && y>=0;
    }

    private void loveThyNeighbor(Pair pair) {
        List <Pair> adjacentOpenNodes = getAdjacentOpenNodes(pair);
        for(Pair adjacentOpenNode : adjacentOpenNodes){//TODO: update data member find
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
    private boolean checkForPath(Pair finalPair) {
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

    public boolean percolates(){//TODO: adding wrong elements to bottom
        if((weighted)?((WeightedQuickUnionUF) unionFinder).connected(dummyTop.getValue(), dummyBottom.getValue())
                : ((UF)unionFinder).connected(dummyTop.getValue(), dummyBottom.getValue())){
        }
        if(percolates)
            return true;
        for (Pair anOpenNodesBottomRow : bottomElements) {
            if (isFull(anOpenNodesBottomRow.getRow(), anOpenNodesBottomRow.getCol())) {
                percolates = true;
            }
        }
        return percolates;
    }

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
            this.value = this.getCol()*n + this.getRow();
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
                s = new StringBuilder("\u001B[40m" + "|\n");
            }
            switch (gridVals[j]) {
                case 0:
                    s.insert(0, "\u001B[40m" + "bk ");
                    break;
                case 1:
                    s.insert(0, "\u001B[47m" + "wh ");
                    break;
                case 2:
                    s.insert(0, "\u001B[40m" + "bl ");
                    break;
            }
            if (j%this.n == 0) {
                s.insert(0, "\u001B[40m" + "| ");
                System.out.print(s);
                s = new StringBuilder();
            }
        }
        System.out.println("_________________________________" +
                "\n---------------------------------" +
                "\n_________________________________\n");
    }
}