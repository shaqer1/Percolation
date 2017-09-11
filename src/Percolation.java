import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

public class Percolation {
    private int n;
    private boolean weighted = false;
    private int nSquare;
    private int [] gridVals;
    private boolean percolates;
    private Object unionFinder;
    private int numOpened;
    private int virtualTop;

    /*
    *int 0 is closed black
    * int 1 is open white
    * int 2 is open blue
    * */
    public Percolation(int n) {
        this(n, "Fast");
    }


    public Percolation(int n, String typeUnion){
        try{
            this.n = n;
            this.nSquare = n*n;
            virtualTop = this.nSquare;
            numOpened = 0;
            gridVals = new int[n*n];
            if(typeUnion.equalsIgnoreCase("FAST")){
                weighted = true;
                unionFinder = new WeightedQuickUnionUF(nSquare +1);
            }else{
                unionFinder = new MyUF(nSquare+ 1);
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
            if(p < this.nSquare && p >= (this.nSquare - this.n)){
                union(p, virtualTop);
            }
            if(p<this.n && ((weighted)?((WeightedQuickUnionUF) unionFinder).connected(virtualTop, p)
                    : ((MyUF)unionFinder).connected(virtualTop, p))){
                percolates = true;
            }
        }
    }

    private void loveThyNeighbor(int x, int y) {
        int p = getCellValue(x,y);
        if(isOpen(p+-1*this.n)){
            union(p + -1*this.n,p);
        }
        if( isOpen(p+this.n)){
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
        }else{
            ((MyUF) unionFinder).union(one,two);
        }
    }

    private boolean checkValidity(int val){
        return val< nSquare && val >= 0;
    }

    private boolean checkValidity(int x, int y) {
        return x<this.n && x>=0 && y<this.n && y>=0;
    }
    public boolean isOpen(int x, int y) {
        return checkValidity(x,y) && gridVals[getCellValue(x,y)] >= 1;
    }
    public boolean isFull(int x, int y) {
        int finalPair = getCellValue(x,y);
        if(checkValidity(x,y)){
            if(weighted){
                return  ((WeightedQuickUnionUF) unionFinder).connected(virtualTop, finalPair);
            }else {
                return  ((MyUF) unionFinder).connected(virtualTop, finalPair);
            }
        }
        return false;
    }

    private int getCellValue(int x, int y) {
        return  y*n + x;
    }

    private boolean isOpen(int p) {
        return checkValidity(p) && gridVals[p] >=1;
    }

    public boolean percolates(){
        if(percolates){
            return true;
        }
        for (int i = 0; i < this.n; i++) {
            if((weighted)?((WeightedQuickUnionUF) unionFinder).connected(virtualTop, i)
                    : ((MyUF)unionFinder).connected(virtualTop, i)){
                return true;
            }
        }
        return false;
    }

    public int countOpenCells() {
        return numOpened;
    }

    public static void main(String[] args) {
        //System.out.println(args[0]);
        try{
            //In input = new In(args[0]);
            int n = StdIn.readInt();
            Percolation percolation = new Percolation(n, "fast");
            StdIn.readLine();
            while(StdIn.hasNextLine()){
                String s = StdIn.readLine();
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