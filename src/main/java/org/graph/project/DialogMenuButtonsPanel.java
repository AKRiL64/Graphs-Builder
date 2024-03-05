package org.graph.project;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class DialogMenuButtonsPanel extends JPanel {
    JButton buttonOpen = new JButton("Open");
    JButton buttonSave = new JButton("Save");
    JButton buttonClose = new JButton("Exit");
    File currentFile;
    CanvasPanel canvasPanel;
    public DialogMenuButtonsPanel(CanvasPanel canvasPanel) {
        setSize(150, 75);
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
        buttonOpen.setBackground(Color.LIGHT_GRAY);
        buttonSave.setBackground(Color.LIGHT_GRAY);
        buttonClose.setBackground(Color.LIGHT_GRAY);

        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    if (!canvasPanel.peaks.isEmpty()) {
                        canvasPanel.peaks.subList(0, canvasPanel.peaks.size()).clear();
                    }
                    if (!canvasPanel.edges.isEmpty()) {
                        canvasPanel.edges.subList(0, canvasPanel.edges.size()).clear();
                    }
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
                                        canvasPanel.peaks.add(new Peak(new Point(x, Integer.parseInt(line.toString()))));
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
                                        for (Peak peak: canvasPanel.peaks){
                                            if (peak.getCenter().x == x && peak.getCenter().y == y){
                                                edge.setPeakOne(peak);
                                            }
                                            if (peak.getCenter().x == x1 && peak.getCenter().y ==y1){
                                                edge.setPeakTwo(peak);
                                            }
                                        }
                                        canvasPanel.edges.add(edge);
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
                    for (Peak peak: canvasPanel.peaks){
                        writer.print(peak.getCenter().x + " "
                                + peak.getCenter().y + " "
                                        + peak.getRadius() + " ");
                    }
                    writer.print("}");
                    for (Edge edge: canvasPanel.edges){
                        writer.print(edge.getPeakOne().getCenter().x + " "
                                + edge.getPeakOne().getCenter().y + " "
                                + edge.getPeakTwo().getCenter().x + " "
                                +edge.getPeakTwo().getCenter().y + " ");
                    }
                    writer.print("}");
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
