package ar.edu.itba.ati.io;

import java.io.DataInputStream;

public class PPMInfo {

    private DataInputStream dis;
    private int width;
    private int height;

    public PPMInfo(DataInputStream dis, int width, int height) {
        this.dis = dis;
        this.width = width;
        this.height = height;
    }

    public DataInputStream getDis() {
        return this.dis;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
