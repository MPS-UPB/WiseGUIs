/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI.window.implementation;

import java.awt.event.ActionEvent;
import javax.swing.*;

import java.util.*;

/**
 *
 * @author John
 * @version Last modified by Roxana 11/15/2012
 */
public class BinarizationWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	DefaultListModel<String> jListingModel = new DefaultListModel<String>();
    DefaultListModel<String> jChoicesModel = new DefaultListModel<String>();
    MainWindow mainWindow;
    
    private JButton jAddButton;
    private JList <String>jChoicesPanelList;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JList<String> jListingPanelList;
    private JButton jRemoveButton;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    
    /**
     * Creates new form BinarizationWindow
     */
    public BinarizationWindow(List<String>list) {
        
    	
    	initComponents();
        
            
    	for(String s : list){
        	jListingModel.addElement(s);
		}

        jListingPanelList.setModel(jListingModel);
        jChoicesPanelList.setModel(jChoicesModel);
    }

    void setMainWindow(MainWindow mainWindow2) {
		// TODO Auto-generated method stub
		mainWindow = mainWindow2;
	}

	private void initComponents() {

        jScrollPane1 = new JScrollPane();
        jListingPanelList = new JList<String>();
        jScrollPane2 = new JScrollPane();
        jChoicesPanelList = new JList<String>();
        jAddButton = new JButton();
        jRemoveButton = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(jListingPanelList);
        jScrollPane2.setViewportView(jChoicesPanelList);

        jAddButton.setText("Add");
        jAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddButtonActionPerformed(evt);
            }
        });

        jRemoveButton.setText("Remove");
        jRemoveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jRemoveButtonActionPerformed(evt);
            }
        });
        jLabel1.setText("Listing Panel");

        jLabel2.setText("Choices Panel");

        jLabel3.setText("Binarization Window");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jAddButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRemoveButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jAddButton)
                        .addGap(64, 64, 64)
                        .addComponent(jRemoveButton)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void jAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (evt.getActionCommand().equals("Add")) {
            int selectedIndex = jListingPanelList.getSelectedIndex();

            String selectedElement = jListingModel.elementAt(selectedIndex);
            
            jChoicesModel.addElement(selectedElement);
                
       }
    }
    private void jRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (evt.getActionCommand().equals("Remove")) {
            int selectedIndex = jChoicesPanelList.getSelectedIndex();

            Object selectedElement = jChoicesModel.elementAt(selectedIndex);
            
            jChoicesModel.removeElement(selectedElement);     
       }
    }

  
}
