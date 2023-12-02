package example.java.designpattern;

import java.util.ArrayList;
import java.util.List;

interface Observer {
    public void update(int state);
}

class Observer1 implements Observer {
    @Override
    public void update(int state) {
        System.out.println("Observer1 -> Subject has changed state into: " + state);
    }
}

class Observer2 implements Observer {
    @Override
    public void update(int state) {
        System.out.println("Observer2 -> Subject has changed state into: " + state);
    }
}

class Subject {
    private int state;
    private List<Observer> observers = new ArrayList<Observer>();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyChange(state);
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyChange(int state) {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }
}

public class ObserverPatternDemo {
    public static void main(String[] args) {

        Subject subject = new Subject();
        Observer1 observer1 = new Observer1();
        Observer2 observer2 = new Observer2();

        subject.attach(observer1);
        subject.attach(observer2);
        subject.setState(17);
        subject.detach(observer1);
        subject.detach(observer2);
        subject.setState(10);
    }
}