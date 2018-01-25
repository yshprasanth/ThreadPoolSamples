package com.sai.sri.siddhi.java.threads.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ThreadWaitNotify1 {

    public static void main(String[] args) {
        Channel channel = new Channel();
        Thread questioner = new Thread(new User(Role.QUESTIONER, channel));
        Thread answerer = new Thread(new User(Role.ANSWERER, channel));

        questioner.start();
        answerer.start();
    }
}

class Channel{

    private Logger logger = LoggerFactory.getLogger(Channel.class);

    private boolean askedFlag;

    public synchronized void question(String question) {
        if(askedFlag) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.info("question interrupted..");
                e.printStackTrace();
            }
        }

        logger.info("Question: " + question );
        askedFlag=true;
        notify();
    }

    public synchronized void answer(String answer) {
        if(!askedFlag) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.info("answer interrupted..");
                e.printStackTrace();
            }
        }
        logger.info("Answer: " + answer);
        askedFlag = false;
        notify();
    }
}

class User implements Runnable{

    private final Channel channel;
    private Logger logger = LoggerFactory.getLogger(Channel.class);
    private Role whoAmI;

    public User(Role whoAmI, Channel channel) {
        this.whoAmI = whoAmI;
        this.channel = channel;
    }

    private void print(String message) {
        logger.info(whoAmI + ": " + message);
    }

    @Override
    public void run() {
        print("Hi, you can either ask / respond..");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            print("enter some text: ");
            switch (whoAmI) {
                case QUESTIONER:
                    channel.question(scanner.nextLine());
                    break;
                case ANSWERER:
                    channel.answer(scanner.nextLine());
                    break;
            }
        }

    }
}

enum Role {
    QUESTIONER, ANSWERER
}