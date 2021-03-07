package passwordmanager.model;

/**
 * An interface used to add observers and update them on certain events
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public interface Observable<T> {
	public void addObserver(Observer<T> observer);

	public void removeObserver(Observer<T> observer);
}
