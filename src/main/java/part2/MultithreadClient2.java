package part2;

import part1.SingleClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadClient {
    final static private int NUMTHREADS = 10;
    final static private int TOTALREQUESTS = 500000;
    private int successCount = 0;
    private int failCount = 0;


    public int getFailCount() {
        return failCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    synchronized public void increaseFailCount() {
        this.failCount++;
    }

    synchronized public void increaseSuccessCount() {
        this.successCount++;
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CountDownLatch latch = new CountDownLatch(NUMTHREADS);
        MultithreadClient obj = new MultithreadClient();
        for (int i = 0; i < NUMTHREADS; i++) {

            pool.execute(() -> {
                try {
                    sends(TOTALREQUESTS / NUMTHREADS, obj);


                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });


        }
        latch.await();
        pool.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("The number of successful requests sent is " + obj.getSuccessCount());
        System.out.println("The number of unsuccessful requests sent is " + obj.getFailCount());
        System.out.println("The total run time for all threads to complete is " + (end - start) + " milliseconds");
        System.out.println("The total throughput in requests per second is " + (TOTALREQUESTS / ((end - start) / 1000)));
    }


    public static void sends(int loopNum, MultithreadClient obj) throws Exception {
        for (int i = 0; i < loopNum; i++) {
            new SingleClient().executePost(obj);

        }
    }


}
