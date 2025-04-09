package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class ChairsSharedResource {
    private int MAX_CHAIRS = 4;
    private final ArrayList<ChairsEventListener> listeners = new ArrayList<>();
    private HashMap<Integer, String> chairCustomer = new HashMap<>();

    public synchronized void addChairsListener(final ChairsEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    public synchronized void removeChairsListener(final ChairsEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for(ChairsEventListener listener : listeners) {
            listener.onChairsChanged(this);
        }
    }

    public synchronized int availableChairs(){
        return MAX_CHAIRS - chairCustomer.size();
    }

    public synchronized void releaseChair(String customer){
//        System.out.println("Customer " + customer + " has been served");

        Integer chairToRelease = null;
        for(Integer chairId : chairCustomer.keySet()) {
            if(chairCustomer.get(chairId).equals(customer)) {
                chairToRelease = chairId;
                break;
            }
        }
        if (chairToRelease != null) {
            chairCustomer.remove(chairToRelease);
//            System.out.println("Customer " + customer + " has left chair " + chairToRelease);
            notifyListeners();
        }
    }

    public synchronized void acquireChair(String customer) {
//        System.out.println("Serving customer: " + customer);
        int chairId = 0;
        while(chairCustomer.containsKey(chairId)) {
            chairId++;
        }
        chairCustomer.put(chairId, customer);
//        chairCapacity--;
//        System.out.println("Serving customer: " + customer + " in chair " + chairId);
        notifyListeners();
    }

    public synchronized boolean hasAvailableChairs() {
        return chairCustomer.size() < MAX_CHAIRS;
    }

    public HashMap<Integer, String> getChairsCopy() {
        return new HashMap<>(chairCustomer);
    }
}
