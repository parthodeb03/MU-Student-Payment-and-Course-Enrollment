package com.mu.observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final List<Observer> observers;

    public NotificationService() {

        observers = new ArrayList<>();

        // Automatically register one observer
        observers.add(new StudentObserver());

    }

    public void addObserver(Observer observer) {

        observers.add(observer);

    }

    public void removeObserver(Observer observer) {

        observers.remove(observer);

    }

    public void notifyObservers(String message) {

        for (Observer observer : observers) {

            observer.update(message);

        }

    }

}