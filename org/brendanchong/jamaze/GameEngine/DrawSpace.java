package org.brendanchong.jamaze.GameEngine;

import javax.swing.*;
import java.awt.*;

/**
 * This is the class that draws your location and the exits in a graphical representation.
 * The rooms are represented as squares.
 * 
 * Brendan Chong, 2301426
 * 14/11/05
 */

class DrawSpace extends JPanel
{
    public static boolean hasNorth;
    public static boolean hasSouth;
    public static boolean hasWest;
    public static boolean hasEast;
    int coord;
    int sqSize;
    
    public DrawSpace(int c, int size)
    {
        coord = c;
        sqSize = size;
    }
    
    public void paintComponent(Graphics g)
    {
    g.drawRect(coord, coord, sqSize, sqSize);
    
    if(hasNorth)
        drawNorth(g);
    if(hasSouth)
        drawSouth(g);
    if(hasWest)
        drawWest(g);
    if(hasEast)
        drawEast(g);
            
    }
    
    
    public void drawNorth(Graphics g)
    {
        g.drawRect(coord, 0, sqSize, sqSize);
    }
    
    public void drawSouth(Graphics g)
    {
        g.drawRect(coord, coord * 2 + 1, sqSize, sqSize);
    }
    
    public void drawWest(Graphics g)
    {
        g.drawRect(0, coord, sqSize, sqSize);
    }
    
    public void drawEast(Graphics g)
    {
        g.drawRect(coord * 2 + 1, coord, sqSize, sqSize);
    }
       
}