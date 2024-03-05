package org.graph.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
    JFrame window = new JFrame();
    CanvasPanel graphsArea = new CanvasPanel();
    DialogMenu dialogMenu = new DialogMenu();
    JButton buttonFile = new JButton("File");

    boolean isOpened = false;
    DialogMenuButtonsPanel dialogMenuButtonsPanel = new DialogMenuButtonsPanel(graphsArea);
    public MainWindow(){
        System.out.println(System.lineSeparator());
        window.setTitle("Graphs");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 500);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setResizable(false);

        window.add(graphsArea);
        graphsArea.setBackground(Color.darkGray);
        graphsArea.setSize(500, 400);
        graphsArea.setVisible(true);
        graphsArea.setBounds(200,50, 500, 400);

        window.add(dialogMenu);
        window.add(dialogMenuButtonsPanel);
        window.add(buttonFile);
        dialogMenuButtonsPanel.setLocation(0,25);

        buttonFile.setBounds(0,0,100, 25);
        buttonFile.setBackground(Color.lightGray);
        buttonFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (isOpened){
                    dialogMenuButtonsPanel.enableMenu(false);
                    isOpened = false;
                    System.out.println("Close");
                }
                else{
                    dialogMenuButtonsPanel.enableMenu(true);
                    isOpened = true;
                    System.out.println("Open");
                }
            }
        });
    }
    public void show() {
        window.setVisible(true);
    }
}
