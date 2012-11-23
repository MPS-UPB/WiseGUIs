/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI.window.implementation;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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

    public PreprocessingWindow(MainWindow window) {
        super(window);
        this.setTitle("Preprocessing");
    }

    @Override
    protected void jRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (evt.getActionCommand().equals("Remove")) {
            selectedIndex = jChoicesPanelList.getSelectedIndex();

            if (selectedIndex != -1) {

                String selectedElement = (String) jChoicesModel.elementAt(selectedIndex);
                jChoicesModel.removeElement(selectedElement);
                selectedExecs.remove(selectedIndex);
            }
        }
    }

    protected void jAddButtonActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:

        if (evt.getActionCommand().equals("Add")) {

            selectedIndex = jListingPanelList.getSelectedIndex();


            if (selectedIndex != -1) {
                parametersWindow = new ParametersWindow(this);

                Operation newOperation = operations.get(selectedIndex).copy();
                //transfer ferestrei de parametri lista de parametri asociata operatiei
                parametersWindow.generateFields(newOperation);
                parametersWindow.setVisible(true);
            }


        }
    }

    protected void addListener() {
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent arg0) {
                mainWindow.setEnabled(false);
            }

            @Override
            public void windowIconified(WindowEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowDeiconified(WindowEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowDeactivated(WindowEvent arg0) {
            }

            @Override
            public void windowClosing(WindowEvent arg0) {

                //daca se inchide direct fereastra, se face clear la lista din dreapta
                //si readauga in stanga
                for (int index = 0; index < jChoicesModel.getSize(); index++) {
                    jListingModel.addElement(jChoicesModel.get(index));
                }
                jChoicesModel.removeAllElements();
                mainWindow.setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent arg0) {
            }

            @Override
            public void windowActivated(WindowEvent arg0) {
                //    mainWindow.setEnabled(false);
            }
        });
    }

    @Override
    protected void okClicked(java.awt.event.MouseEvent evt) {

        //transmit in main window lista de operatii, cu lista de parametri completata 90%
        //in main window se vor lansa in executie programele, dupa ce se vor adauga 2 parametri: fisierul de intrare si cel de iesire 
        //(sau doar fisierul de intrare, cel de iesire poate fi generat automat si transmis inapoi ca raspuns in ferestra principala)
        mainWindow.launchPreprocOperations(selectedExecs);
        mainWindow.setEnabled(true);
        dispose();
    }

    @Override
    protected void cancelClicked(MouseEvent evt) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to Cancel? All your operations will be lost!",
                "Canceling...",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {            
            
            swap();
            jChoicesModel.removeAllElements();
            mainWindow.setEnabled(true);
            dispose();
        }
    }

    @Override
    public void changeLists() {
        
          String selectedElement = (String) jListingModel.elementAt(selectedIndex);
        jListingModel.removeElement(selectedElement);
        jChoicesModel.addElement(selectedElement);
    }
}
