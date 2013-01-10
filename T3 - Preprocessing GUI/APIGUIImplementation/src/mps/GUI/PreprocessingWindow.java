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

	@Override
	protected void jAddButtonActionPerformed(ActionEvent evt) {

		if (evt.getActionCommand().equals("Add")) {

			selectedIndex = jListingPanelList.getSelectedIndex();

			// Daca s-a selectat ceva din lista din stanga
			if (selectedIndex != -1) {

				String selectedElement = (String) jListingModel
						.elementAt(selectedIndex);

				// Procedeul de aici nu mai este acelasi ca in fereastra de
				// binarizare, pentru ca executabilele se muta dintr-o lista in
				// alta
				// Este necesara cautarea "manuala" a executabilului in
				// operations, dupa nume

				for (Operation op : operations) {

					if (selectedElement.equals(op.getName())) {

						Operation newOperation = op.copy();
						// transfer ferestrei de parametri lista de parametri
						// asociata operatiei

						// daca are si alti parametri, in afara de fisierul de
						// input si cel de output
						if (newOperation.getParameters().size() == 2) {

							newSelection.add(newOperation);
							// Executabilul ales din stanga trece in lista din
							// dreapta
							changeLists();
						} else {

							ParametersWindow parametersWindow = new ParametersWindow(
									this, newOperation);
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

			// Daca s-a selectat ceva din lista din dreapta
			if (selectedIndex != -1) {

				// Elementul este eliminat din lista din dreapta si trecut
				// inapoi in lista din stanga
				String selectedElement = jChoicesModel.elementAt(selectedIndex);
				jChoicesModel.removeElementAt(selectedIndex);		

				// elimin din oldSelection
				if (selectedIndex > oldSelection.size() - 1) {

					newSelection.remove(selectedIndex - oldSelection.size());
				}
				// elimin din newSelection
				else {

					//se pot retine si aici operatiile sterse in removedOps
					oldSelection.remove(selectedIndex);
				}

				// Daca elimin un executabil, sa fie pus inapoi in lista din
				// stanga pe aceeasi pozitie pe care era inainte
				// (relativ la celelalte elemente, sa se pastreze ordinea
				// elementelor, ca sa nu ametim utilizatorul)
				
				Object[] ceva = jListingModel.toArray();

				// daca elementul selectat este in jListingModel deja, atunci
				// cand il sterg nu-l mai trec dincolo
				boolean yesRemove = true;
				for (int i = 0; i < ceva.length; i++) {

					if (selectedElement.equals(ceva[i].toString())) {
						yesRemove = false;
						break;
					}
				}

				if (yesRemove) {

					jListingModel.clear();
					for (Operation op2 : operations) {

						if (op2.getName().equals(selectedElement)) {

							jListingModel.addElement(selectedElement);
						}

						for (int j = 0; j < ceva.length; j++) {
							if (op2.getName().equals(ceva[j])) {

								jListingModel.addElement(ceva[j].toString());
							}
						}
					}
				}
			}

		}
	}

	@Override
	protected void okClicked(java.awt.event.MouseEvent evt) {

		// Se transmite in Main Window lista de operatii, cu lista de parametri
		// completata 90% (adica fara fisierul de intrare)
		// In Main Window se vor lansa in executie programele, dupa ce se vor
		// adauga 2 parametri: fisierul de intrare si cel de iesire
		// (sau doar fisierul de intrare, cel de iesire poate fi generat automat
		// si transmis inapoi ca raspuns in ferestra principala)

		super.okClicked(evt);
        //Se transfera in fereastra principala lista cu executabilele de preprocesare ce trebuie aplicate imaginii
        mainWindow.launchPreprocOperations(newSelection);
        newSelection.clear();
	}

	@Override
	public void changeLists() {

		// La adaugarea unui executabil, acesta este trecut din stanga in
		// dreapta
		String selectedElement = jListingModel.elementAt(selectedIndex);
		jListingModel.removeElementAt(selectedIndex);
		jChoicesModel.addElement(selectedElement);
	}
}
