package com.sai.sri.siddhi.java.threads.basic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadInterrupt1 {
    private static Logger logger = LoggerFactory.getLogger(ThreadInterrupt1.class);

    // 10 sec wait time
    private static long waitTime = 20000; // in milli secs

    static void printMessage(String message) {
        logger.info(Thread.currentThread().getName() + ": " + message);
    }

    public static void main(String[] args) throws InterruptedException {

        printMessage("start..");

        MessagePrinter messagePrinter = new MessagePrinter(100, "I Love Threading");

        printMessage("creating printerThread..");

        long startTime = System.currentTimeMillis();
        Thread printerThread = new Thread(messagePrinter);
        printerThread.start();

        printMessage("started printerThread..");

        printMessage("I am now waiting for 5 seconds for the printerThread to complete..");
        printerThread.join(5000); // wait for 5 seconds for the printerThread to complete

        printMessage("waited 5 seconds, check if the printerThread is still alive");


        while (printerThread.isAlive()) {
            printMessage("the printerThread is still alive and printing..");

            printMessage("Sleep for 5 seconds..");
            Thread.sleep(5000); // sleep for 5 more seconds

            if(System.currentTimeMillis()-startTime>waitTime && printerThread.isAlive()){
                printMessage("Interrupting printerThread..");
                printerThread.interrupt();

                printMessage("I am now joining the printerThread..");
                printerThread.join();
            } else {
                printMessage("still not long enough to interrupt the printerThread");
            }

        }

        printMessage("Ha ha printerThread is no longer running ..");
    }

}

class MessagePrinter implements Runnable{

    Logger logger = LoggerFactory.getLogger(MessagePrinter.class);

    private final int noOfCopies;
    private final String message;

    public MessagePrinter(int noOfCopies, String mesasge) {
        this.noOfCopies = noOfCopies;
        this.message = mesasge;
    }

    void printMessage(String message) {
        logger.info(Thread.currentThread().getName() + ": " + message);
    }

    @Override
    public void run() {
        int counter = 0;
        while(counter++<noOfCopies){
            printMessage("Printing Message [" + counter + "]: " + message);
            try {
                Thread.sleep(1000); // sleep for a second
            } catch (InterruptedException e) {
                printMessage("Interrupted Exception, terminating printing..: " + e);
                e.printStackTrace();
                return;
            }
        }
    }
}
