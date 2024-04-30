package org.graph.project;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashSet;
import java.util.Map;

public class DialogMenuButtonsPanel extends JPanel {
    JButton buttonOpen = new JButton("Open");
    JButton buttonSave = new JButton("Save");
    JButton buttonClose = new JButton("Exit");
    JButton buttonDFS = new JButton("DFS");
    JButton buttonBFS = new JButton("BFS");
    JButton buttonSearch = new JButton("Search");
    JLabel textPanel = new JLabel("Waiting to do smth (*^▽^*)");
    File currentFile;
    CanvasPanel canvasPanel;
    public DialogMenuButtonsPanel(CanvasPanel canvasPanel) {
        setSize(200, 100);
        setVisible(true);
        setLayout(null);
        this.canvasPanel=canvasPanel;
        setupButtons();
        enableMenu(false);
    }
    private void setupButtons(){
        add(buttonOpen);
        buttonOpen.setBounds(0,0, 100,25);
        add(buttonSave);
        buttonSave.setBounds(0,25, 100,25);
        add(buttonClose);
        buttonClose.setBounds(0,50, 100,25);
        add(buttonDFS);
        buttonDFS.setBounds(100,0,100,25);
        add(buttonBFS);
        buttonBFS.setBounds(100,25, 100,25);
        add(buttonSearch);
        buttonSearch.setBounds(100,50, 100,25);
        add(textPanel);
        textPanel.setBounds(0,75,200,25);
        canvasPanel.textPanel =textPanel;
        textPanel.setBackground(Color.LIGHT_GRAY);
        buttonSearch.setBackground(Color.LIGHT_GRAY);
        buttonBFS.setBackground(Color.LIGHT_GRAY);
        buttonDFS.setBackground(Color.LIGHT_GRAY);
        buttonOpen.setBackground(Color.LIGHT_GRAY);
        buttonSave.setBackground(Color.LIGHT_GRAY);
        buttonClose.setBackground(Color.LIGHT_GRAY);

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!canvasPanel.animationStarted){
                    textPanel.setText("Select vertex 1 ╰(ಠ_ಠ )¬");
                    canvasPanel.animationStarted = true;
                    canvasPanel.searchSelectMode = true;
                    canvasPanel.curSearchPeak = 1;
                }
            }
        });
        buttonBFS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!canvasPanel.animationStarted) {
                    Thread animationBFS = new Thread(() -> {
                        canvasPanel.animationStarted=true;
                        HashSet<Peak> firstPeak= new HashSet<>();
                        if (canvasPanel.isThereSelectedPeak) {
                            firstPeak.add(canvasPanel.selectedPeak);
                        }
                        else{
                            firstPeak.add((Peak) canvasPanel.edgesHashMap.keySet().toArray()[0]);
                        }
                        try {
                            canvasPanel.buttonSelected = buttonBFS;
                            canvasPanel.currentNumberOfAnimationPeaks = 1;
                            canvasPanel.bfsAnimation(firstPeak);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    animationBFS.start();
                }
            }
        });
        buttonDFS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!canvasPanel.animationStarted) {
                    canvasPanel.buttonSelected=buttonDFS;
                    canvasPanel.currentNumberOfAnimationPeaks=0;
                    if (canvasPanel.isThereSelectedPeak) {
                        canvasPanel.animationStarted = true;
                        Thread animationDFS = new Thread(() -> {
                            try {
                                canvasPanel.startingDfsPeak = canvasPanel.selectedPeak;
                                canvasPanel.dfsAnimation(canvasPanel.startingDfsPeak);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        animationDFS.start();
                    } else if (canvasPanel.currentNumberOfPeaks != 0) {
                        canvasPanel.animationStarted = true;
                        Thread animationDFS = new Thread(() -> {
                            try {
                                canvasPanel.startingDfsPeak = (Peak) canvasPanel.edgesHashMap.keySet().toArray()[0];
                                canvasPanel.dfsAnimation(canvasPanel.startingDfsPeak);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        animationDFS.start();
                    }
                }
            }
        });
        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvasPanel.isThereSelectedPeak = false;
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "TXT files", "txt");
                fileChooser.setFileFilter(filter);
                int option = fileChooser.showOpenDialog(new DialogMenu());
                if(option == JFileChooser.APPROVE_OPTION){
                    currentFile = fileChooser.getSelectedFile();
                    FileReader fileReader = null;
                    try {
                        fileReader = new FileReader(fileChooser.getSelectedFile());
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    canvasPanel.edgesHashMap.clear();
                    canvasPanel.currentNumberOfPeaks=0;
                    try {
                        char[] c = new char[1];
                        int pointer = 1;
                        int x=0;
                        int y=0;
                        int x1=0;
                        int y1=0;
                        StringBuilder line = new StringBuilder();
                        bufferedReader.read(c);
                        while (c[0]!='}'){
                            if (c[0]!=' '){
                                line.append(c[0]);
                            }
                            else{
                                switch (pointer){
                                    case 1:{
                                        x = Integer.parseInt(line.toString());
                                        pointer = 2;
                                        break;
                                    }
                                    case 2:{
                                        canvasPanel.currentNumberOfPeaks++;
                                        canvasPanel.edgesHashMap.put(new Peak(new Point(x, Integer.parseInt(line.toString())),
                                                canvasPanel.currentNumberOfPeaks), new Edge());
                                        pointer = 3;
                                        break;
                                    }
                                    case 3:{
                                        pointer = 1;
                                        break;
                                    }
                                }
                                line.delete(0,line.length());
                            }
                            bufferedReader.read(c);
                        }
                        bufferedReader.read(c);
                        while (c[0]!='}'){
                            if (c[0]!=' '){
                                line.append(c[0]);
                            }
                            else{
                                switch (pointer){
                                    case 1:{
                                        x = Integer.parseInt(line.toString());
                                        pointer = 2;
                                        break;
                                    }
                                    case 2:{
                                        y = Integer.parseInt(line.toString());
                                        pointer = 3;
                                        break;
                                    }
                                    case 3:{
                                        x1 = Integer.parseInt(line.toString());
                                        pointer = 4;
                                        break;
                                    }
                                    case 4:{
                                        y1 = Integer.parseInt(line.toString());
                                        Edge edge = new Edge();
                                        int finalX = x;
                                        int finalY = y;
                                        Peak tFirstPeak = null;
                                        Peak tSecondPeak = null;
                                        int finalX1 = x1;
                                        int finalY1 = y1;
                                        for (Map.Entry<Peak, Edge> entry : canvasPanel.edgesHashMap.entrySet()) {
                                            Peak key = entry.getKey();
                                            Edge value = entry.getValue();
                                            if (key.getCenter().x == finalX && key.getCenter().y == finalY) {
                                                tFirstPeak = key;
                                            }
                                            if (key.getCenter().x == finalX1 && key.getCenter().y == finalY1) {
                                                tSecondPeak = key;
                                            }
                                        }
                                        canvasPanel.edgesHashMap.get(tFirstPeak).addPeak(tSecondPeak);
                                        canvasPanel.edgesHashMap.get(tSecondPeak).addPeak(tSecondPeak);
                                        pointer = 1;
                                        break;
                                    }
                                }
                                line.delete(0,line.length());
                            }
                            bufferedReader.read(c);
                        }

                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    canvasPanel.repaint();
                }
            }
        });
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "TXT files", "txt");
                fileChooser.setFileFilter(filter);
                int option = fileChooser.showSaveDialog(new DialogMenu());
                if(option == JFileChooser.APPROVE_OPTION){
                    currentFile = fileChooser.getSelectedFile();
                    PrintWriter writer;
                    try {
                        writer = new PrintWriter(fileChooser.getSelectedFile());
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                      canvasPanel.edgesHashMap.forEach((key,value)->{
                          writer.print(key.getCenter().x + " "
                                  +key.getCenter().y + " "
                                          + key.getRadius() + " ");
                      });
                      writer.print("}");
                      canvasPanel.edgesHashMap.forEach((key, value)->{
                          value.getConnectedPeaks().forEach((valuePrint)->{
                              writer.print(key.getCenter().x + " " + key.getCenter().y + " "
                                      + valuePrint.getCenter().x + " " + valuePrint.getCenter().y + " ");
                          });
                      });
                      writer.print("}");
//                    for (Peak peak: canvasPanel.peaks){
//                        writer.print(peak.getCenter().x + " "
//                                + peak.getCenter().y + " "
//                                        + peak.getRadius() + " ");
//                    }
//
//                    for (Edge edge: canvasPanel.edges){
//                        writer.print(edge.getPeakOne().getCenter().x + " "
//                                + edge.getPeakOne().getCenter().y + " "
//                                + edge.getPeakTwo().getCenter().x + " "
//                                +edge.getPeakTwo().getCenter().y + " ");
//                    }
                    writer.close();
                }
            }
        });
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void enableMenu(boolean flag){
        if (flag){
            buttonClose.setVisible(true);
            buttonClose.setEnabled(true);
            buttonOpen.setVisible(true);
            buttonOpen.setEnabled(true);
            buttonSave.setVisible(true);
            buttonSave.setEnabled(true);
        }
        else {
            buttonClose.setVisible(false);
            buttonClose.setEnabled(false);
            buttonOpen.setVisible(false);
            buttonOpen.setEnabled(false);
            buttonSave.setVisible(false);
            buttonSave.setEnabled(false);
        }
    }

}
