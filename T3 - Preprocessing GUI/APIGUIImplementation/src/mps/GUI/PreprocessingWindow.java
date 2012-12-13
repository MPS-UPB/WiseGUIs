/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI;

import mps.parser.Operation;
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

                //cand se inchide fereastra de la "X", nu se salveaza modificarile
                close();
            }

            @Override
            public void windowClosed(WindowEvent arg0) {
            }

            @Override
            public void windowActivated(WindowEvent arg0) {
                //    mainWindow.setEnabled(false);
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {

                close();
            }

            public void componentShown(ComponentEvent e) {
                //    mainWindow.setEnabled(false);
            }
        });
    }

    @Override
    protected void jAddButtonActionPerformed(ActionEvent evt) {

        if (evt.getActionCommand().equals("Add")) {

            selectedIndex = jListingPanelList.getSelectedIndex();

            //Daca s-a selectat ceva din lista din stanga
            if (selectedIndex != -1) {

                String selectedElement = (String) jListingModel.elementAt(selectedIndex);

                //Procedeul de aici nu mai este acelasi ca in fereastra de binarizare, pentru ca executabilele se muta dintr-o lista in alta
                //Este necesara cautarea "manuala" a executabilului in operations, dupa nume
                
                for (Operation op : operations) {

                    if (selectedElement.equals(op.getName())) {

                        Operation newOperation = op.copy();
                        //transfer ferestrei de parametri lista de parametri asociata operatiei
                        
                        //daca are si alti parametri, in afara de fisierul de input si cel de output
                          if (newOperation.getParameters().size() == 2) {

                            currentSelection.add(newOperation);
                            //Executabilul ales din stanga trece in lista din dreapta
                            changeLists();
                        } else {

                            ParametersWindow parametersWindow = new ParametersWindow(this, newOperation);
                            parametersWindow.setVisible(true);
                        }
                        
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void jRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (evt.getActionCommand().equals("Remove")) {
            selectedIndex = jChoicesPanelList.getSelectedIndex();

            //Daca s-a selectat ceva din lista din dreapta
            if (selectedIndex != -1) {

                //Elementul este eliminat din lista din dreapta si trecut inapoi in lista din stanga                
                String selectedElement = (String) jChoicesModel.elementAt(selectedIndex);
                jChoicesModel.removeElement(selectedElement);
                currentSelection.remove(selectedIndex);

                //Daca elimin un executabil, sa fie pus inapoi in lista din stanga pe aceeasi pozitie pe care era inainte 
                //(relativ la celelalte elemente, sa se pastreze ordinea elementelor, ca sa nu ametim utilizatorul)

                Object[] ceva = jListingModel.toArray();
                jListingModel.clear();

                for (Operation op : operations) {

                    if (op.getName().equals(selectedElement)) {

                        jListingModel.addElement(selectedElement);
                    }

                    for (int j = 0; j < ceva.length; j++) {
                        if (op.getName().equals(ceva[j])) {

                            jListingModel.addElement(ceva[j].toString());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void okClicked(java.awt.event.MouseEvent evt) {

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
        //Se transfera in fereastra principala lista cu executabilele de preprocesare ce trebuie aplicate imaginii
        mainWindow.launchPreprocOperations(currentSelection);
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

        //La adaugarea unui executabil, acesta este trecut din stanga in dreapta
        String selectedElement = (String) jListingModel.elementAt(selectedIndex);
        jListingModel.removeElement(selectedElement);
        jChoicesModel.addElement(selectedElement);
    }

    @Override
    public void close() {

        //Se reinitializeaza cele doua liste
        //Se reconstruiesc listele, revenindu-se la starea de la ultimul "OK"

        jListingModel.removeAllElements();

        for (Operation op : operations) {

            jListingModel.addElement(op.getName());
        }

        currentSelection.clear();
        jChoicesModel.removeAllElements();

        for (Operation op : oldSelection) {

            jChoicesModel.addElement(op.getName());
            jListingModel.removeElement(op.getName());
        }

        currentSelection.addAll(oldSelection);

        mainWindow.setEnabled(true);
        this.setVisible(false);
    }

    /*
     protected void swap() {

     for (int index = 0; index < jChoicesModel.getSize(); index++) {
     jListingModel.addElement(jChoicesModel.get(index));
     }
     jChoicesModel.removeAllElements();
     }
     */
}
