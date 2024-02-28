package org.graph.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import static org.graph.project.Utils.*;

public class CanvasPanel extends JPanel implements MouseListener{
    private final ArrayList<Peak> peaks;
    public CanvasPanel() {
        peaks = new ArrayList<>();
        this.addMouseListener(this);
    }

    //Generating a new image on screen
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke());

        for (int i = 0;i < peaks.size(); i++){
            Point p = peaks.get(i).getCenter();
            int radius = peaks.get(i).getRadius();
            //draw a peak
            g2.setColor(Color.CYAN);
            g2.fillOval(p.x-radius, p.y-radius, 2*radius, 2*radius);
            //draw a peak caption
            g2.setColor(Color.BLACK);
            String caption = String.valueOf(i + 1);
            g2.drawString(caption, p.x-(radius/2), p.y+(radius/2));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        // Check, if there could be a peak
        if(peaks.stream()
                .allMatch(p-> isDistanceBiggerThan(e.getPoint(), p.getCenter(), p.getRadius()*2))) {
            // Adding new peak to an array
            peaks.add(new Peak(e.getPoint()));
            repaint();
        }
        // Prin
        System.out.println(peaks);
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
