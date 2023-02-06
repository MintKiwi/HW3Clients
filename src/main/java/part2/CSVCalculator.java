package part2;

import com.opencsv.CSVReader;
import org.apache.commons.collections.bag.SynchronizedSortedBag;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class CSVCalculator {
    public static void main(String[] args) throws IOException {
        final int  NUMOFREQUESTS = 500000;
        CSVReader reader = new CSVReader(new FileReader("output.csv"));
        Iterator<String[]> iterator = reader.iterator();
        int maxLatency = Integer.MIN_VALUE;
        int minLatency = Integer.MAX_VALUE;
        int sum = 0;
        ArrayList<Integer> list = new ArrayList<>();
        long startTime = 0, endTime = 0;
        int count = 0;

        while(iterator.hasNext()){
            String[] next = iterator.next();
            int latency = Integer.valueOf(next[2]);
            sum += latency;
            list.add(latency);
            if(count == 0){
                startTime = Long.valueOf(next[0]);
            }
            else if(count == NUMOFREQUESTS - 1){
                endTime = Long.valueOf(next[0]);
            }
            count++;
            if(latency > maxLatency){
                maxLatency = latency;
            }
            if(latency < minLatency){
                minLatency = latency;
            }
        }
        Collections.sort(list);
        System.out.println("Mean response time is " + (sum / NUMOFREQUESTS) + " milliseconds");
        System.out.println("Median response time is " + ((list.get((int) (list.size() * 0.5) - 1) + list.get((int) (list.size() * 0.5)))/2 +" milliseconds"));
        System.out.println("Total throughput is " + NUMOFREQUESTS/((endTime - startTime)/1000) + " requests/second");
        System.out.println("P99 response time is " + ((list.get((int) (list.size() * 0.99) - 1) + list.get((int) (list.size() * 0.99)))/2 +" milliseconds"));
        System.out.println("Maximum response time is " + maxLatency + " milliseconds");
        System.out.println("Minimum response time is " + minLatency + " milliseconds");




    }
}
