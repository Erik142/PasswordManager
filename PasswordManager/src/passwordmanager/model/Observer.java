package passwordmanager.model;

/**
 * An interface used to represent an observer.
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public interface Observer<T> {
	public void update(T observable);
}
