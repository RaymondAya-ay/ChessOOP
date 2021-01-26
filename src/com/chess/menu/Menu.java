package com.chess.menu;
import com.chess.graphics.ChessboardGrid;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JFrame {

    public Menu(){
        Container contents = getContentPane();
        contents.setLayout(new GridLayout(2,1));

        JButton startGame = new JButton();
        JButton history = new JButton();
        JLabel startGameLabel = new JLabel("Start Game", SwingConstants.CENTER);
        JLabel historyLabel = new JLabel("View Game History", SwingConstants.CENTER);
        startGame.add(startGameLabel, BorderLayout.CENTER);
        history.add(historyLabel, BorderLayout.CENTER);

        contents.add(startGame);
        contents.add(history);

        setSize(300, 225);
        setResizable(false);
        setLocationRelativeTo(null); //centers window
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChessboardGrid grid = new ChessboardGrid();
            }
        });
    }

}
