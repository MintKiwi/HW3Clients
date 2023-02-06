package part1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class MultithreadClient4 {
    final static private int NUMTHREADS = 80;
    final static private int TOTALREQUESTS = 500000;
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicInteger failCount = new AtomicInteger(0);


    public AtomicInteger getFailCount() {
        return failCount;
    }

    public AtomicInteger getSuccessCount() {
        return successCount;
    }


    public static void main(String[] args) throws InterruptedException {
        MultithreadClient4 obj = new MultithreadClient4();
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
        latch.await();
        long end = System.currentTimeMillis();
        pool.shutdown();
        System.out.println("The number of successful requests sent is " + obj.getSuccessCount());
        System.out.println("The number of unsuccessful requests sent is " + obj.getFailCount());
        System.out.println("The total run time for all threads to complete is " + (end - start) + " milliseconds");
        System.out.println("The number of thread is " + NUMTHREADS);
        System.out.println("The total throughput in requests per second is " + (TOTALREQUESTS / ((end - start) / 1000.0)));
        System.out.println("Little’s Law throughput predictions in requests per second is " + (NUMTHREADS / 0.015));


    }


    public static void sends(int loopNum, MultithreadClient4 obj) throws Exception {
        for (int i = 0; i < loopNum; i++) {
            //call the client to send http post request
             TT.run(obj);
        }
    }




}
