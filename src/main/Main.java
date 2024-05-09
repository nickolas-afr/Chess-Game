package main;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo((null));

        Board board = new Board();
        frame.add(board);

        frame.setVisible(true);



    }
}