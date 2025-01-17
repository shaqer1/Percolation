public class MyUF {
    private int[] id; // access to component id (site indexed)
    private int count; // number of components
    //private int[] freq;
    public MyUF(int N)
    { // Initialize component id array.
        count = N;
        id = new int[N];/*
        freq = new int[N];*/
        for (int i = 0; i < N; i++){
            id[i] = i;/*
            freq[i] = 1;*/
        }
    }
    public int count()
    { return count; }
    public boolean connected(int p, int q)
    { return find(p) == find(q); }
    public int find(int p){
        return id[p];
    }
    public void union(int p, int q){
        // Put p and q into the same component.
        int pID = find(p);
        int qID = find(q);
        if (pID == qID) return;
        /*boolean less = freq[pID] < freq[qID];
        int idToCompare = (less)?pID:qID;
        int idToChange = (less)?qID:pID;*/
// Nothing to do if p and q are already
        //in the same component.
// Rename p’s component to q’s name.
        //freq[idToChange]+=freq[idToCompare];
        for (int i = 0; i < id.length; i++)
            if (id[i] == pID) {
                id[i] = qID;
            }
        count--;
    }
    // See page 222 (quick-find),page 224 (quick-union) andpage 228 (weighted).
    public static void main(String[] args)
    { // Solve dynamic connectivity problem on StdIn.
        int N = StdIn.readInt(); // Read number of sites.
        MyUF uf = new MyUF(N); // Initialize N components.
        while (!StdIn.isEmpty())
        {
            int p = StdIn.readInt();
            int q = StdIn.readInt(); // Read pair to connect.
            if (uf.connected(p, q)) continue; // Ignore if connected.
            uf.union(p, q); // Combine components
            StdOut.println(p + " " + q); // and print connection.
        }
        StdOut.println(uf.count() + " components");
    }
}
