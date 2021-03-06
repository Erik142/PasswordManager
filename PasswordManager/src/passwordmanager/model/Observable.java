package passwordmanager.model;

/**
 * @author Erik Wahlberger
 * An interface used to add observers and update them on certain events
 */
public interface Observable<T> {
	public void addObserver(Observer<T> observer);
	public void removeObserver(Observer<T> observer);
}
