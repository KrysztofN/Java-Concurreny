package org.example;

public class ChairsSharedResource {
    private static final int CHAIR_CAPACITY = 4;
    private int chairsInUse = 0;

    public synchronized int availableChairs(){
        return CHAIR_CAPACITY - chairsInUse;
    }

    public synchronized void releaseChair(){
        if (chairsInUse > 0) {
            chairsInUse--;
        }
    }

    public synchronized boolean acquireChair() {
        if (chairsInUse >= CHAIR_CAPACITY) {
            return false;
        }
        chairsInUse++;
        return true;
    }

    public boolean hasAvailableChairs() {
        return CHAIR_CAPACITY - chairsInUse > 0;
    }

}
