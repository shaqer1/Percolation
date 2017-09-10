import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean visualize = false;
    private int n;
    private boolean weighted = false;
    private int nSquare;
    private int [] gridVals;
    private Object unionFinder;
    private Object isFullUnionFinder;
    private int numOpened;
    private boolean percolates;
    private int dummyBottom;
    private int dummyTop;

    /*
    *int 0 is closed black
    * int 1 is open white
    * int 2 is open blue
    * */
    public Percolation(int n) {
        this(n, "SLOW");
    }

    public Percolation(int n, String typeUnion){//TODO:hash and dont call union
        this(n,typeUnion,false);
    }
    public Percolation(int n, String typeUnion, boolean visualize){
        try{
            this.visualize = visualize;
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
                if(visualize){
                    isFullUnionFinder = new WeightedQuickUnionUF(nSquare + 3);
                }
            }else{
                unionFinder = new QuickFindUF(nSquare+ 3);
                if(visualize) {
                    isFullUnionFinder = new QuickFindUF(nSquare + 3);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void open(int x, int y){
        if(checkValidity(x,y)){
            int p = getCellValue(x, y);
            if(gridVals[p]  < 1) {
                numOpened++;
                gridVals[p] = 1;
            }
            loveThyNeighbor(x,y);
            boolean bottom;
            if((bottom = p < this.n) || p < this.nSquare && p >= (this.nSquare - this.n)){
                if(bottom){
                    union(p, dummyBottom);
                }else{
                    union(p,dummyTop);
                }
            }
        }
    }

    private void loveThyNeighbor(int x, int y) {
        int p = getCellValue(x,y);
        if(p + -1*this.n < this.nSquare && p + -1*this.n >=0
                && gridVals[p+-1*this.n] !=0){
            union(p + -1*this.n,p);
        }
        if(p + this.n < this.nSquare && p + this.n >=0
                && gridVals[p+this.n] !=0){
            union(p + this.n,p);
        }
        if((p-1)%this.n != this.n - 1 && isOpen(p-1)){
            union(p -1,p);
        }
        if((p+1)%this.n != 0 && isOpen(p+1)){
            union(p + 1,p);
        }
    }
    private void union(int one, int two){
        if(weighted){
            ((WeightedQuickUnionUF) unionFinder).union(one,two);
            if(visualize)
                ((WeightedQuickUnionUF) isFullUnionFinder).union(one,two);
        }else{
            ((QuickFindUF) unionFinder).union(one,two);
            if(visualize)
                ((QuickFindUF) isFullUnionFinder).union(one,two);
        }
    }

    private boolean checkValidity(int x, int y) {
        return x<this.n && x>=0 && y<this.n && y>=0;
    }
    public boolean isOpen(int x, int y) {
        return checkValidity(x,y) && gridVals[getCellValue(x,y)] >= 1;
    }
    public boolean isFull(int x, int y) {
        int finalPair = getCellValue(x,y);
        return checkValidity(x,y) && (weighted && visualize)?((WeightedQuickUnionUF) isFullUnionFinder).connected(dummyTop, finalPair)
                : (!weighted && visualize) && ((QuickFindUF) isFullUnionFinder).connected(dummyTop, finalPair);
    }

    private int getCellValue(int x, int y) {
        return  y*n + x;
    }

    private boolean isOpen(int p) {
        return isOpen(p%this.n,p/this.n);
    }

    public boolean percolates(){
        if((weighted)?((WeightedQuickUnionUF) unionFinder).connected(dummyTop, dummyBottom)
                : ((QuickFindUF)unionFinder).connected(dummyTop, dummyBottom)){
            percolates = true;
        }
        /*if(percolates) {
            return true;
        }*/
        return percolates;
    }

    public int countOpenCells() {
        return numOpened;
    }

    public static void main(String[] args) {
        //System.out.println(args[0]);
        try{
            In input = new In(args[0]);
            int n = input.readInt();
            Percolation percolation = new Percolation(n, "slow");
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