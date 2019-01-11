package com.michael.mooc.concurrency.example.atomic;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class AtomicExample6 {
    //total requests
    private static AtomicBoolean isHappened = new AtomicBoolean(false);

    public static int clientTotal = 5000;

    //total threads
    public static int threadTotal = 50;

    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();

        //信号量 Semaphore
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for(int i = 0; i < clientTotal; i++){
            executorService.execute(() -> {
                try{
                    semaphore.acquire();
                    test();
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}", isHappened.get());
    }

    private static void test(){
        if(isHappened.compareAndSet(false,true)){
            log.info("executed");
        }
    }

}
