package com.bluecatcode.jeff.agent;

import java.util.Observable;
import java.util.Observer;

public enum Counter implements Observer {

    INSTANCE;

    private int n;

    @Override
    public void update(Observable o, Object arg) {
        n++;
    }

    public int count() {
        return n;
    }

    public static Notifier newNotifier() {
        return new Notifier(INSTANCE);
    }

    public static class Notifier extends Observable {

        public Notifier(Observer first) {
            addObserver(first);
        }

        public void send() {
            setChanged();
            notifyObservers();
        }
    }
}