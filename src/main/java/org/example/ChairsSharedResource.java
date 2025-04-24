package org.example;

public class ChairsSharedResource {
    private static final int CHAIR_CAPACITY = 4;
    private int chairsInUse = 0;

    public synchronized int availableChairs(){
        return CHAIR_CAPACITY - chairsInUse;
    }

    public synchronized void releaseChair(String customer){
        System.out.println("Customer " + customer + " has been served");
        chairsInUse--;
    }

    public synchronized void acquireChair(String customer) {
        System.out.println("Serving customer: " + customer);
        chairsInUse++;
    }

    public synchronized boolean hasAvailableChairs() {
        return CHAIR_CAPACITY - chairsInUse > 0;
    }
}
