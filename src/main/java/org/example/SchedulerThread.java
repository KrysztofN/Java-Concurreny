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
        String service = "F";
        int min = 0;
        int max = 0;
        while (true) {
            if(queue.size() > 0){
                try {
                    Thread.sleep(calculateServiceTime(service));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    service = queue.dequeue();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("People in queue: "+ queue.size() +" Serving customer, option: " + service);
            }
        }
    }

    public int calculateServiceTime(String service){
        int min = 0;
        int max = 0;

        switch (service) {
            case "S" -> {
                min = 5000;
                max = 10000;
            }
            case "G" -> {
                min = 2000;
                max = 5000;
            }
            case "M" -> {
                min = 1000;
                max = 2000;
            }
        }
        Utils utils = new Utils();
        return utils.getRandomInRange(min, max);
    }
}
