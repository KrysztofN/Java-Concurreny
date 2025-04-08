package org.example;

public class HairDressersSharedResource {
    private int hairdressersS = 2;
    private int hairdressersM = 2;
    private int hairdressersG = 2;

    public synchronized void decrementHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> hairdressersS--;
            case "M" -> hairdressersM--;
            case "G" -> hairdressersG--;
        }
    }

    public synchronized void incrementHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> hairdressersS++;
            case "M" -> hairdressersM++;
            case "G" -> hairdressersG++;
        }
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
