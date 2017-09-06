import java.util.*;

public class PercolationAnalyzer {
    public static void main(String[] args) {
        int [] valuesOfN = {10,25,50,100,250,500};
        int T = 30;
        String s = "fast";
        String s1 = "slow";
        int index = 0;
        boolean secondSpeed= false;
        HashMap <String, List<Double>> dataToPlot = new HashMap<>();
        for (int i = 0; i < valuesOfN.length*2; i++) {
            if(i == valuesOfN.length){
                index = 0;
                /*TODO: change files and reset hashmap
                *create N to Map
                 */
            }
            secondSpeed = i >= valuesOfN.length;
            dataToPlot  =  getData(dataToPlot ,PercolationStats.getStats(valuesOfN[index],T,(secondSpeed)?s1:s), valuesOfN[index]);
            //TODO:write to file
            index++;
        }
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
