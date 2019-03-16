package ar.edu.itba.ati;

import java.io.DataInputStream;

public class PPMReaderInfo {

    private DataInputStream dis;
    private int width;
    private int height;

    public PPMReaderInfo(DataInputStream dis, int width, int height){
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
