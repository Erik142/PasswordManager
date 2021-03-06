package passwordmanager.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import passwordmanager.model.MainModel;

public class UpdateTableWindowListener implements WindowListener {

    private final MainModel model;

    public UpdateTableWindowListener(MainModel model) {
        this.model = model;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        this.model.updateCredentials();
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    
}
