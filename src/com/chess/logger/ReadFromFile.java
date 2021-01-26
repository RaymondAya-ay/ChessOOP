package com.chess.logger;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ReadFromFile extends JFrame {

    public ReadFromFile() throws IOException {
        FileReader fr = new FileReader("./src/com/chess/logger/data/logs.txt");

        Container contents = getContentPane();
        contents.setLayout(new GridLayout(1,1));

        int i;
        String log = " ";
        while ((i = fr.read()) != -1) {
            log = log + (char) i;
        }
        JTextArea text = new JTextArea(log);
        JScrollPane sp = new JScrollPane(text);

        contents.add(sp);
        System.out.print(log);

        setSize(450, 600);
        setResizable(false);
        setLocationRelativeTo(null); //centers window
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

}
