package org.graph.project;

import java.awt.*;

public class Peak {
    //base
    private int radius;
    private Point center;
    private int id;
    //for
    private boolean isSelected;
    //for dfs
    private boolean isUsed;

    public Peak() {
    }

    public Peak(Point center, int id) {
        this.center = center;
        this.id = id;
        this.radius = 20;
        this.isSelected = false;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public boolean getSelected() {return isSelected;}

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void incId() {
        this.id++;
    }

    public void decId(){
        this.id--;
    }
}
