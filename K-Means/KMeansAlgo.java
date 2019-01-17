
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class KMeansAlgo {
    private ArrayList<Clusters> clustersList = new ArrayList<>();
    private ArrayList<DataPoint> dataList = new ArrayList<>();

    public void getData(String fileName) throws Exception{
//    	System.out.println("Fetching data");
        String line="";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            String[] values = line.split("\t");
            if("id".equals(values[0])){
                continue;
            }else {
                DataPoint data = new DataPoint();
                data.id = Integer.parseInt(values[0]);
                data.x = Float.parseFloat(values[1]);
                data.y = Float.parseFloat(values[2]);
                dataList.add(data);
            }
        }
        br.close();
    }

    public float generateRandomValue(){
        float minX = 0.0f;
        float maxX = 1.0f;
        Random rand = new Random();
        return rand.nextFloat() * (maxX - minX) + minX;
        //return 0;
    }

    public void generateRandomCenters(int k){
        while (k>0){
            DataPoint data = new DataPoint();
            Clusters cluster = new Clusters();
            data.x = generateRandomValue();
            data.y = generateRandomValue();
            cluster.center = data;
            cluster.id = k;
            clustersList.add(cluster);
            k--;
        }
    }

    private float findDistance(DataPoint point1, DataPoint point2){
        return (float)Math.sqrt(Math.pow(point1.x-point2.x,2)+Math.pow(point1.y-point2.y,2));
    }

    private void assignmentStep(){
        float distance;
        float minDistance = 100;
        Clusters finalCluster = new Clusters();
        for(Clusters cluster : clustersList){
            cluster.clusterData.clear();
        }
        for(DataPoint data : dataList){
            for(Clusters cluster : clustersList){
                distance = findDistance(data,cluster.center);
                if(distance < minDistance){
                    minDistance = distance;
                    finalCluster = cluster;
                }

            }
            finalCluster.clusterData.add(data);
            minDistance = 100;
        }
    }

    private boolean updateStep(){
        float x,x1,y,y1;//sum of x and y and new x and y
        boolean centersChanged = false;
        for(Clusters cluster : clustersList){
            x = 0;
            y = 0;
            x1 = 0;
            y1 = 0;
            for(DataPoint data : cluster.clusterData){
                x += data.x;
                y += data.y;
            }
            if(cluster.clusterData.size() > 0) {
                x1 = x /cluster.clusterData.size();
                y1 = y / cluster.clusterData.size();
            }
            if(cluster.center.x != x1 || cluster.center.y != y1) {
                cluster.center.x = x / cluster.clusterData.size();
                cluster.center.y = y / cluster.clusterData.size();
                if(!centersChanged){
                    centersChanged = true;
                }
            }

        }
        return centersChanged;
    }

    public void applyKMeans(){
//    	System.out.println("Applying K-Means");
        boolean centersChanged = true;
        while (centersChanged){
            assignmentStep();
            centersChanged = updateStep();
        }
    }

    public float calculateSSE(){
//    	System.out.println("Calculating SSE");
        float totalSSE = 0;
        float clusterSSE;
        for(Clusters clusters : clustersList){
            clusterSSE = 0;
            for(DataPoint data : clusters.clusterData){
                clusterSSE += Math.pow(findDistance(data,clusters.center),2);
            }
            totalSSE+=clusterSSE;
        }
        return totalSSE;
    }

    public void writeIntoOutputFile(String outputPath, float SSE){
    	System.out.println("Writing the below content to output file: "+outputPath);
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            String content = "cluster-id \tList of points ids separated by comma\n";
            fw = new FileWriter(outputPath);
            bw = new BufferedWriter(fw);
            for(Clusters cluster : clustersList){
                content += cluster.id+"\t\t";
                for(DataPoint data : cluster.clusterData){
                    content += data.id+",";
                }
                content = content.substring(0,content.lastIndexOf(",")) + "\n";
            }
            content += "SSE = "+SSE;
            System.out.println(content);
            bw.write(content);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }

        }
    }

}
