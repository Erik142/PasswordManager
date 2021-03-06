package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Erik Wahlberger
 * An abstract implementation of the Observable interface
 */
public class AbstractObservable<T> implements Observable<T> {

    private Collection<Observer<T>> observers;

    private final T observable;

    public AbstractObservable(T observable) {
        observers = new HashSet<Observer<T>>();
        this.observable = observable;
    }

    /**
     * Updates all the observers with the specified Observable
     * @param observable The Observable
     */
    protected void updateObservers() {
        for (Observer<T> observer: observers) {
            observer.update(this.observable);
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
