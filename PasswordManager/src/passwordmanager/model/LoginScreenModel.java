package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

public class LoginScreenModel implements Observable<LoginScreenModel>, Observer<LoginDialogModel> {
	private boolean isViewVisible = false;
	
	private final Collection<Observer<LoginScreenModel>> observers;
	
	public LoginScreenModel() {
		this.observers = new HashSet<Observer<LoginScreenModel>>();
	}
	
	public boolean getViewVisibility() {
		return this.isViewVisible;
	}
	
	public void setViewVisibility(boolean isVisible) {
		this.isViewVisible = isVisible;
		
		updateObservers();
	}

	private void updateObservers() {
		for (Observer<LoginScreenModel> observer : observers) {
			observer.update(this);
		}
	}
	
	@Override
	public void addObserver(Observer<LoginScreenModel> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<LoginScreenModel> observer) {
		observers.remove(observer);
	}

	@Override
	public void update(LoginDialogModel observable) {
		if (observable.getLoggedInStatus()) {
			setViewVisibility(false);
		}
	}
}
