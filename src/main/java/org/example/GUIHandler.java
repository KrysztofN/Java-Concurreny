package org.example;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GUIHandler implements QueueEventListener, ChairsEventListener, HairdressersEventListener {
    private Screen screen;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private volatile boolean running = true;
    private final int maxQueueSize = 5;
    private QueueSharedResource queueResource;
    private ChairsSharedResource chairsResource;
    private HairDressersSharedResource hairdressersResource;
    private Terminal terminal;

    public GUIHandler(QueueSharedResource queueResource, ChairsSharedResource chairsResource, HairDressersSharedResource hairdresserResource) {
        this.queueResource = queueResource;
        this.chairsResource = chairsResource;
        this.hairdressersResource = hairdresserResource;

        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.setCursorPosition(null);
            screen.clear();
            queueResource.addQueueListener(this);
            chairsResource.addChairsListener(this);
            hairdresserResource.addHairdressersListener(this);
            executor.submit(this::pollForInput);

            drawRandomPixels();
            updateQueueDisplay();
            updateChairDisplay();
            updateHairdresserDisplay();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawRandomPixels() {
        synchronized (screen) {
            try {
                screen.clear();

                Random random = new Random();
                TerminalSize terminalSize = screen.getTerminalSize();
                for(int column = 0; column < terminalSize.getColumns(); column++) {
                    for(int row = 0; row < terminalSize.getRows(); row++) {
                        screen.setCharacter(column, row, new TextCharacter(
                                ' ',
                                TextColor.ANSI.DEFAULT,
                                TextColor.ANSI.values()[random.nextInt(TextColor.ANSI.values().length)]));
                    }
                }

                String sizeLabel = "Witaj w salonie fryzjerskim";
                int centerX = terminalSize.getColumns() / 2;
                int centerY = terminalSize.getRows() / 2;
                TerminalPosition labelBoxTopLeft = new TerminalPosition(centerX - sizeLabel.length()/2, centerY);
                TerminalSize labelBoxSize = new TerminalSize(sizeLabel.length() + 2, 3);
                TerminalPosition labelBoxTopRightCorner = labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 1);
                TextGraphics textGraphics = screen.newTextGraphics();
                textGraphics.fillRectangle(labelBoxTopLeft, labelBoxSize, ' ');

                textGraphics.drawLine(
                        labelBoxTopLeft.withRelativeColumn(1),
                        labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 2),
                        Symbols.DOUBLE_LINE_HORIZONTAL);
                textGraphics.drawLine(
                        labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(1),
                        labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(labelBoxSize.getColumns() - 2),
                        Symbols.DOUBLE_LINE_HORIZONTAL);

                textGraphics.setCharacter(labelBoxTopLeft, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
                textGraphics.setCharacter(labelBoxTopLeft.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
                textGraphics.setCharacter(labelBoxTopLeft.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
                textGraphics.setCharacter(labelBoxTopRightCorner, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
                textGraphics.setCharacter(labelBoxTopRightCorner.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
                textGraphics.setCharacter(labelBoxTopRightCorner.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);
                textGraphics.putString(labelBoxTopLeft.withRelative(1, 1), sizeLabel);

                screen.refresh();
                Thread.sleep(3000);
                screen.clear();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onQueueChanged(QueueSharedResource queue) {
        executor.submit(this::updateQueueDisplay);
    }

    @Override
    public void onChairsChanged(ChairsSharedResource chair) {
        executor.submit(this::updateChairDisplay);
    }

    @Override
    public void onHairDresserChange(HairDressersSharedResource hairdresser) {
        executor.submit(this::updateHairdresserDisplay);
    }

    private void updateQueueDisplay() {
        synchronized (screen) {
            try {
                TerminalSize size = screen.getTerminalSize();
                int startX = size.getColumns()/ 2;
                int startY = size.getRows() / 3;

                String header = "QUEUE STATUS";
                for(int i=0; i < header.length(); i++) {
                    screen.setCharacter(startX + i, startY - 2, new TextCharacter(header.charAt(i)));
                }

                screen.setCharacter(startX, startY, new TextCharacter('['));
                screen.setCharacter(startX + maxQueueSize * 3 + 1, startY, new TextCharacter(']'));

                for(int i=1; i < maxQueueSize * 3 + 1; i++) {
                    screen.setCharacter(startX + i, startY, new TextCharacter('·'));
                }


                Queue<String> queueCopy = queueResource.getQueueCopy();
                int i = 0;
                for (String item : queueCopy) {
                    char displayChar = item.charAt(0);
                    TextColor itemColor;

                    switch (displayChar) {
                        case 'S':
                            itemColor = TextColor.ANSI.RED;
                            break;
                        case 'M':
                            itemColor = TextColor.ANSI.GREEN;
                            break;
                        case 'G':
                            itemColor = TextColor.ANSI.BLUE;
                            break;
                        default:
                            itemColor = TextColor.ANSI.WHITE;
                    }
                    for(int j = 0; j < item.length(); j++) {
                        screen.setCharacter(startX + i + 1 + j, startY, new TextCharacter(item.charAt(j), itemColor, TextColor.ANSI.BLACK));
                    }
                    i += item.length();
                }

                screen.refresh();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateChairDisplay() {
        HashMap<Integer, String> chairsCopy;
        chairsCopy = chairsResource.getChairsCopy();

        synchronized (screen) {
            try {
                TerminalSize size = screen.getTerminalSize();

                final int MAX_CHAIRS = 4;
                final int CHAIR_WIDTH = 5;
                final int CHAIR_HEIGHT = 3;
                final int CHAIR_SPACING = 4;

                int startX = size.getColumns() / 3;
                int initialY = size.getRows() / 3;

                String header = "CHAIRS STATUS";
                for (int i = 0; i < header.length(); i++) {
                    screen.setCharacter(startX + i, initialY - 2, new TextCharacter(header.charAt(i)));
                }

                for (int chairId = 0; chairId < MAX_CHAIRS; chairId++) {
                    int chairY = initialY + (chairId * CHAIR_SPACING);

                    drawChair(screen, startX, chairY, CHAIR_WIDTH, CHAIR_HEIGHT);

                    screen.setCharacter(startX - 2, chairY + 1,
                            new TextCharacter(Character.forDigit(chairId, 10)));

                    if (chairsCopy.containsKey(chairId)) {
                        String customer = chairsCopy.get(chairId);
                        char displayChar = customer.charAt(0);

                        TextColor itemColor;
                        switch (displayChar) {
                            case 'S':
                                itemColor = TextColor.ANSI.RED;
                                break;
                            case 'M':
                                itemColor = TextColor.ANSI.GREEN;
                                break;
                            case 'G':
                                itemColor = TextColor.ANSI.BLUE;
                                break;
                            default:
                                itemColor = TextColor.ANSI.WHITE;
                        }
                        int customerX = startX + CHAIR_WIDTH/2 - customer.length()/2;

//                        Czyszczenie
                        for(int i = 0; i < 3; i++) {
                            screen.setCharacter(customerX + i, chairY + CHAIR_HEIGHT/2,
                                    new TextCharacter(' ', itemColor, TextColor.ANSI.BLACK));
                        }

//                        Wyswietlanie
                        for(int i = 0; i < customer.length(); i++) {
                            screen.setCharacter(customerX + i, chairY + CHAIR_HEIGHT/2,
                                    new TextCharacter(customer.charAt(i), itemColor, TextColor.ANSI.BLACK));
                        }
                    }
                }
                screen.refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateHairdresserDisplay(){
        synchronized (screen) {
            try {
                TerminalSize size = screen.getTerminalSize();
                int startX = size.getColumns()/6 ;
                int startY = size.getRows() / 3;

                String header = "HAIRDRESSERS STATUS";
                for(int i=0; i < header.length(); i++) {
                    screen.setCharacter(startX + i, startY - 2, new TextCharacter(header.charAt(i)));
                }
                int hairdressersS = hairdressersResource.availableHairdressers("S");
                int hairdressersM = hairdressersResource.availableHairdressers("M");
                int hairdressersG = hairdressersResource.availableHairdressers("G");

                int offset = 0;
                for(int i=0; i < hairdressersS; i++) {
                    TextGraphics tg = screen.newTextGraphics();
                    tg.putString(startX, startY + offset, "HS");
                    offset++;
                }

                for(int j=0; j < hairdressersM; j++) {
                    TextGraphics tg = screen.newTextGraphics();
                    tg.putString(startX, startY + offset, "HM");
                    offset++;
                }

                for(int k=0; k < hairdressersG; k++) {
                    TextGraphics tg = screen.newTextGraphics();
                    tg.putString(startX, startY + offset, "HG");
                    offset++;
                }
                screen.refresh();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void drawChair(Screen screen, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            screen.setCharacter(x + i, y, new TextCharacter('■'));
            screen.setCharacter(x + i, y + height - 1, new TextCharacter('■'));
        }

        for (int j = 0; j < height; j++) {
            screen.setCharacter(x, y + j, new TextCharacter('■'));
            screen.setCharacter(x + width - 1, y + j, new TextCharacter('■'));
        }
    }

    private void pollForInput() {
        try {
            while (running) {
                KeyStroke keyStroke = terminal.pollInput();
                if (keyStroke != null && (keyStroke.getKeyType() == KeyType.Escape || keyStroke.getKeyType() == KeyType.EOF)) {
                    stop();
                    break;
                }
            }
        } catch (IOException e) {
            if (running) e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        running = false;
        queueResource.removeQueueListener(this);
        chairsResource.removeChairsListener(this);
        hairdressersResource.removeHairdressersListener(this);
        executor.shutdownNow();
        screen.close();
        terminal.close();
    }
}