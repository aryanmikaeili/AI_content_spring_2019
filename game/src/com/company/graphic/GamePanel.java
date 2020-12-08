package com.company.graphic;

import com.company.Board;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public final static int size = 120;
    private final static int dia = 118;
    private Board board;
    private Color[] colors = {Color.GRAY, new Color(0, 100, 238), new Color(238, 0, 31),
            new Color(195, 41, 234)};

    public GamePanel(Board board) {
        this.board = board;
        setLayout(null);
        setOpaque(true);
        setBackground(Color.black);
        setPreferredSize(new Dimension(size * (Board.size + 1), size * (Board.size + 1)));
        setSize(size * (Board.size + 1), size * (Board.size + 1));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < Board.size; i++)
            for (int j = 0; j < Board.size; j++) {

                if (board.getCell(Board.size - 1 - j, i).getColor() == 0) {
                    drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                            colors[board.getCell(Board.size - 1 - j, i).getColor()], "");

                  } else
                    drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                            colors[board.getCell(Board.size - 1 - j, i).getColor()], "fill");
            }
    }


    private void drawCenteredRectangle(Graphics2D g, int x, int y, Color col, String str) {
        g.setColor(col);
        g.drawString(str, x, y);
        x = x - (GamePanel.dia / 2);
        y = y - (GamePanel.dia / 2);
        if (str.equals("fill")) {
            g.fillRect(x, y, GamePanel.dia, GamePanel.dia);
        } else
            g.drawRect(x, y, GamePanel.dia, GamePanel.dia);
    }
}