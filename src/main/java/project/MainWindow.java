package project;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    JFrame window = new JFrame();
    CanvasPanel graphsArea = new CanvasPanel();
    public MainWindow(){
        window.setTitle("Graphs");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 500);
        window.setLocationRelativeTo(null);
        window.setLayout(null);

        window.add(graphsArea);
        graphsArea.setBackground(Color.darkGray);
        graphsArea.setSize(500, 400);
        graphsArea.setVisible(true);
        graphsArea.setBounds(200,50, 500, 400);
    }
    public void show() {
        window.setVisible(true);
    }
}
