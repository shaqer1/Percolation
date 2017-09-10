import edu.princeton.cs.algs4.StdOut;

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
        boolean fastSpeed;
        HashMap <String, Double> dataToPlot = new HashMap<>();
        writeToFile("N,Time,Mean Threshold\n", "nVsTimeSlow.csv", false);
        writeToFile("N,Time,Mean Threshold\n", "nVsTimeFast.csv", false);
        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < valuesOfN.length*2; i++) {
            if(i == valuesOfN.length){
                index = 0;
                //writeToFile(dataToPlot, "nVsTimeSlow.csv", true);
            }
            fastSpeed = i >= valuesOfN.length;
            dataToPlot  =  getData(dataToPlot ,PercolationStats.getStats(valuesOfN[index],T,(fastSpeed)?s1:s), valuesOfN[index]);
            index++;
            writeToFile(getString(dataToPlot),(fastSpeed)?"nVsTimeSlow.csv":"nVsTimeFast.csv", true);
            dataToPlot.clear();
        }
        StdOut.println(System.currentTimeMillis() - timeStart);
    }

    private static void writeToFile(String message, String filePath, boolean append) {
        File file = new File(filePath);
        try{
            BufferedWriter bfr = new BufferedWriter(new FileWriter(file, append));//create buffer
            bfr.write(message);//write buffer message
            bfr.flush();//flush buffer and close it
            bfr.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String getString(HashMap<String, Double> dataToPlot) {
        return String.valueOf(dataToPlot.get("data")) + "," +
                dataToPlot.get("time") + "," +
                dataToPlot.get("mean threshold") + "\n";
    }

    private static HashMap<String, Double> getData(HashMap<String, Double> response, String stats, int N) {
        response.put("data", (0.0 + N));
        String [] lines = stats.split("\n");
        try{
            for (String s: lines) {
                String [] dataValue = s.split("=");
                if(dataValue.length == 2){
                    response.put(dataValue[0], Double.parseDouble(dataValue[1]));
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
