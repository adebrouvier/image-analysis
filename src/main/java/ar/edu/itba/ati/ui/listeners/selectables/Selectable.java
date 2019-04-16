package ar.edu.itba.ati.ui.listeners.selectables;

import java.awt.event.MouseEvent;

public interface Selectable {

    void onMousePressed(MouseEvent mouseEvent);

    void onMouseReleased(MouseEvent mouseEvent);
}
