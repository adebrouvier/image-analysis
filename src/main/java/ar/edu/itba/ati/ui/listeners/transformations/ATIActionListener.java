package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionListener;

public abstract class ATIActionListener implements ActionListener {

    private WindowContext windowContext;

    public ATIActionListener(WindowContext windowContext){
        this.windowContext = windowContext;
    }

    public WindowContext getWindowContext() {
        return windowContext;
    }
}
