package passwordmanager.model;

/**
 * @author Erik Wahlberger
 * An interface used to represent an observer.
 */
public interface Observer<T> {
	public void update(T observable);
}
