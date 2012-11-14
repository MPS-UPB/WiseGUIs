package mps.GUI.window.implementation;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CompareImagesWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	Image firstImage;
	Image secondImage;
	JPanel imagePanel;
	JPanel buttonsPanel;
	
	public CompareImagesWindow(String imageFile1, String imageFile2){
       super("Compare images");
		
      
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setSize(400, 400);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		 
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/2;
		 
		setLocation(x, y);
		setResizable(false);
		
		

		 
		ImageIcon sizedIcon1 = new ImageIcon(new ImageIcon(imageFile1)
				.getImage().getScaledInstance(250, 250,
				Image.SCALE_SMOOTH));
		
		ImageIcon sizedIcon2 = new ImageIcon(new ImageIcon(imageFile2)
		.getImage().getScaledInstance(250, 250,
		 Image.SCALE_SMOOTH));
		
		ImageIcon smallIcon1 = new ImageIcon(new ImageIcon(imageFile1)
		.getImage().getScaledInstance(70, 70,
		Image.SCALE_SMOOTH));

		ImageIcon smallIcon2 = new ImageIcon(new ImageIcon(imageFile2)
		.getImage().getScaledInstance(70, 70,
		 Image.SCALE_SMOOTH));
		
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
		
		
		imagesPanel.add(Box.createVerticalStrut(30));
		JLabel l2 = new JLabel(smallIcon2);
		l2.setBorder(BorderFactory.createTitledBorder("Image 2"));
		
		
		imagesPanel.add(l2);
		
		imagesPanel.add(Box.createVerticalStrut(30));
		
		
		imagePanel.add(imagesPanel,BorderLayout.WEST);
		imagePanel.add(new JLabel(sizedIcon1));
		imagePanel.add(Box.createVerticalGlue());
		
		
		imagePanel.add(new JLabel(sizedIcon2));
		myPanel.add(imagePanel,BorderLayout.NORTH);

		
		
	
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.GRAY));
		JButton slideButton = new JButton("Slide");
		JButton differenceButton = new JButton("Diff");
		JButton intersectionButton = new JButton("Common");
		
		buttonsPanel.add(slideButton);
		buttonsPanel.add(differenceButton);
		buttonsPanel.add(intersectionButton);
		myPanel.add(buttonsPanel,BorderLayout.SOUTH);
		
		setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new CompareImagesWindow("image.jpg", "image.jpg");
	}

}
