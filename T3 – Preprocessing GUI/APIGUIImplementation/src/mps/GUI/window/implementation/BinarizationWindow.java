/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI.window.implementation;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import java.util.*;

/**
 *
 * @author John
 * @version Last modified by Roxana 11/15/2012
 */
public class BinarizationWindow extends SecondaryWindow {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form BinarizationWindow
     */
    public BinarizationWindow(MainWindow window) {


        super(window);
        this.setTitle("Binarization");
    }

    @Override
    protected void jAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (evt.getActionCommand().equals("Add")) {

            parametersWindow = new ParametersWindow(this);

            int selectedIndex = jListingPanelList.getSelectedIndex();

            if (selectedIndex != -1) {

                //clonez tipul de operatie
                Operation newOperation = operations.get(selectedIndex).clone();
                parametersWindow.generareCampuri(newOperation);

                String selectedElement = (String) jListingModel.elementAt(selectedIndex);
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

                Object selectedElement = jChoicesModel.elementAt(selectedIndex);

                jChoicesModel.removeElement(selectedElement);
                //daca se elimina un executabil selectat, trebuie sa elimin si operatia asociata cu acesta
                selectedExecs.remove(selectedIndex);
            }
        }
    }
    
    @Override
    protected void okClicked (java.awt.event.MouseEvent evt) {

        //transmit in main window lista de operatii, cu lista de parametri completata 90%
        //in main window se vor lansa in executie programele, dupa ce se vor adauga 2 parametri: fisierul de intrare si cel de iesire 
        //(sau doar fisierul de intrare, cel de iesire poate fi generat automat si transmis inapoi ca raspuns in ferestra principala)
        
        mainWindow.launchBinarizOperations(selectedExecs);
        
        closeWindow();
    }
}
