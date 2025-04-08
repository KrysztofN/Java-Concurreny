package org.example;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class QueueSharedResource {
    private final Queue<String> queue = new LinkedList<String>();
    private static final int MAX_CAPACITY = 5;

    public synchronized void enqueue(String item) throws InterruptedException {
        while(isFull()){
            wait();
        }
        queue.add(item);
        System.out.println("Enqueued " + item + " People in queue: " + queue.size());
        notifyAll();

    }

    public synchronized boolean isFull(){
        if(queue.size() < MAX_CAPACITY) {
            return false;
        } return true;
    }

    public synchronized String dequeue() throws InterruptedException {
        while(queue.isEmpty()) {
            wait();
        }

        String item =  queue.poll();
        System.out.println("People in queue: " + queue.size() + " Dequeued " + item);
        notifyAll();
        return item;

    }

    public synchronized String peek() {
        if(queue.isEmpty()) {
            return null;
        } else{
            return queue.peek();
        }
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized String returnFirstOccurence(HashMap<String, Boolean> hairdressersAvailable) {
        for(String customer : queue){
            if(hairdressersAvailable.get(customer.substring(0, 1))) return customer;
        }
        return "";
    }

    public synchronized void removeFromFifo(String customer) {
        queue.remove(customer);
    }
}
