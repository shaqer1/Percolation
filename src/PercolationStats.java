public class PercolationStats {
    public static String getStats(int N, int T, String type){
        String s = "";
        try{
            double [] estimates = new double[T];
            double [] times = new double[T];
            long timeTotalInitial = System.currentTimeMillis();
            Percolation perc = new Percolation(N);
            for (int i = 0; i < T; i++) {
                long expStart = System.currentTimeMillis();
                perc = new Percolation(N, type);
                while(!perc.percolates()){
                    int x = (int) StdRandom.uniform(0, N*N);
                    perc.open(x/N,x%N);
                }
                //PercolationVisualizer.draw(perc,N);
                //perc.printBoard();
                estimates[i] = perc.countOpenCells()/(N*N + 0.0);
                times[i] = System.currentTimeMillis() - expStart;
            }
            long timeTotalFinal = System.currentTimeMillis();
            s += String.format("mean threshold=%.9f\n", StdStats.mean(estimates));
            s += String.format("std dev=%.9f\n", StdStats.stddev(estimates));
            s += "time=" + (timeTotalFinal-timeTotalInitial) + "\n";
            s += String.format("mean time=%.9f\n", StdStats.mean(times));
            s += String.format("stddev time=%.9f\n", StdStats.stddev(times));
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