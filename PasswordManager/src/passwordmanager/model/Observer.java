package passwordmanager.model;

/**
 * An interface used to represent an observer.
 * 
 * @author Erik Wahlberger
 */
public interface Observer<T> {
	public void update(T observable);
}
