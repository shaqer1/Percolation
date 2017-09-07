import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PercolationAnalyzer {
    public static void main(String[] args) {
        int [] valuesOfN = {10,25,50,100,250,500};
        int T = 30;
        String s = "fast";
        String s1 = "slow";
        int index = 0;
        boolean secondSpeed;
        HashMap <String, List<Double>> dataToPlot = new HashMap<>();
        for (int i = 0; i < valuesOfN.length*2; i++) {
            if(i == valuesOfN.length){
                index = 0;
                writeToFile(dataToPlot, "nVsTimeSlow.csv", true);
                dataToPlot.clear();
                /*TODO: change files and reset hashmap

                *create N to Map
                 */
            }
            secondSpeed = i >= valuesOfN.length;
            dataToPlot  =  getData(dataToPlot ,PercolationStats.getStats(valuesOfN[index],T,(secondSpeed)?s1:s), valuesOfN[index]);
            //TODO:write to file
            index++;
        }
        writeToFile(dataToPlot,"nVsTimeFast.csv", true);
    }

    private static void writeToFile(HashMap<String, List<Double>> dataToPlot, String filePath, boolean append) {
        File file = new File(filePath);
        try{
            BufferedWriter bfr = new BufferedWriter(new FileWriter(file, append));//create buffer
            bfr.write(getString(dataToPlot));//write buffer message
            bfr.flush();//flush buffer and close it
            bfr.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String getString(HashMap<String, List<Double>> dataToPlot) {
        StringBuilder s = new StringBuilder("N,Time\n");
        List <Double> N = dataToPlot.get("data");
        List <Double> times = dataToPlot.get("time");
        for (int i = 0; i < N.size(); i++) {
            s.append(N.get(i)).append(times.get(i));
        }
        return s.toString();
    }

    private static HashMap<String, List<Double>> getData(HashMap<String, List<Double>> response, String stats, int N) {
        List <Double> data = new ArrayList<>();
        data.add(0.0 + N);
        response.put("data", data);
        String [] lines = stats.split("\n");
        try{
            for (String s: lines) {
                String [] dataValue = s.split("=");
                if(dataValue.length == 2){
                    putInHash(response, dataValue[0], Double.parseDouble(dataValue[1]));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    private static void putInHash(HashMap<String, List<Double>> hashMap, String dataKey, double data) {
        if(hashMap.containsKey(dataKey)){
            hashMap.get(dataKey).add(data);
        }else{
            hashMap.put(dataKey,new ArrayList<>(Collections.singletonList(data)));
        }
    }
}
