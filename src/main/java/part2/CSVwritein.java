package part2;


import com.opencsv.CSVWriter;


import java.io.FileWriter;
import java.io.IOException;


public class CSVwritein {


    public static void write(long startTime, String requestType, long latency, int responseCode) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("output.csv", true));
        String line1[] = {String.valueOf(startTime),requestType,String.valueOf(latency),String.valueOf(responseCode)};
        writer.writeNext(line1);
        writer.flush();

    }



    }



