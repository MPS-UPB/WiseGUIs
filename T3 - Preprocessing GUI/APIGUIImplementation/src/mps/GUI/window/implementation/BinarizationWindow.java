package mps.GUI.window.implementation;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

import javax.swing.JOptionPane;

/**
 *
 * @author John
 * @version Last modified by Roxana 11/15/2012
 */
public class BinarizationWindow extends SecondaryWindow {

    private static final long serialVersionUID = 1L;
    Object[] oldSelection = new Object[50];

    public BinarizationWindow(MainWindow window) {
        super(window);
        this.setTitle("Binarization");
    }

    @Override
    protected void jRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (evt.getActionCommand().equals("Remove")) {

            selectedIndex = jChoicesPanelList.getSelectedIndex();

            if (selectedIndex != -1) {
                String selectedElement = (String) jChoicesModel.elementAt(selectedIndex);
                jChoicesModel.removeElement(selectedElement);
                selectedExecs.remove(selectedIndex);
                jListingModel.addElement(selectedElement);
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
                swap();
                mainWindow.setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent arg0) {
            }

            @Override
            public void windowActivated(WindowEvent arg0) {
            }
        });
    }

    @Override
    protected void okClicked(MouseEvent evt) {

        //transmit in main window lista de operatii, cu lista de parametri completata 90%
        //in main window se vor lansa in executie programele, dupa ce se vor adauga 2 parametri: fisierul de intrare si cel de iesire 
        //(sau doar fisierul de intrare, cel de iesire poate fi generat automat si transmis inapoi ca raspuns in ferestra principala)

        oldSelection = jChoicesModel.toArray();
        mainWindow.launchBinarizOperations(selectedExecs);
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
                      
            jChoicesModel.removeAllElements();
            mainWindow.setEnabled(true);
            dispose();
        }
    }

    @Override
    public void changeLists() {
        
        String selectedElement = (String) jListingModel.elementAt(selectedIndex);
        jChoicesModel.addElement(selectedElement);
    }
}
