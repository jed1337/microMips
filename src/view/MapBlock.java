/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Nonbr
 */
public class MapBlock extends Rectangle {

    private static Font sanSerifFont = new Font("SanSerif", Font.PLAIN, 12);
    private int x, y;
    private final String color;
    private String text;

    public MapBlock(int x, int y, String color, String text) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.text = text;
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.decode(this.color));
        g.drawRect(x, y, 30, 30);
        g.fillRect(x, y, 30, 30);

        g.setColor(Color.black);
        //g.setFont(sanSerifFont);
        int newX = x + 10;
        if (text == "MEM") {
            newX = x + 5;
        }
        g.drawString(text, newX, y + 20);
    }
}
