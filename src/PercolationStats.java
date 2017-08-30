public class PercolationStats {
    public static void main(String[] args) {
        try{
            int T = Integer.parseInt(args[1]);
            double [] estimates = new double[T];
            double [] times = new double[T];
            long timeTotalInitial = System.currentTimeMillis();
            for (int i = 0; i < T; i++) {
                long expStart = System.currentTimeMillis();
                int N = Integer.parseInt(args[0]);
                Percolation perc = new Percolation(N);
                while(!perc.percolates()){
                    int x = (int) StdRandom.uniform(0, N*N);
                    perc.open(x/N,x%N);
                }
                estimates[i] = perc.countOpenCells()/(N*N);
                times[i] = System.currentTimeMillis() - expStart;
            }
            long timeTotalFinal = System.currentTimeMillis();
            StdOut.println("mean threshold=" + StdStats.mean(estimates));
            StdOut.println("std dev=" + StdStats.stddev(estimates));
            StdOut.println("time=" + (timeTotalFinal-timeTotalInitial));
            StdOut.println("mean time=" + StdStats.mean(times));
            StdOut.println("mean time=" + StdStats.stddev(times));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}