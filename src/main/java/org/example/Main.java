package org.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

//        QueueSharedResource queue = new QueueSharedResource();
//
//        QueueThread qt = new QueueThread(queue);
//        SchedulerThread st = new SchedulerThread(queue);
//
//        qt.start();
//        st.start();
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal = null;
        try {
            terminal = defaultTerminalFactory.createTerminal();
            terminal.setForegroundColor(TextColor.ANSI.GREEN);
            AnimatedText animatedText = new AnimatedText(terminal);
            animatedText.animateText("Hello World");
            Thread.sleep(2000);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (terminal != null) {
                try {
                    terminal.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}