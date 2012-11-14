package mps.GUI.window.implementation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ProcessingWindow extends SecondaryWindow{

	public ProcessingWindow( List<String> list) {
		super("Processing Window", list);
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
					if(!listAdded.contains(selected)){
						listAdded.addElement(selected);
						listExecs.removeElement(selected);
						ProcessingWindow.this.revalidate();
					}
					
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
						listExecs.addElement(selected);
						listAdded.removeElementAt(listAdded.indexOf(selected));
						ProcessingWindow.this.revalidate();
					}
					
				}
			});
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> data = new ArrayList<String>(Arrays.asList("one", "two", "three", "four"));
        SecondaryWindow sw = new ProcessingWindow(data); 
        sw.setVisible(true);
        
	}

	


	}


