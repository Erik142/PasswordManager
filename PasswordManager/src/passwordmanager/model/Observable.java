package passwordmanager.model;

public interface Observable<T> {
	public void addObserver(Observer<T> observer);
	public void removeObserver(Observer<T> observer);
}
