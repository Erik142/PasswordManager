package passwordmanager.model;

/**
 * An interface used to add observers and update them on certain events
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public interface Observable<T> {
	/**
	 * Add a new observer
	 * @param observer The observer to add
	 */
	public void addObserver(Observer<T> observer);

	/**
	 * Remove an observer from the active observers
	 * @param observer The observer
	 */
	public void removeObserver(Observer<T> observer);
}
