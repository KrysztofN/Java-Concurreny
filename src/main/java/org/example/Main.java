package org.example;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        QueueSharedResource queue = new QueueSharedResource();
        ChairsSharedResource chairs = new ChairsSharedResource();
        HairDressersSharedResource hairdresser = new HairDressersSharedResource();
        GUIHandler gui = new GUIHandler(queue, chairs, hairdresser);
        QueueThread qt = new QueueThread(queue);
        qt.start();

        SchedulerThread[] st = new SchedulerThread[4];
        for(int i=0; i<4; i++){
            st[i] = new SchedulerThread(queue, chairs, hairdresser);
            st[i].start();
        }
    }
}