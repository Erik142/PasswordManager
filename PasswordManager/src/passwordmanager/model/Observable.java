package passwordmanager.model;

/**
 * An interface used to add observers and update them on certain events
 * 
 * @author Erik Wahlberger
 */
public interface Observable<T> {
	public void addObserver(Observer<T> observer);

	public void removeObserver(Observer<T> observer);
}
