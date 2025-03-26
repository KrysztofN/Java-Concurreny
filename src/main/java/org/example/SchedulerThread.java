package org.example;

import java.util.Queue;
import java.util.Random;

public class SchedulerThread extends Thread {
    private QueueSharedResource queue;

    public SchedulerThread(QueueSharedResource queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random r = new Random();
        while (true) {
            if(queue.isFull()){
                String option = queue.dequeue();
                System.out.println("People in queue:"+ queue.size() +" Serving customer, option: " + option);
            }
        }
    }
}
