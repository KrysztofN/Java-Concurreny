package org.example;

import java.util.Queue;
import java.util.Random;

public class SchedulerThread extends Thread {
    private final QueueSharedResource queue;
    private final ChairsSharedResource chairs;
    private volatile boolean running = true;

    public SchedulerThread(QueueSharedResource queue, ChairsSharedResource chairs) {
        this.queue = queue;
        this.chairs = chairs;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                if(queue.size() > 0 && chairs.acquireChair()){
                    String service = queue.dequeue();
                    processService(service);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processService(String service) throws InterruptedException {
        int serviceTime = calculateServiceTime(service);

        Thread.sleep(serviceTime);
        chairs.releaseChair();
        System.out.println("Completed service type: " + service);
    }

    private int calculateServiceTime(String service) {
        Random random = new Random();
        return switch (service) {
            case "S" -> // Slow service
                    random.nextInt(5000, 10000);
            case "M" -> // Medium service
                    random.nextInt(1000, 2000);
            case "G" -> // Quick service
                    random.nextInt(2000, 5000);
            default -> 3000; // Default service time
        };
    }
}
