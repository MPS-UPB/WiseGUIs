package mps.GUI;

/**
 * @author Roxana
 */
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.*;

public final class CompareImagesWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    Image firstImage;
    Image secondImage;
    
    JPanel imagePanel;
    JPanel buttonsPanel;
    MainWindow mainWindow;
    
    boolean img2Selected = false;
    boolean img1Selected = false;

    String imageFile1;
    String imageFile2;
    
    public CompareImagesWindow(String imageFile1, String imageFile2) throws IOException {

        super("Compare images");

        this.imageFile1 = imageFile1;
        this.imageFile2 = imageFile2;
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                //        mainWindow.setEnabled(true);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowActivated(WindowEvent e) {
                //       mainWindow.setEnabled(false);
            }
        });

        setSize(400, 400);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        setLocation(x, y);
        setResizable(false);

        // Prima imagine
        int width = 250;
        int height = 250;
        Image image = ImageViewer.buildImage(imageFile1);
        image = ImageViewer.resize(image, width, height);
        
        ImageIcon sizedIcon1 = new ImageIcon(image);
        
        
        // A doua imagine
        width = 250;
        height = 250;
        image = ImageViewer.buildImage(imageFile2);
        image = ImageViewer.resize(image, width, height);
        
        ImageIcon sizedIcon2 = new ImageIcon(image);


        final JLabel labelImg1 = new JLabel(sizedIcon1);
        final JLabel labelImg2 = new JLabel(sizedIcon2);

        // Prima imagine mica din stanga
        width = 70;
        height = 70;
        image = ImageViewer.buildImage(imageFile1);
        image = ImageViewer.resize(image, width, height);
        
        ImageIcon smallIcon1 = new ImageIcon(image);
        
        // A doua imagine mica din stanga
        width = 70;
        height = 70;
        image = ImageViewer.buildImage(imageFile2);
        image = ImageViewer.resize(image, width, height);
        
        ImageIcon smallIcon2 = new ImageIcon(image);
        
        // Bucata de cod pentru constructia de imagini tiff pentru overlayImage
        width = 250;
        height = 250;
        image = ImageViewer.buildImage(imageFile1);
        final Image overlayImage = ImageViewer.resize(image, width, height);
        
        // Bucata de cod pentru constructia de imagini tiff pentru backgroundImage
        width = 250;
        height = 250;
        image = ImageViewer.buildImage(imageFile2);
        final Image backgroundImage = ImageViewer.resize(image, width, height);
        
        buttonsPanel = new JPanel();
        imagePanel = new JPanel();

        JPanel imagesPanel = new JPanel();
        Container myPanel = getContentPane();

        imagesPanel.setBorder(BorderFactory.createEtchedBorder());
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));

        imagesPanel.add(Box.createVerticalStrut(30));

        JLabel l1 = new JLabel(smallIcon1);
        l1.setBorder(BorderFactory.createTitledBorder("Image 1"));
        imagesPanel.add(l1);
        l1.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                //if compare button was pressed, none of the images are selected
                // same for the other icon
                if (!img1Selected && !img2Selected)
                {
                    img2Selected = true;
                }
                
                if (img2Selected && !img1Selected) {
                    
                    imagePanel.remove(imagePanel.getComponent(1));
                    
                    FadeOut fo = new FadeOut(backgroundImage, overlayImage);
                    fo.setSize(250, 250);
                    
                    labelImg1.add(fo);
                    fo.start();
                    
                    imagePanel.add(labelImg1, 1);
                    
                    revalidate();
                    repaint();
                    
                    img2Selected = false;
                    img1Selected = true;
                }
            }
        });

        imagesPanel.add(Box.createVerticalStrut(30));
        JLabel l2 = new JLabel(smallIcon2);
        l2.setBorder(BorderFactory.createTitledBorder("Image 2"));
        imagesPanel.add(l2);
        l2.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (!img1Selected && !img2Selected)
                {
                    img1Selected = true;
                }
                
                if (img1Selected && !img2Selected) {
                    
                    imagePanel.remove(imagePanel.getComponent(1));
                    
                    FadeOut fo = new FadeOut(overlayImage, backgroundImage);
                    fo.setSize(250, 250);
                    fo.start();
                    
                    labelImg2.add(fo);
                    
                    imagePanel.add(labelImg2, 1);
                    
                    revalidate();
                    repaint();
                    
                    img1Selected = false;
                    img2Selected = true;
                }

            }
        });

        imagesPanel.add(Box.createVerticalStrut(30));

        imagePanel.add(imagesPanel, BorderLayout.WEST);
        imagePanel.add(labelImg2);
        imagePanel.add(Box.createVerticalGlue());

        img2Selected = true;
        
        myPanel.add(imagePanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.GRAY));
        
        JButton compareButton = new JButton("Compare");
        
        // to do : add functionalities to bottons
        
        compareButton.addMouseListener(diffListener);
        
        buttonsPanel.add(compareButton);
        
        myPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private MouseListener diffListener = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) { 

                
                // Pentru img1
                int width = 250;
                int height = 250;
                Image image = ImageViewer.buildImage(imageFile1);
                Image img1 = ImageViewer.resize(image, width, height);

                // Pentru img2
                width = 250;
                height = 250;
                image = ImageViewer.buildImage(imageFile2);
                Image img2 = ImageViewer.resize(image, width, height);
                
                imagePanel.remove(imagePanel.getComponent(1));

               FadeOut fo = null;

                if (img1Selected)
                {
                    fo = new FadeOut(img1, img2);
                } else
                {
                    fo = new FadeOut(img2, img1);
                }

                img1Selected = false;
                img2Selected = false;

                fo.setSize(250, 250);
                fo.setAlphaLimit(0.5f);
                fo.start();
                
                // Pentru sizedIcon
                width = 250;
                height = 250;
                image = ImageViewer.buildImage(imageFile2);
                image = ImageViewer.resize(image, width, height);
                ImageIcon sizedIcon = new ImageIcon(image);
               
                final JLabel labelImg = new JLabel(sizedIcon);

                labelImg.add(fo);

                imagePanel.add(labelImg, 1);

                revalidate();
                repaint();
                                   
            
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            
            }

            @Override
            public void mouseEntered(MouseEvent me) {
               
            }

            @Override
            public void mouseExited(MouseEvent me) {
                
            }
        };
}
