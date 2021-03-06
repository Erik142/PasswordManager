package passwordmanager.model;

public interface Observer<T> {
	public void update(T observable);
}
