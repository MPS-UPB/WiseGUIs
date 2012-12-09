/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI.window.implementation;

import java.awt.AlphaComposite;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FadeOut extends JPanel implements ActionListener {

    Image overlayImage;
    Image backgroundImage;
    
    Timer timer;
    private float alpha = 1f;
    private float alphaLimit = 0f;
    
    private boolean transparentBackground = false;
    
    public FadeOut(Image overlayImage, Image backgroundImage) {
        
        this.overlayImage = overlayImage;
        this.backgroundImage = backgroundImage;
    }
    
    public FadeOut(Image overlayImage, Image backgroundImage, boolean transparentBackground)
    {
        this(overlayImage, backgroundImage);
        
        this.transparentBackground = transparentBackground;
    }
    
    public void start()
    {
        timer = new Timer(20, this);
        timer.start();
    }
    
    public void setAlphaLimit(float al)
    {
        alphaLimit = al;
    }
    
    public float getAlphaLimit()
    {
        return alphaLimit;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        g.drawImage(backgroundImage, 0, 0, null);
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                    alpha));
        g2d.drawImage(overlayImage, 0, 0, null);        
    }

    public void actionPerformed(ActionEvent e) {
        alpha += -0.03f;
        
        if (alpha <= alphaLimit) {
            alpha = alphaLimit;
            timer.stop();
        }
        
        repaint();
    }
}
