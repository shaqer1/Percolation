import java.awt.*;

public class PercolationVisualizer {
    private static final int ANIM_DELAY = 100;

    public static void draw(Percolation perc, int N) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(0, N);
        StdDraw.setYscale(0, N);
        StdDraw.filledSquare(N/2.0, N/2.0, N/2.0);
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (perc.isFull(row, col)) {
                    StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                }
                else if (perc.isOpen(row, col)) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                }
                else
                    StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledSquare(row + 0.5, col + 0.5, 0.45);
            }
        }

        // write status text
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 12));
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(.25*N, -N*.025, perc.countOpenCells() + " open sites");
        if (perc.percolates()) StdDraw.text(.75*N, -N*.025, "percolates");
        else                   StdDraw.text(.75*N, -N*.025, "does not percolate");

    }

    public static void main(String[] args) {
        //In in = new In(args[0]);      // input file
        int N = StdIn.readInt();         // N-by-N percolation system
        StdDraw.show(0);
        Percolation perc = new Percolation(N, "fast");
        draw(perc, N);
        StdDraw.show(ANIM_DELAY);
        while (!StdIn.isEmpty()) {
            int i = StdIn.readInt();
            int j = StdIn.readInt();
            perc.open(i, j);
            draw(perc, N);
            StdDraw.show(ANIM_DELAY);
        }
        //perc.printBoard();
    }
}
