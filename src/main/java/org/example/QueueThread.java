package org.example;

import java.util.Random;

public class QueueThread extends Thread {
    public QueueSharedResource queue;
    private final String[] options = {"S", "M", "G"};

    public QueueThread(QueueSharedResource queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random r = new Random();
        while(true) {
            int sleepTime = r.nextInt(5000);
            int option = r.nextInt(3);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                int isFull = queue.enqueue(options[option]);
                if (isFull == 1) {
                    System.out.println("Queue full");
                } else{
                    System.out.println("Added "+ queue.size() + " value: " + options[option] + " to the queue");
                }
            }
        }
    }
}
