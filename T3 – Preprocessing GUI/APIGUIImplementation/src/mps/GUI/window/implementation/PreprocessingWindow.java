/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI.window.implementation;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;


/**
 *
 * @author John
 * @version Last Modified Roxana 11/15/2012
 */
public class PreprocessingWindow extends SecondaryWindow {

    private static final long serialVersionUID = 1L;

	
    private JButton jAddButton;
    private JList jChoicesPanelList;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JList jListingPanelList;
    private JButton jRemoveButton;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
	DefaultListModel jListingModel = new DefaultListModel();
    DefaultListModel jChoicesModel = new DefaultListModel();


	 MainWindow mainWindow;
	 ParametersWindow parametersWindow;
    
    /**
     * Creates new form PreprocessingWindow
     */
    public PreprocessingWindow(MainWindow window) {
        super("Preprocessing Window");
        initComponents();

        super(window);
        this.setTitle("Preprocessing");
    }

    @Override
    protected void jAddButtonActionPerformed(java.awt.event.ActionEvent evt) {

    private void jAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (evt.getActionCommand().equals("Add")) {

            parametersWindow = new ParametersWindow(this);

            int selectedIndex = jListingPanelList.getSelectedIndex();

            //daca intr-adevar s-a selectat un element
            if (selectedIndex != -1) {

                Operation newOperation = operations.get(selectedIndex).clone();
                 //transfer ferestrei de parametri lista de parametri asociata operatiei
                parametersWindow.generareCampuri(newOperation);               

                String selectedElement = (String) jListingModel.elementAt(selectedIndex);

                jListingModel.removeElement(selectedElement);
                jChoicesModel.addElement(selectedElement);

                parametersWindow.setVisible(true);
            }
        }
    }

    @Override
    protected void jRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (evt.getActionCommand().equals("Remove")) {
            int selectedIndex = jChoicesPanelList.getSelectedIndex();

            if (selectedIndex != -1) {

                String selectedElement = (String) jChoicesModel.elementAt(selectedIndex);

                jChoicesModel.removeElement(selectedElement);
                jListingModel.addElement(selectedElement);
                //elimin si operatia asociata acelui executabil
                selectedExecs.remove(selectedIndex);
            }
        }
    }
    
    @Override
    protected void okClicked (java.awt.event.MouseEvent evt) {

        //transmit in main window lista de operatii, cu lista de parametri completata 90%
        //in main window se vor lansa in executie programele, dupa ce se vor adauga 2 parametri: fisierul de intrare si cel de iesire 
        //(sau doar fisierul de intrare, cel de iesire poate fi generat automat si transmis inapoi ca raspuns in ferestra principala)
        
        mainWindow.launchPreprocOperations(selectedExecs);
        
        closeWindow();
    }
}
