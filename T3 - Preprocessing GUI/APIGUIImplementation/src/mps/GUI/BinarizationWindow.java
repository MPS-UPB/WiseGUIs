package mps.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Map;

import mps.parser.Operation;

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

	@Override
	protected void jAddButtonActionPerformed(ActionEvent evt) {

		if (evt.getActionCommand().equals("Add")) {

			selectedIndex = jListingPanelList.getSelectedIndex();

			// Daca s-a selectat ceva din lista din dreapta
			if (selectedIndex != -1) {

				// Se creeaza o copie a tipului de operatie (rotate, otsu etc.),
				// pentru a avea lista de parametri cu tipul lor
				// Indexul elementului selectat din lista din stanga este
				// acelasi cu al aceluiasi element in lista operations
				Operation newOperation = operations.get(selectedIndex).copy();
				// Se creeaza o fereastra de parametri careia i se transfera
				// lista de parametri asociata operatiei, incapsulata in
				// newOperation

				if (newOperation.getParameters().size() == 2) {

					addExec(newOperation);

				} else {

					ParametersWindow parametersWindow = new ParametersWindow(
							this, newOperation);
					parametersWindow.setVisible(true);
				}
			}
		}
	}

	@Override
	protected void jRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {

		if (evt.getActionCommand().equals("Remove")) {

			selectedIndex = jChoicesPanelList.getSelectedIndex();

			// Daca s-a selectat ceva din lista din stanga
			if (selectedIndex != -1) {

				// Se elimina elementul selectat din lista din stanga
				String selectedElement = jChoicesModel.elementAt(selectedIndex);
				jChoicesModel.removeElementAt(selectedIndex);

				// elimin din newSelection
				if (selectedIndex > oldSelection.size() - 1) {

					newSelection.remove(selectedIndex - oldSelection.size());
				}
				// altfel, elimin din oldSelection
				else {

					removedOps.put(oldSelection.remove(selectedIndex), selectedIndex);
				}
			}
		}
	}

	@Override
	protected void okClicked(MouseEvent evt) {

		// Se transmite in Main Window lista de operatii, cu lista de parametri
		// completata 90% (adica fara fisierul de intrare)
		// In Main Window se vor lansa in executie programele, dupa ce se vor
		// adauga 2 parametri: fisierul de intrare si cel de iesire
		// (sau doar fisierul de intrare, cel de iesire poate fi generat automat
		// si transmis inapoi ca raspuns in ferestra principala)

		super.okClicked(evt);
		// Se transfera in fereastra principala lista cu executabilele de
		// binarizare ce trebuie aplicate imaginii
		mainWindow.launchBinarizOperations(newSelection);
		newSelection.clear();

		for (Map.Entry<Operation, Integer> entry : removedOps.entrySet()) {

				mainWindow.removeBinarization(entry.getKey());
		}		
		removedOps.clear();
	}

	@Override
	public void changeLists() {

		// Se trece un nume de executabil din stanga in dreapta, fara a fi sters
		// din lista din stanga
		String selectedElement = jListingModel.elementAt(selectedIndex);
		jChoicesModel.addElement(selectedElement);

	}
}
