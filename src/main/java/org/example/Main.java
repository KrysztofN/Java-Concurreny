package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        QueueSharedResource queue = new QueueSharedResource();

        QueueThread qt = new QueueThread(queue);
        SchedulerThread st = new SchedulerThread(queue);

        qt.start();
        st.start();

    }
}