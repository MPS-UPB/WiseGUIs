package mps.GUI;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class Experiment extends JFrame {
	protected JSplitPane splitPane;
	protected JPanel rightPanel;
	protected JPanel leftPanel;
	protected JPanel filePanel;
	protected JPanel panel_2;
	protected JScrollPane imagePanel;
	protected JButton browseButton;
	protected JTextField pathTextField;
	protected Component verticalStrut;
	protected Component horizontalStrut;
	protected Component verticalStrut_1;
	protected Component verticalStrut_2;
	protected Component horizontalStrut_2;
	protected Component verticalStrut_3;
	protected JButton binarizationButton;
	protected JButton preprocessingButton;
	protected Component horizontalStrut_1;
	protected Component horizontalGlue;
	protected Component horizontalStrut_3;
	protected Component horizontalStrut_4;
	protected JCheckBox updateCheckBox;
	protected Component horizontalGlue_1;
	protected JButton updateButton;
	protected Component horizontalStrut_5;
	protected Component verticalStrut_4;
	protected JButton compareButton;
	protected JScrollPane imageScrollPane;
	protected Component verticalStrut_5;
	protected Component verticalStrut_6;

	public Experiment() throws HeadlessException {
		// TODO Auto-generated constructor stub
		initComponents();
	}
	private void initComponents() {
		
		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		rightPanel = new JPanel();
	//	rightPanel.setMinimumSize(new Dimension(250, 30));
	//	rightPanel.setPreferredSize(new Dimension(250, 30));
		splitPane.setLeftComponent(rightPanel);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		verticalStrut = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut);
		
		filePanel = new JPanel();
	//	filePanel.setPreferredSize(new Dimension(250, 30));
	//	filePanel.setMinimumSize(new Dimension(250, 30));
		filePanel.setMaximumSize(new Dimension(32767, 30));
		rightPanel.add(filePanel);
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
		
		horizontalStrut = Box.createHorizontalStrut(10);
		filePanel.add(horizontalStrut);
		
		browseButton = new JButton("Browse");
		browseButton.setPreferredSize(new Dimension(70, 25));
		browseButton.setMinimumSize(new Dimension(70, 25));
		browseButton.setMaximumSize(new Dimension(70, 25));
		filePanel.add(browseButton);
		
		horizontalStrut_2 = Box.createHorizontalStrut(10);
		filePanel.add(horizontalStrut_2);
		
		pathTextField = new JTextField();
		pathTextField.setPreferredSize(new Dimension(200, 25));
		pathTextField.setMinimumSize(new Dimension(150, 25));
		pathTextField.setMaximumSize(new Dimension(250, 25));
		pathTextField.setEditable(false);
		filePanel.add(pathTextField);
		
		verticalStrut_1 = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut_1);
		
		imagePanel = new JScrollPane();
		rightPanel.add(imagePanel);
		
		verticalStrut_2 = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut_2);
		
		JPanel buttonsPanel = new JPanel();
	//	buttonsPanel.setPreferredSize(new Dimension(250, 30));
	//	buttonsPanel.setMinimumSize(new Dimension(250, 30));
		buttonsPanel.setMaximumSize(new Dimension(32767, 30));
		rightPanel.add(buttonsPanel);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		buttonsPanel.add(horizontalStrut_1);
		
		preprocessingButton = new JButton("Preprocessing");
		preprocessingButton.setPreferredSize(new Dimension(100, 25));
		preprocessingButton.setMinimumSize(new Dimension(100, 25));
		preprocessingButton.setMaximumSize(new Dimension(100, 25));
		preprocessingButton.setEnabled(false);
		buttonsPanel.add(preprocessingButton);
		
		horizontalGlue = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue);
		
		binarizationButton = new JButton("Binarization");
		binarizationButton.setMinimumSize(new Dimension(100, 25));
		binarizationButton.setMaximumSize(new Dimension(100, 25));
		binarizationButton.setPreferredSize(new Dimension(100, 25));
		binarizationButton.setEnabled(false);
		buttonsPanel.add(binarizationButton);
		
		horizontalStrut_3 = Box.createHorizontalStrut(20);
		buttonsPanel.add(horizontalStrut_3);
		
		verticalStrut_3 = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut_3);
		
		leftPanel = new JPanel();
		splitPane.setRightComponent(leftPanel);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		verticalStrut_2 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_2);
		
		JPanel updatePanel = new JPanel();
	//	buttonsPanel.setPreferredSize(new Dimension(250, 30));
	//	buttonsPanel.setMinimumSize(new Dimension(250, 30));
		updatePanel.setMaximumSize(new Dimension(32767, 30));
		leftPanel.add(updatePanel);
		updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.X_AXIS));
		
		horizontalStrut_4 = Box.createHorizontalStrut(10);
		updatePanel.add(horizontalStrut_4);
		
		updateCheckBox = new JCheckBox();
		updateCheckBox.setText("Immediate Update ");
		updateCheckBox.setActionCommand("Immediate Update ");
		updatePanel.add(updateCheckBox);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		updatePanel.add(horizontalGlue_1);
		
		updateButton = new JButton("Update");
		updateButton.setMaximumSize(new Dimension(80, 25));
		updateButton.setMinimumSize(new Dimension(80, 25));
		updateButton.setPreferredSize(new Dimension(80, 25));
		updateButton.setEnabled(false);
		updatePanel.add(updateButton);
		
		horizontalStrut_5 = Box.createHorizontalStrut(10);
		updatePanel.add(horizontalStrut_5);
		
		verticalStrut_4 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_4);
		
		imageScrollPane = new JScrollPane();
		imageScrollPane.setBorder(null);
		leftPanel.add(imageScrollPane);
		
		verticalStrut_5 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_5);
		
		compareButton = new JButton("Compare");
		compareButton.setMaximumSize(new Dimension(80, 25));
		compareButton.setMinimumSize(new Dimension(80, 25));
		compareButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		compareButton.setPreferredSize(new Dimension(80, 25));
		compareButton.setEnabled(false);
		leftPanel.add(compareButton);
		
		verticalStrut_6 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_6);
	}


}
