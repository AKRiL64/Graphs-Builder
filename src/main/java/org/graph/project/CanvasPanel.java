package org.graph.project;

import com.sun.source.tree.WhileLoopTree;
import org.graph.project.enums.CursorActionState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.Thread.sleep;
import static org.graph.project.Utils.*;
import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class CanvasPanel extends JPanel implements MouseListener, KeyListener {
    public HashMap<Peak,Edge> edgesHashMap;
    private CursorActionState cursorActionState = CursorActionState.ADDING_AND_DELETING;
    public int currentNumberOfPeaks = 0;
    private Peak selectedMovingPeak;
    public Peak startingDfsPeak;
    public JLabel textPanel;
    public boolean isThereSelectedPeak;
    public HashMap<Peak,Peak> bfsPeakWaysPairs = new HashMap<>();
    private boolean movingModeOn = false;
    public boolean animationStarted = false;
    public boolean searchSelectMode = false;
    public int curSearchPeak;
    private Peak startingSearchPeak;
    private Peak peakToFind;
    public int currentNumberOfAnimationPeaks = 0;
    public Peak selectedPeak;
    private boolean endSearch;
    private int counter;
    public JButton buttonSelected;
    public CanvasPanel() {
        edgesHashMap = new HashMap<>();
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    //Generating a new image frame on screen
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Courier New", Font.BOLD, 20));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke());
        counter = 0;
        edgesHashMap.forEach((key, value)-> {
            g2.setColor(Color.BLACK);
            value.getConnectedPeaks().forEach((keySecond) -> {
                g2.drawLine(key.getCenter().x, key.getCenter().y,
                        keySecond.getCenter().x, keySecond.getCenter().y);
            });
        });
        edgesHashMap.forEach((key, value)-> {
            Point p = key.getCenter();
            int radius = key.getRadius();
            if (!animationStarted) {
                if (key.getSelected()) {
                    g2.setColor(Color.CYAN);
                } else {
                    g2.setColor(Color.GREEN);
                }
            }
            else {
                switch (key.getDfsCounter()){
                    case 0: {
                        g2.setColor(Color.GREEN);
                        break;
                    }
                    case 1: {
                        g2.setColor(Color.PINK);
                        break;
                    }
                    case 2: {
                        g2.setColor(Color.MAGENTA);
                        break;
                    }
                }
            }
            g2.fillOval(p.x-radius, p.y-radius, 2*radius, 2*radius);
            //draw a peak caption
            g2.setColor(Color.BLACK);
            String caption = String.valueOf(key.getId());
            switch (caption.length()){
                case 1:{
                    g2.drawString(caption, p.x-1-radius/4, p.y+radius/4);
                    break;
                }
                case 2:{
                    g2.drawString(caption, p.x-radius/2, p.y+radius/4);
                    break;
                }
                default:{
                    g2.drawString(caption, p.x-radius*3/4, p.y+radius/4);
                }
            }
            counter=counter+1;
        });
    }

    public void bfsAnimation(HashSet<Peak> curBfsPeaks) throws InterruptedException {
        HashSet<Peak> nextBfsPeaks = new HashSet<>();
        curBfsPeaks.forEach((p)->{
            p.setDfsCounter(2);
            edgesHashMap.get(p).getConnectedPeaks().forEach((value)->{
                if (value.getDfsCounter()==0){
                    nextBfsPeaks.add(value);
                    value.setDfsCounter(1);
                    currentNumberOfAnimationPeaks++;
                }
            });
        });
        repaint();
        sleep(1000);
        curBfsPeaks.forEach((p)->{
            p.setDfsCounter(2);
        });
        repaint();
        if (!nextBfsPeaks.isEmpty()){
            bfsAnimation(nextBfsPeaks);
        }
        else {
            repaint();
            if (currentNumberOfAnimationPeaks ==currentNumberOfPeaks){
                textPanel.setText("CONNECTED (✿◡‿◡)");
            }
            else{
                textPanel.setText("NOT CONNECTED (っ °Д °;)っ");
            }
            sleep(4000);
            textPanel.setText("Waiting to do smth (*^▽^*)");
            animationStarted=false;
            edgesHashMap.forEach((key, value)->{
                key.setDfsCounter(0);
            });
            repaint();
        }
    }

    private void bfsSearchAnimation(HashSet<Peak> curBfsPeaks) throws InterruptedException {
        HashSet<Peak> nextBfsPeaks = new HashSet<>();
        curBfsPeaks.forEach((p) -> {
            p.setDfsCounter(2);
            edgesHashMap.get(p).getConnectedPeaks().forEach((value) -> {
                if (value.getDfsCounter() == 0) {
                    bfsPeakWaysPairs.put(value,p);
                    nextBfsPeaks.add(value);
                    value.setDfsCounter(1);
                    currentNumberOfAnimationPeaks++;
                    if (value == peakToFind){
                        endSearch=true;
                    }
                }
            });
        });
        repaint();
        sleep(1000);
        curBfsPeaks.forEach((p) -> {
            p.setDfsCounter(2);
        });
        repaint();
        if (!nextBfsPeaks.isEmpty() && !endSearch) {
            bfsSearchAnimation(nextBfsPeaks);
        } else {
            if (nextBfsPeaks.isEmpty() && !endSearch){
                textPanel.setText("No ways to the destination ⊙﹏⊙∥");
            }
            else{
                String tString = new String("");
                Peak key = bfsPeakWaysPairs.get(peakToFind);
                tString = tString+String.valueOf(peakToFind.getId());

                while (key!=bfsPeakWaysPairs.get(key)){
                    System.out.println(key.getId());
                    tString = String.valueOf(key.getId())+"-"+tString;
                    key = bfsPeakWaysPairs.get(key);
                }

                tString = String.valueOf(key.getId())+"-"+tString;
                textPanel.setText(tString);
            }
            repaint();
            sleep(4000);
            textPanel.setText("Waiting to do smth (*^▽^*)");
            animationStarted = false;
            edgesHashMap.forEach((key, value) -> {
                key.setDfsCounter(0);
            });
            repaint();
        }
    }
    public void dfsAnimation(Peak peak) throws InterruptedException {
        peak.setDfsCounter(1);
        currentNumberOfAnimationPeaks++;
        repaint();
        edgesHashMap.get(peak).getConnectedPeaks().forEach((value)->{
            if (value.getDfsCounter()==0) {
                try {
                    sleep(1000);
                    dfsAnimation(value);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        sleep(1000);
        peak.setDfsCounter(2);
        repaint();
        if (peak == startingDfsPeak){
            if (currentNumberOfAnimationPeaks==currentNumberOfPeaks){
                textPanel.setText("CONNECTED (✿◡‿◡)");
            }
            else{
                textPanel.setText("NOT CONNECTED (っ °Д °;)っ");
            }
            sleep(4000);
            textPanel.setText("Waiting to do smth (*^▽^*)");
            edgesHashMap.forEach((key, value)-> {
                key.setDfsCounter(0);
            });
            animationStarted = false;
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2 && !animationStarted) {
            edgesHashMap.forEach((key, value)->{
                if (!isDistanceBetweenPointsBiggerThan(e.getPoint(),
                        key.getCenter(),key.getRadius())){
                    selectedMovingPeak = key;
                    key.setRadius(18);
                    cursorActionState = CursorActionState.MOVING;
                    isThereSelectedPeak = false;
                    if (selectedPeak != null) {
                        selectedPeak.setSelected(false);
                    }
                }
            });
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Checking lmb
        if (!animationStarted) {
            if (cursorActionState == CursorActionState.ADDING_AND_DELETING) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    edgesHashMap.forEach((key, value) -> {
                        if (!isDistanceBetweenPointsBiggerThan(e.getPoint(),
                                key.getCenter(), key.getRadius())) {
                            if (!isThereSelectedPeak) {
                                selectedPeak = key;
                                key.setSelected(true);
                                isThereSelectedPeak = true;
                            } else {
                                isThereSelectedPeak = false;
                                if (selectedPeak != null) {
                                    selectedPeak.setSelected(false);
                                }
                                key.setSelected(false);
                                value.addPeak(selectedPeak);
                                edgesHashMap.get(selectedPeak).addPeak(key);
                            }
                        }
                    });
                    if (edgesHashMap.entrySet().stream().allMatch(p -> isDistanceBetweenPointsBiggerThan(
                            e.getPoint(), p.getKey().getCenter(), p.getKey().getRadius() * 3))) {
                        if (selectedPeak != null) {
                            selectedPeak.setSelected(false);
                        }
                        currentNumberOfPeaks++;
                        edgesHashMap.put(new Peak(e.getPoint(), currentNumberOfPeaks), new Edge());
                        isThereSelectedPeak = false;
                    }
//                edgesHashMap.forEach((key, value) ->{
//
//                });
                    //Create new peak if not touching any other
                }
                //checking rmb
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedPeak != null) {
                        selectedPeak.setSelected(false);
                    }
                    isThereSelectedPeak = false;
                    for (Map.Entry<Peak, Edge> entry : edgesHashMap.entrySet()) {
                        Peak key = entry.getKey();
                        Edge value = entry.getValue();
                        if (!isDistanceBetweenPointsBiggerThan(e.getPoint(), key.getCenter(), key.getRadius())) {
                            edgesHashMap.forEach((keyDelete, valueDelete) -> {
                                int tempID = key.getId();
                                if (keyDelete.getId() > tempID) {
                                    keyDelete.decId();
                                }
                                valueDelete.getConnectedPeaks().remove(key);
                            });
                            edgesHashMap.remove(key);
                            currentNumberOfPeaks--;
                            break;
                        }
                        value.getConnectedPeaks().removeIf(keyDelete ->
                                !isDistanceToLineSectionBiggerThan(key.getCenter(),
                                        keyDelete.getCenter(), e.getPoint(), HITBOX_DELTA));
                    }
                }
            } else {
                cursorActionState = CursorActionState.ADDING_AND_DELETING;
                for (Map.Entry<Peak, Edge> entry : edgesHashMap.entrySet()) {
                    Peak key = entry.getKey();
                    if (!isDistanceBetweenPointsBiggerThan(e.getPoint(),
                            key.getCenter(), key.getRadius())) {
                        selectedMovingPeak.setCenter(new Point(e.getPoint().x +
                                (key.getCenter().x - e.getPoint().x) / (key.getCenter().x - e.getPoint().x)
                                        * key.getRadius(), e.getPoint().y + (key.getCenter().y - e.getPoint().y) / (key.getCenter().y - e.getPoint().y)
                                * key.getRadius()
                        ));
                        break;
                    }
                }
                selectedMovingPeak.setRadius(20);
                selectedMovingPeak.setCenter(e.getPoint());
            }
            repaint();
        }
        else if (searchSelectMode){
            for (Map.Entry<Peak, Edge> entry : edgesHashMap.entrySet()) {
                Peak key = entry.getKey();
                Edge value = entry.getValue();
                if (!isDistanceBetweenPointsBiggerThan(e.getPoint(),
                        key.getCenter(), key.getRadius())) {
                    if (curSearchPeak==1){
                        startingSearchPeak=key;
                        System.out.println(startingSearchPeak);
                        curSearchPeak++;
                        textPanel.setText("Select vertex 2 ┌( ಠ_ಠ)┘");
                    }
                    else{
                        textPanel.setText("Calculating.. φ(゜▽゜*)♪");
                        Thread animSearch = new Thread(()->{
                            try {
                                endSearch=false;
                                peakToFind=key;
                                searchSelectMode=false;
                                if (bfsPeakWaysPairs != null){
                                    bfsPeakWaysPairs.clear();
                                }
                                bfsPeakWaysPairs.put(startingSearchPeak,startingSearchPeak);
                                HashSet<Peak> tPeakSet = new HashSet<Peak>();
                                System.out.println(tPeakSet);
                                tPeakSet.add(startingSearchPeak);
                                System.out.println(tPeakSet);
                                if (peakToFind==startingSearchPeak){
                                    endSearch=true;
                                }
                                bfsSearchAnimation(tPeakSet);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        animSearch.start();
                    }
                }
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {
        if (cursorActionState == CursorActionState.MOVING){
            selectedMovingPeak.setRadius(20);
            selectedMovingPeak.setCenter(e.getPoint());
            cursorActionState=CursorActionState.ADDING_AND_DELETING;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("TYPE");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("PRESS");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("RELEASE");
    }
}
