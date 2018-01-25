package com.sai.sri.siddhi.java.threads.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadStaticLocks {

    static Logger logger = LoggerFactory.getLogger(ThreadStaticLocks.class);

    private static int counter;

    public static int incrementCounterStatic() {
        return ++counter;
    }

    public int incrementCounter() {
        return ++counter;
    }

    public static synchronized void printCounterStatic() {
        logger.info("static: counter:" + counter);
    }

    public synchronized void printCounter() {
        logger.info("counter:" + counter);
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyInstanceRunnable());
        Thread t2 = new Thread(new MyStaticRunnable());
        Thread t3 = new Thread(new MyStaticRunnable());


        t1.start();
        t2.start();
        t3.start();
    }

}

class MyInstanceRunnable implements Runnable {
    Logger logger = LoggerFactory.getLogger(MyInstanceRunnable.class);
    @Override
    public void run() {
        while(true) {
            ThreadStaticLocks locks = new ThreadStaticLocks();
            int counter = locks.incrementCounter();
            logger.info("instance incrementer: " + counter);

            if(counter>10)
                break;
        }
    }
}

class MyStaticRunnable implements Runnable {
    Logger logger = LoggerFactory.getLogger(MyStaticRunnable.class);
    @Override
    public void run() {
        while(true) {
            int counter = ThreadStaticLocks.incrementCounterStatic();
            logger.info("static incrementer: " + counter);

            if(counter>10)
                break;
        }
    }
}