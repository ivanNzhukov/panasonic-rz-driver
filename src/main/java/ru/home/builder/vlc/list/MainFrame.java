package ru.home.builder.vlc.list;

import javax.swing.*;
import java.awt.*;

class MainFrame extends JFrame {


    public MainFrame(Canvas canvas) {
        setTitle("MediaListPlayer Test");
        setBounds(100, 100, 800, 600);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        canvas.setBackground(Color.black);
        contentPane.add(canvas, BorderLayout.CENTER);
        setContentPane(contentPane);
    }

}