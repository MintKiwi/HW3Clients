package part1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;



public class MultithreadClient1 {
    final static private int NUMTHREADS = 200;
    final static private int TOTALREQUESTS = 500000;
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicInteger failCount = new AtomicInteger(0);

    static List<Long> getLatencies = Collections.synchronizedList(new ArrayList<Long>());




    public AtomicInteger getFailCount() {
        return failCount;
    }

    public AtomicInteger getSuccessCount() {
        return successCount;
    }



    public static void main(String[] args) throws InterruptedException {
        MultithreadClient1 obj = new MultithreadClient1();
        //use thread pool to reuse threads more efficiently
        ExecutorService pool = Executors.newCachedThreadPool();
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(NUMTHREADS);
        for (int i = 0; i < NUMTHREADS; i++) {

            pool.execute(() -> {
                        try {
                            sends(TOTALREQUESTS / NUMTHREADS, obj);

                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            latch.countDown();
                        }
                    }

            );
        }
        new Thread(() -> {
//            when the latch count reaches 0, stop the get thread
            while(latch.getCount() != 0){
                for(int i = 0; i < 5; i++){
                    long startTime = System.currentTimeMillis();
                    //mix up the two get request
                    if(i % 2 == 0){
                        SingleClient1.getMatchRequest();
                    }
                    else{
                        SingleClient1.getMatchStatsRequest();
                    }
                    long latency = System.currentTimeMillis() - startTime;
                    //add the get latency to the synchronizedList
                    getLatencies.add(latency);


                }

                try {
                    Thread.sleep(850);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();




        latch.await();

        long end = System.currentTimeMillis();
        pool.shutdown();


        System.out.println("The number of successful requests sent is " + obj.getSuccessCount());
        System.out.println("The number of unsuccessful requests sent is " + obj.getFailCount());
        System.out.println("The total run time for all threads to complete is " + (end - start) + " milliseconds");
        System.out.println("The number of thread is " + NUMTHREADS);
        System.out.println("The total throughput in requests per second is " + (TOTALREQUESTS / ((end - start) / 1000.0)));
        System.out.println("Little’s Law throughput predictions in requests per second is " + (NUMTHREADS / 0.015));
        System.out.println("The number of get requests are " + getLatencies.size());
        System.out.println("The min latency of the get requests in millisecond is " + getLatencies.stream().min(Long::compare).get());
        System.out.println("The max latency of the get requests in millisecond is " + getLatencies.stream().max(Long::compare).get());
        System.out.println("The mean latency of the get requests in millisecond is " + getLatencies.stream().mapToLong(Long::longValue).average().getAsDouble());
        System.out.println("The mean latency of the post requests in millisecond is " + SingleClient1.postLatencies.stream().mapToLong(Long::longValue).average().getAsDouble());






    }


    public static void sends(int loopNum, MultithreadClient1 obj) throws Exception {
        for (int i = 0; i < loopNum; i++) {
            //call the client to send http post request
            SingleClient1.run(obj);
        }
    }


}
