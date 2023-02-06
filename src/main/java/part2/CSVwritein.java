package part2;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVwritein {


    public static void write(long startTime, String requestType, long latency, int responseCode) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("output.csv", true));
        String line1[] = {String.valueOf(startTime),requestType,String.valueOf(latency),String.valueOf(responseCode)};
        writer.writeNext(line1);
        writer.flush();

    }



    }



