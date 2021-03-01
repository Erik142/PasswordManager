package passwordmanager;

public interface Observer<T> {
	public void update(T observable);
}
