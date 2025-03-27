package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class QueueSharedResource {
    private Queue<String> queue = new LinkedList<String>();
    private static final int MAX_CAPACITY = 5;

    public synchronized void enqueue(String item) throws InterruptedException {
        if(isFull()){
            wait();
        } else {
            queue.add(item);
            notifyAll();
        }
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

}
