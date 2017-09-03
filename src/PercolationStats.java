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
                String type = args[2];
                Percolation perc = new Percolation(N, type);
                while(!perc.percolates()){
                    int x = (int) StdRandom.uniform(0, N*N);
                    perc.open(x/N,x%N);
                }
                estimates[i] = perc.countOpenCells()/(N*N + 0.0);
                times[i] = System.currentTimeMillis() - expStart;
            }
            long timeTotalFinal = System.currentTimeMillis();
            StdOut.printf("mean threshold=%.9f\n", StdStats.mean(estimates));
            StdOut.printf("std dev=%.9f\n", StdStats.stddev(estimates));
            StdOut.println("time=" + (timeTotalFinal-timeTotalInitial));
            StdOut.printf("mean time=%.9f\n", StdStats.mean(times));
            StdOut.printf("stddev time=%.9f\n", StdStats.stddev(times));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}