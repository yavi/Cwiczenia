/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lifegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author yavi
 */
public class DrawingPanel extends JPanel{

    BufferedImage cs;
    Timer timer;



    public DrawingPanel() {
        setBorder(BorderFactory.createLineBorder(Color.gray));
    }

    /*@Override
    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }*/

    class TimeRefresh implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                     paintTick();
            }
    }

    public void initSequence()
    {
        ActionListener listener = new TimeRefresh();
        timer = new Timer(35, listener);
        timer.setInitialDelay(300);
        timer.start();

        cs = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_ARGB);
        for ( int y = 0; y< this.getHeight(); y++)
            {
                for(int x = 0; x < this.getWidth(); x++)
                {
                    if(Math.random() < 0.1f)
                    {
                        cs.setRGB(x, y, Color.green.getRGB());
                    }
                }
            }
        repaint();
    }

    public void paintTick()
    {
        BufferedImage cs2 = new BufferedImage(cs.getWidth(),cs.getHeight(),BufferedImage.TYPE_INT_ARGB);
        int neigh=0;
        for(int y = 1; y < cs.getHeight()-1;y++)
        {
            for(int x = 1; x < cs.getWidth()-1;x++)
            {
                neigh = 0;
                if(cs.getRGB(x, y-1) == Color.green.getRGB()){neigh++;}
                if(cs.getRGB(x, y+1) == Color.green.getRGB()){neigh++;}
                if(cs.getRGB(x-1, y) == Color.green.getRGB()){neigh++;}
                if(cs.getRGB(x+1, y) == Color.green.getRGB()){neigh++;}
                if(cs.getRGB(x+1, y+1) == Color.green.getRGB()){neigh++;}
                if(cs.getRGB(x+1, y-1) == Color.green.getRGB()){neigh++;}
                if(cs.getRGB(x-1, y+1) == Color.green.getRGB()){neigh++;}
                if(cs.getRGB(x-1, y-1) == Color.green.getRGB()){neigh++;}

                if(cs.getRGB(x, y) == Color.green.getRGB())
                {
                    if(neigh != 3 && neigh !=4)
                        cs2.setRGB(x, y, Color.black.getRGB());
                    else
                        cs2.setRGB(x, y, Color.green.getRGB());
                } else {
                    if(neigh == 3)
                        cs2.setRGB(x,y,Color.green.getRGB());
                    else
                        cs2.setRGB(x, y, Color.black.getRGB());
                }
            }
        }
        cs.getGraphics().drawImage(cs2, 0, 0, this);
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(cs, 0, 0, this);
        // Draw Text
    }
}
