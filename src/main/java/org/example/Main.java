package org.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        QueueSharedResource queue = new QueueSharedResource();
        ChairsSharedResource chairs = new ChairsSharedResource();
        HairDressersSharedResource hairdresser = new HairDressersSharedResource();

        QueueThread qt = new QueueThread(queue);
        qt.start();

        SchedulerThread[] st = new SchedulerThread[4];
        for(int i=0; i<4; i++){
            st[i] = new SchedulerThread(queue, chairs, hairdresser);
            st[i].start();
        }


//        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
//        Terminal terminal = null;
//        try {
//            terminal = defaultTerminalFactory.createTerminal();
//            terminal.setForegroundColor(TextColor.ANSI.GREEN);
//            AnimatedText animatedText = new AnimatedText(terminal);
//            animatedText.animateText("Jestem Simon, buahaha");
//            Thread.sleep(2000);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (terminal != null) {
//                try {
//                    terminal.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}