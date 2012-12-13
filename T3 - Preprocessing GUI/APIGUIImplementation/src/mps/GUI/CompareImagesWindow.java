package mps.GUI;

/**
 * @author Roxana
 */
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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
        FileInputStream in = null;
        ImageIcon sizedIcon1 = null;
        try {
            in = new FileInputStream(imageFile1);
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
            channel.read(buffer);
            Image image = ImageViewer.load(buffer.array());
            // make sure that the image is not too big
            //  scale with a width of 250
            Image imageScaled = 
              image.getScaledInstance(250,250,  Image.SCALE_SMOOTH);
            sizedIcon1 = new ImageIcon(imageScaled);
        }
        catch (Exception ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // A doua imagine
        in = null;
        ImageIcon sizedIcon2 = null;
        try {
            in = new FileInputStream(imageFile2);
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
            channel.read(buffer);
            Image image = ImageViewer.load(buffer.array());
            // make sure that the image is not too big
            //  scale with a width of 250
            Image imageScaled = 
              image.getScaledInstance(250,250,  Image.SCALE_SMOOTH);
            sizedIcon2 = new ImageIcon(imageScaled);
        }
        catch (Exception ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        final JLabel labelImg1 = new JLabel(sizedIcon1);
        final JLabel labelImg2 = new JLabel(sizedIcon2);

        // Prima imagine mica din stanga
        in = null;
        ImageIcon smallIcon1 = null;
        try {
            in = new FileInputStream(imageFile1);
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
            channel.read(buffer);
            Image image = ImageViewer.load(buffer.array());
            // make sure that the image is not too big
            //  scale with a width of 70
            Image imageScaled = 
              image.getScaledInstance(70,70,  Image.SCALE_SMOOTH);
            smallIcon1 = new ImageIcon(imageScaled);
        }
        catch (Exception ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // A doua imagine mica din stanga
        in = null;
        ImageIcon smallIcon2 = null;
        try {
            in = new FileInputStream(imageFile2);
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
            channel.read(buffer);
            Image image = ImageViewer.load(buffer.array());
            // make sure that the image is not too big
            //  scale with a width of 70
            Image imageScaled = 
              image.getScaledInstance(70,70,  Image.SCALE_SMOOTH);
            smallIcon2 = new ImageIcon(imageScaled);
        }
        catch (Exception ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
     //   final Image overlayImage = new ImageIcon(imageFile1).getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
     //   final Image backgroundImage = new ImageIcon(imageFile2).getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        
        // Bucata de cod pentru constructia de imagini tiff pentru overlayImage
        in = new FileInputStream(imageFile1);
        FileChannel channel = in.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
        channel.read(buffer);
        Image overlayImage_tmp = null;
        try {
            overlayImage_tmp = ImageViewer.load(buffer.array());
            overlayImage_tmp.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            Logger.getLogger(CompareImagesWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        final Image overlayImage = overlayImage_tmp;
        
        // Bucata de cod pentru constructia de imagini tiff pentru backgroundImage
        in = new FileInputStream(imageFile2);
        FileChannel channel2 = in.getChannel();
        ByteBuffer buffer2 = ByteBuffer.allocate((int)channel2.size());
        channel2.read(buffer2);
        Image backgroundImage_tmp = null;
        try {
            backgroundImage_tmp = ImageViewer.load(buffer2.array());
            backgroundImage_tmp.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            Logger.getLogger(CompareImagesWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        final Image backgroundImage = backgroundImage_tmp;
        
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
            //    Image img1 = ImageIO.read(new File(imageFile1)).getScaledInstance(250, 250, Image.SCALE_SMOOTH);
             //   Image img2 = ImageIO.read(new File(imageFile2)).getScaledInstance(250, 250, Image.SCALE_SMOOTH);

                // Pentru img1
                FileInputStream in = null;
                Image img1 = null;
                try {
                    in = new FileInputStream(imageFile1);
                    FileChannel channel = in.getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
                    channel.read(buffer);
                    Image image = ImageViewer.load(buffer.array());
                    // make sure that the image is not too big
                    //  scale with a width of 250
                    img1 = 
                      image.getScaledInstance(250,250,  Image.SCALE_SMOOTH);
                }
                catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // Pentru img2
                in = null;
                Image img2 = null;
                try {
                    in = new FileInputStream(imageFile2);
                    FileChannel channel = in.getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
                    channel.read(buffer);
                    Image image = ImageViewer.load(buffer.array());
                    // make sure that the image is not too big
                    //  scale with a width of 250
                    img2 = 
                      image.getScaledInstance(250,250,  Image.SCALE_SMOOTH);
                }
                catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

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
              /*  
                ImageIcon sizedIcon = new ImageIcon(new ImageIcon(imageFile2)
                    .getImage().getScaledInstance(250, 250,
                    Image.SCALE_SMOOTH));
               */
                
                // Pentru sizedIcon
                in = null;
                ImageIcon sizedIcon = null;
                try {
                    in = new FileInputStream(imageFile2);
                    FileChannel channel = in.getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
                    channel.read(buffer);
                    Image image = ImageViewer.load(buffer.array());
                    // make sure that the image is not too big
                    //  scale with a width of 250
                    Image imageScaled = 
                      image.getScaledInstance(250,250,  Image.SCALE_SMOOTH);
                    sizedIcon = new ImageIcon(imageScaled);
                }
                catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
    
    /*
     public static void main(String... args) {
     CompareImagesWindow c = new CompareImagesWindow("image1.jpg", "image2.jpg");
     c.setVisible(true);
     }
     */
}
