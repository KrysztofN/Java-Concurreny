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
            int sleepTime = r.nextInt(10000);
            int option = r.nextInt(3);
            try {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (queue.isFull()) {
                    System.out.println("Queue full");
                } else{
                    try {
                        queue.enqueue(options[option]);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Added "+ queue.size() + " value: " + options[option] + " to the queue");
                }
            }
        }
    }
}
