package passwordmanager.model;

/**
 * An interface used to represent an observer.
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public interface Observer<T> {
	/**
	 * Update the observer with new values from the observable
	 * @param observable The observable which contains updated values
	 */
	public void update(T observable);
}
