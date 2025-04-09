package org.example;

import java.util.ArrayList;

public class HairDressersSharedResource {
    private int hairdressersS = 2;
    private int hairdressersM = 2;
    private int hairdressersG = 2;
    private final ArrayList<HairdressersEventListener> listeners = new ArrayList<>();

    public synchronized void addHairdressersListener(final HairdressersEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    public synchronized void removeHairdressersListener(final HairdressersEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for(HairdressersEventListener listener : listeners) {
            listener.onHairDresserChange(this);
        }
    }

    public synchronized void decrementHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> hairdressersS--;
            case "M" -> hairdressersM--;
            case "G" -> hairdressersG--;
        }
        notifyListeners();
    }

    public synchronized void incrementHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> hairdressersS++;
            case "M" -> hairdressersM++;
            case "G" -> hairdressersG++;
        }
        notifyListeners();
    }

    public synchronized boolean hasAvailableHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> {
                return hairdressersS > 0;
            }
            case "M" -> {
                return hairdressersM > 0;
            }
            case "G" -> {
                return hairdressersG > 0;
            }
        }
        return false;
    }

    public synchronized int availableHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> {
                return hairdressersS;
            }
            case "M" -> {
                return hairdressersM;
            }
            case "G" -> {
                return hairdressersG;
            }
        }
        return 0;
    }
}
