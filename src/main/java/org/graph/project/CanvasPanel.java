package org.graph.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import static org.graph.project.Utils.*;


public class CanvasPanel extends JPanel implements MouseListener{
    public final ArrayList<Peak> peaks;
    public final ArrayList<Edge> edges;

    private boolean isThereSelectedPeak;
    private int selectedPeakNumber;

    public CanvasPanel() {
        peaks = new ArrayList<>();
        edges = new ArrayList<>();
        this.addMouseListener(this);
    }

    //Generating a new image frame on screen
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //g2.setFont();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke());

        for (Edge edge : edges) {
            g2.setColor(Color.BLACK);
            g2.drawLine(edge.getPeakOne().getCenter().x, edge.getPeakOne().getCenter().y,
                    edge.getPeakTwo().getCenter().x, edge.getPeakTwo().getCenter().y);
        }
        for (int i = 0;i < peaks.size(); i++){
            Point p = peaks.get(i).getCenter();
            int radius = peaks.get(i).getRadius();
            //draw a peak
            if (peaks.get(i).getSelected()){
                g2.setColor(Color.CYAN);
            }
            else {
                g2.setColor(Color.GREEN);
            }
            g2.fillOval(p.x-radius, p.y-radius, 2*radius, 2*radius);
            //draw a peak caption
            g2.setColor(Color.BLACK);
            String caption = String.valueOf(i + 1);
            g2.drawString(caption, p.x-(radius/2), p.y);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        // Checking lmb
        if(e.getButton() == MouseEvent.BUTTON1) {
            for (int i = 0; i < peaks.size(); i++) {
                if (!isDistanceBetweenPointsBiggerThan(e.getPoint(), peaks.get(i).getCenter(), peaks.get(i).getRadius())) {
                    if (!isThereSelectedPeak) {
                        selectedPeakNumber = i;
                        peaks.get(i).setSelected(true);
                        isThereSelectedPeak = true;
                    }
                    else {
                        isThereSelectedPeak = false;
                        peaks.get(selectedPeakNumber).setSelected(false);
                        if (isThereNoAbsEqualEdge(edges, peaks, selectedPeakNumber, i)){
                            edges.add(new Edge(peaks.get(selectedPeakNumber), peaks.get(i)));
                        }
                    }
                }
            }
            //Create new peak if not touching any other
            if (peaks.stream()
                    .allMatch(p -> isDistanceBetweenPointsBiggerThan(
                            e.getPoint(), p.getCenter(), p.getRadius() * 3))) {
                // Adding new peak to an array
                peaks.add(new Peak(e.getPoint()));
                peaks.get(selectedPeakNumber).setSelected(false);
                isThereSelectedPeak = false;
            }
        }
        //checking rmb
        else if (e.getButton() == MouseEvent.BUTTON3){
            if (!peaks.isEmpty()){
                peaks.get(selectedPeakNumber).setSelected(false);
            }
            isThereSelectedPeak = false;
            for (int i = 0; i < peaks.size(); i++){
                if (!isDistanceBetweenPointsBiggerThan(e.getPoint(), peaks.get(i).getCenter(), peaks.get(i).getRadius())){
                    selectedPeakNumber=0;
                    ArrayList<Integer> toDelete = new ArrayList<>();
                    for (int j = 0; j < edges.size(); j++){
                        if (edges.get(j).getPeakOne() == peaks.get(i) || edges.get(j).getPeakTwo() == peaks.get(i)){
                            toDelete.add(j);
                        }
                    }
                    Collections.reverse(toDelete);
                    for (Integer j: toDelete){
                        edges.remove(j.intValue());
                    }
                    peaks.remove(i);
                    break;
                }
            }
            for (int i = 0; i<edges.size(); i++){
                if (!isDistanceToLineSectionBiggerThan(edges.get(i).getPeakOne().getCenter(),
                        edges.get(i).getPeakTwo().getCenter(), e.getPoint(), HITBOX_DELTA)){
                    edges.remove(i);
                    break;
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
