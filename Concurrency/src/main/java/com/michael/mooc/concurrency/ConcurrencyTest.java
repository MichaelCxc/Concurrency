package com.michael.mooc.concurrency;


import com.michael.mooc.concurrency.annotation.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@NotThreadSafe
public class ConcurrencyTest {
    //total requests
    public static int clientTotal = 5000;

    //total threads
    public static int threadTotal = 50;

    public static int count = 0;

    public static void main(String[] args) throws Exception{
        //线程池 executorService
        ExecutorService executorService = Executors.newCachedThreadPool();

        //信号量 Semaphore
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for(int i = 0; i < clientTotal; i++){
            executorService.execute(() -> {
                try{
                    semaphore.acquire();
                    add();
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}", count);
    }

    private static void add(){
        count++;
    }
}
