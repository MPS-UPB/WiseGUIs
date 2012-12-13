package mps.GUI.window.implementation;

import mps.parser.implementation.Operation;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 *
 * @author John
 * @version Last modified by Roxana 11/15/2012
 */
public class BinarizationWindow extends SecondaryWindow {

    private static final long serialVersionUID = 1L;

    public BinarizationWindow(MainWindow window) {

        super(window);
        this.setTitle("Binarization");
    }

    protected void addListener() {
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent arg0) {
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

                close();
            }

            @Override
            public void windowClosed(WindowEvent arg0) {
            }

            @Override
            public void windowActivated(WindowEvent arg0) {
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {


                close();

            }

            public void componentShown(ComponentEvent e) {
                //      mainWindow.setEnabled(false);
            }
        });
    }

    @Override
    protected void jAddButtonActionPerformed(ActionEvent evt) {

        if (evt.getActionCommand().equals("Add")) {

            selectedIndex = jListingPanelList.getSelectedIndex();

            //Daca s-a selectat ceva din lista din dreapta
            if (selectedIndex != -1) {

                //Se creeaza o copie a tipului de operatie (rotate, otsu etc.), pentru a avea lista de parametri cu tipul lor
                //Indexul elementului selectat din lista din stanga este acelasi cu al aceluiasi element in lista operations
                Operation newOperation = operations.get(selectedIndex).copy();
                //Se creeaza o fereastra de parametri careia i se transfera lista de parametri asociata operatiei, incapsulata in newOperation
              
                
                    parametersWindow = new ParametersWindow(this, newOperation);
                    parametersWindow.setVisible(true);
            }
        }
    }

    @Override
    protected void jRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (evt.getActionCommand().equals("Remove")) {

            selectedIndex = jChoicesPanelList.getSelectedIndex();

            //Daca s-a selectat ceva din lista din stanga 
            if (selectedIndex != -1) {

                //Se elimina elementul selectat din lista din stanga
                String selectedElement = (String) jChoicesModel.elementAt(selectedIndex);
                jChoicesModel.removeElement(selectedElement);
                mainWindow.removeBinarization(currentSelection.remove(selectedIndex));
            }
        }
    }

    @Override
    protected void okClicked(MouseEvent evt) {

        //Se transmite in Main Window lista de operatii, cu lista de parametri completata 90% (adica fara fisierul de intrare)
        //In Main Window se vor lansa in executie programele, dupa ce se vor adauga 2 parametri: fisierul de intrare si cel de iesire 
        //(sau doar fisierul de intrare, cel de iesire poate fi generat automat si transmis inapoi ca raspuns in ferestra principala)

        mainWindow.setEnabled(true);
        this.setVisible(false);

        //Vechea selectie devine noul set de executabile selectate 
        ArrayList<Operation> aux = new ArrayList<Operation>();
        aux.addAll(oldSelection);

        oldSelection.clear();
        oldSelection.addAll(currentSelection);

        currentSelection.removeAll(aux);
        //Se transfera in fereastra principala lista cu executabilele de binarizare ce trebuie aplicate imaginii
        mainWindow.launchBinarizOperations(currentSelection);
    }

    @Override
    protected void cancelClicked(MouseEvent evt) {

        //Se intreaba utilizatorul daca este sigur ca vrea sa iasa din fereastra, cu riscul de apierde operatiile nou adaugate
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to Cancel? All your operations will be lost!",
                "Canceling...",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {

            //Se executa instructiunile legate de iesirea din fereastra
            close();
        }
    }

    @Override
    public void changeLists() {

        //Se trece un nume de executabil din stanga in dreapta, fara a fi sters din lista din stanga
        String selectedElement = (String) jListingModel.elementAt(selectedIndex);
        jChoicesModel.addElement(selectedElement);

    }

    @Override
    public void close() {

        //    mainWindow.setEnabled(true); //merge si fara asta
        this.setVisible(false);

        //Se iese fara a fi salvate modificarile asupra listei de executabile selectate pentru aplicare asupra imaginii.
        currentSelection.clear();
        //Se revine la setul anterior de executabile (de la ultimul "OK")
        currentSelection.addAll(oldSelection);
        jChoicesModel.removeAllElements();
        //Se readauga in lista din dreapta numele de executabile selectate, de la ultimul "OK"
        for (Operation op : oldSelection) {

            jChoicesModel.addElement(op.getName());
        }
    }
}
