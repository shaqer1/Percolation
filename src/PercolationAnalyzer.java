import java.util.HashMap;
import java.util.List;

public class PercolationAnalyzer {
    public static void main(String[] args) {
        int [] valuesOfN = {10,25,50,100,250,500};
        int T = 30;
        String s = "fast";
        String s1 = "slow";
        int index = 0;
        boolean secondSpeed= false;
        for (int i = 0; i < valuesOfN.length*2; i++) {
            if(i == valuesOfN.length){
                index = 0;
            }
            secondSpeed = i >= valuesOfN.length;
            HashMap <String, List<Double>> dataToPlot  =  getData(PercolationStats.getStats(valuesOfN[index],T,(secondSpeed)?s1:s));
            index++;
        }
    }

    private static HashMap<String, List<Double>> getData(String stats) {
        return null;
    }
}
