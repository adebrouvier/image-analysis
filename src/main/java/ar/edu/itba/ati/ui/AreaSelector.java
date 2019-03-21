package ar.edu.itba.ati.ui;

public class AreaSelector {

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public void setStart(int x, int y){
        startX = x;
        startY = y;
    }

    public void setEnd(int x, int y){
        endX = x;
        endY = y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}
