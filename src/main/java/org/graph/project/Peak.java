package org.graph.project;

import java.awt.*;

public class Peak {
    private int radius;
    private Point center;
    private boolean isSelected;
    public Peak(Point center) {
        this.center = center;
        this.radius = 15;
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
}
