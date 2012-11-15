package old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BinarizationWindow extends SecondaryWindow{

	public BinarizationWindow( List<String> list) {
		super("Binarization Window", list);
	}

	private static final long serialVersionUID = 1L;

	


	@Override
	public void addALtoAdd() {
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!listedExecs.isSelectionEmpty()){
					String selected = listedExecs.getSelectedValue();
					System.out.println("selected : "+selected);
					listAdded.addElement(selected);
					BinarizationWindow.this.revalidate();
				}
				
			}
		});
	}
	
	@Override
	public void addALtoRemove() {
		 removeButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!choosenExecs.isSelectionEmpty()){
						String selected = choosenExecs.getSelectedValue();
						System.out.println("selected for remove: "+selected);
						listAdded.removeElementAt(listAdded.indexOf(selected));
						BinarizationWindow.this.revalidate();
					}
					
				}
			});
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> data = new ArrayList<String>(Arrays.asList("one", "two", "three", "four"));
        SecondaryWindow sw = new BinarizationWindow(data); 
        sw.setVisible(true);
        
	}

	


	}


