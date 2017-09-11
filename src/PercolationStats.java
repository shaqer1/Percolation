public class PercolationStats {
    static String getStats(int N, int T, String type){
        String s = "";
        try{
            double [] estimates = new double[T];
            double [] times = new double[T];
            Percolation percolation;
            long timeTotalInitial = System.nanoTime();
            for (int i = 0; i < T; i++) {
                long expStart = System.nanoTime();
                percolation = new Percolation(N, type);
                while(!percolation.percolates()){
                    int x = (int) StdRandom.uniform(0, N*N);
                    percolation.open(x/N,x%N);
                }
                //PercolationVisualizer.draw(percolation,N);
                //percolation.printBoard();
                estimates[i] = 0.4*(percolation.countOpenCells()/(N*N + 0.0));
                times[i] = ((System.nanoTime() - expStart)/1000000000.0);
            }
            long timeTotalFinal = System.nanoTime();
            s += String.format("mean threshold=%.16f\n", StdStats.mean(estimates));
            s += String.format("std dev=%.18f\n", StdStats.stddev(estimates));
            s += "time=" + ((timeTotalFinal-timeTotalInitial)/1000000000.0) + "\n";
            s += String.format("mean time=%.18f\n", StdStats.mean(times));
            s += String.format("stddev time=%.18f\n", StdStats.stddev(times));
        }catch(Exception e){
            e.printStackTrace();
        }
        return s;
    }
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        String type = args[2];
        StdOut.print(getStats(N,T,type));
    }
}