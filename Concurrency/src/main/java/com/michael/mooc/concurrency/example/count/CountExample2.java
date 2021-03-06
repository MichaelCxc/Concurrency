package com.michael.mooc.concurrency.example.count;


import com.michael.mooc.concurrency.annotation.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ThreadSafe
public class CountExample2 {
    //total requests
    public static int clientTotal = 5000;

    //total threads
    public static int threadTotal = 50;

    public static AtomicInteger count = new AtomicInteger(0);

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
        log.info("count:{}", count.get());
    }

    private static void add(){
        //count.getAndIncrement();
        count.incrementAndGet();
    }
}
