package old;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

/**
 * 
 * @author Roxana
 *
 */

public abstract  class SecondaryWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	
	DefaultListModel<String> listExecs;
	DefaultListModel<String> listAdded;
	JPanel panel;
    JButton saveButton;
    JButton cancelButton;
    JButton addButton;
    JButton removeButton;
    JList<String> listedExecs;
    JList<String> choosenExecs;
    
    
	public SecondaryWindow(String title, List<String> list){
		super(title);
		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setSize(510, 200);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		 
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/2;
		 
		setLocation(x, y);
		
		setResizable(false);
		
		listExecs = new DefaultListModel<String>();
		for(String s : list){
			listExecs.addElement(s);
		}
		listAdded = new DefaultListModel<String>();
	    panel = new JPanel();
	    saveButton = new JButton("Save");
	    cancelButton = new JButton("Cancel");
	    addButton = new JButton("Add");
	    removeButton = new JButton("Remove");
	    listedExecs = new JList<String>(listExecs);
	    choosenExecs = new JList<String>(listAdded);
	    JPanel downPanel = new JPanel();
	    JPanel panelListedExecs = new JPanel();
	    JPanel panelChoosenExecs = new JPanel();
	    JPanel listPanel = new JPanel();
	    
	    cancelButton.addActionListener(new ActionListener(){
			@Override
 			public void actionPerformed(ActionEvent e) {
 				//setLocationRelativeTo();
 				int result = JOptionPane.showConfirmDialog(
 			            panel,
 			            "Are you sure you want to cancel",
 			            "Canceling...",
 			            JOptionPane.YES_NO_OPTION);
 			    if (result == JOptionPane.YES_OPTION){
 			    	dispose();
 			    }
					
 			}
		});
	    addALtoAdd();
	    addALtoRemove();
	   
		
	
	    
	    
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    downPanel.setBorder(BorderFactory.createBevelBorder(1));
	    downPanel.add(saveButton);
	    downPanel.add(cancelButton);
	   
	    panelChoosenExecs.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(2, 1, 1, 1, Color.LIGHT_GRAY),
				"Choosen executables"));
	    
	    panelListedExecs.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(2, 1, 1, 1, Color.LIGHT_GRAY),
				"Available executables"));
	    
	    listedExecs.setFixedCellHeight(20);
	    listedExecs.setFixedCellWidth(150);
	    choosenExecs.setFixedCellHeight(20);
	    choosenExecs.setFixedCellWidth(150);
	    
	    JPanel addRemovePanel = new JPanel();
	    
	    addRemovePanel.setLayout(new FlowLayout());
	    addRemovePanel.add(addButton);
	    addRemovePanel.add(removeButton);
	    
		JScrollPane scroll1 = new JScrollPane(listedExecs,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll1.setPreferredSize(new Dimension(150,100));
		
		JScrollPane scroll2 = new JScrollPane(choosenExecs,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	 
		scroll2.setPreferredSize(new Dimension(150,100));
	    panelListedExecs.add(scroll1,BorderLayout.NORTH);
	    panelChoosenExecs.add(scroll2,BorderLayout.NORTH);
	    
	    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
	    listPanel.add(panelListedExecs,BorderLayout.WEST);
	    listPanel.add(addRemovePanel,BorderLayout.CENTER);
	    listPanel.add(panelChoosenExecs,BorderLayout.EAST);
	    
	    panel.add(listPanel,BorderLayout.CENTER);
	    
	    panel.add(downPanel,BorderLayout.SOUTH);
	    
	    add(panel);
	    
	    
	}
	

	public abstract void addALtoRemove();


	public abstract void addALtoAdd() ;
}

