package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class QueueSharedResource {
    private Queue<String> queue = new LinkedList<String>();

    public synchronized int enqueue(String item) {
        if(queue.size() < 10) {
            queue.add(item);
            return 0;
        } else {
            return 1;
        }
    }

    public synchronized boolean isFull(){
        if(queue.size() < 5) {
            return false;
        } return true;
    }

    public synchronized String dequeue() {
        if(queue.isEmpty()) {
            return null;
        } else {
            return queue.poll();
        }
    }

    public String peek() {
        if(queue.isEmpty()) {
            return null;
        } else{
            return queue.peek();
        }
    }

    public int size() {
        return queue.size();
    }

}
