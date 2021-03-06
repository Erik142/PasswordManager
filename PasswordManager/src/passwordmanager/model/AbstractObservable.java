package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

/**
 * An abstract implementation of the Observable interface
 * 
 * @author Erik Wahlberger
 */
public abstract class AbstractObservable<T> implements Observable<T> {

    private Collection<Observer<T>> observers;

    public AbstractObservable() {
        observers = new HashSet<Observer<T>>();
    }

    /**
     * Updates all the observers with the specified Observable
     * 
     * @param observable The Observable
     */
    protected void updateObservers(T observable) {
        for (Observer<T> observer : observers) {
            observer.update(observable);
        }
    }

    @Override
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

}
