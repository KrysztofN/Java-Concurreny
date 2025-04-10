package org.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SchedulerThread extends Thread {
    private final QueueSharedResource queue;
    private final ChairsSharedResource chairs;
    private final HairDressersSharedResource hairDressers;
    private volatile boolean running = true;
    private String[] services = {"S", "M", "G"};

    public SchedulerThread(QueueSharedResource queue, ChairsSharedResource chairs, HairDressersSharedResource hairDressers) {
        this.queue = queue;
        this.chairs = chairs;
        this.hairDressers = hairDressers;
    }
    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                String customer = null;
                String service = null;

                synchronized (queue) {
                    if (queue.size() > 0 && chairs.hasAvailableChairs()) {
                        HashMap<String, Boolean> hairdressersAvailable = new HashMap<>();
                        for (String svc : services) {
                            hairdressersAvailable.put(svc, hairDressers.hasAvailableHairdressers(svc));
                        }

                        customer = queue.returnFirstOccurence(hairdressersAvailable);
                        if (!customer.isEmpty()) {
                            queue.removeFromFifo(customer);
                            service = String.valueOf(customer.charAt(0));

                            hairDressers.decrementHairdressers(service);
                            chairs.acquireChair(customer);
                        }
                    }
                }

                if (customer != null && service != null) {
//                    System.out.println(customer + " Available Hairdressers of type " + service + " : " +
//                            hairDressers.availableHairdressers(service));
                    processService(customer, service);
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException | IOException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    private void processService(String customer, String service) throws InterruptedException, IOException {
        int serviceTime = calculateServiceTime(service);

//        System.out.println(customer + " Available chairs: " + chairs.availableChairs());
        Thread.sleep(serviceTime);
        chairs.releaseChair(customer);
        hairDressers.incrementHairdressers(service);
//        System.out.println(customer + " Available chairs: " + chairs.availableChairs());
//        System.out.println(customer + " Available Hairdressers of type " + service + " : " + hairDressers.availableHairdressers(service));
    }

    private int calculateServiceTime(String service) {
        Random random = new Random();
        return switch (service) {
            case "S" -> // Slow service
                    random.nextInt(8000, 12000);
            case "M" -> // Medium service
                    random.nextInt(5000, 8000);
            case "G" -> // Quick service
                    random.nextInt(2000, 5000);
            default -> 3000; // Default service time
        };
    }
}
