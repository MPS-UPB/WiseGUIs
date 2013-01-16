/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import mps.parser.Operation;

/**
 * Clasa care descrie partea comuna a ferestrelor de binarizare si de
 * preprocesare.
 * 
 * @author Luiza
 */
public abstract class SecondaryWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	protected DefaultListModel<String> jListingModel;
	protected DefaultListModel<String> jChoicesModel;
	protected JButton jAddButton;
	protected JButton jOKButton;
	protected JButton jCancelButton;
	protected JLabel jLabel3;
	protected JList<String> jChoicesPanelList;
	protected JList<String> jListingPanelList;
	protected JButton jRemoveButton;
	protected JScrollPane jScrollPane1;
	protected JScrollPane jScrollPane2;
	/**
	 * Fereastra principala care a deschis aceasta fereastra.
	 */
	protected MainWindow mainWindow;
	/**
	 * Fereastra de parametri pe care o va deschide aceasta fereastra.
	 */
	protected ParametersWindow parametersWindow;
	/**
	 * Lista de operatii de preprocesare / binarizare primita din fereastra
	 * principala.
	 */
	protected ArrayList<Operation> operations;
	/**
	 * Indexul executabilului selectat fie din panel-ul din stanga, fie din cel
	 * din dreapta.
	 */
	protected int selectedIndex;
	/**
	 * Lista cu executabilele selectate anterior (de la ultimul OK).
	 */
	ArrayList<Operation> oldSelection;
	/**
	 * Lista curenta cu executabilele nou selectate.
	 */
	ArrayList<Operation> newSelection;

	/**
	 * Lista cu executabilele sterse din lista veche (necesar la binarizare) si pozitia de pe care au fost sterse.
	 */
	LinkedHashMap<Operation, Integer> removedOps;

	/**
	 * Clasa ce descrie un CellRenderer, necesar la afisarea cu o culoare diferita a executabilelor selectate anterior
	 */
	protected class ListElement extends JLabel implements ListCellRenderer {

		public ListElement() {
			setOpaque(true);
		}

		public ListElement(String text) {
			setOpaque(true);
			this.setText(text);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			setText(value.toString());

			//Daca este executabil selectat anterior, atunci il afiseaza cu gri
			if (index < oldSelection.size()) {

				setForeground(Color.LIGHT_GRAY);
				setBackground(Color.WHITE);
			} else {

				//Altfel il afiseaza normal, cu negru
				setForeground(Color.BLACK);
			}

			if (isSelected) {

				setBackground(Color.BLUE);
			} else {

				setBackground(Color.WHITE);
			}

			return this;
		}
	}

	/**
	 * Constructor pentru o fereastra secundara.
	 * @param window fereastra principala, cu care trebuie sa comunice
	 */
	public SecondaryWindow(MainWindow window) {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {

				mainWindow.setEnabled(false);
				init();
			}

			@Override
			public void windowActivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
			
				mainWindow.setEnabled(true);
				close();
			}

		});

		addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {

				mainWindow.setEnabled(true);
				close();
			}

			public void componentShown(ComponentEvent e) {

				mainWindow.setEnabled(false);
				init();
			}
		});

		this.mainWindow = window;

		operations = new ArrayList<Operation>();
		jListingModel = new DefaultListModel<String>();
		jChoicesModel = new DefaultListModel<String>();
		oldSelection = new ArrayList<Operation>();
		removedOps = new LinkedHashMap<Operation, Integer>();
		newSelection = new ArrayList<Operation>();
		// initial nu este selectat nimic
		selectedIndex = -1;
		initComponents();
		setLocationRelativeTo(null);
	}

	protected void initComponents() {

		jScrollPane1 = new JScrollPane();
		jListingPanelList = new JList<String>(jListingModel);
		jListingPanelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollPane2 = new JScrollPane();
		jChoicesPanelList = new JList<String>(jChoicesModel);
		jChoicesPanelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jAddButton = new JButton();
		jRemoveButton = new JButton();
		jOKButton = new JButton();
		jCancelButton = new JButton();
		jLabel3 = new JLabel("Select ops from left:");

		//Afisare tool tips cand mouse-ul est ein dreptul unui element din lista
		jListingPanelList.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {

				int index = jListingPanelList.locationToIndex(e.getPoint());
				Rectangle rectangle = jListingPanelList.getCellBounds(index,
						index);
				if (rectangle != null && index >= 0
						&& index < jListingPanelList.getModel().getSize()
						&& rectangle.contains(e.getPoint())) {

					String execName = ((DefaultListModel<String>) jListingPanelList
							.getModel()).elementAt(index);
					for (Operation op : operations)
						if (op.getName().equals(execName)) {

							if (!op.getDesc().isEmpty())
								// setez Tool Tip cu descrierea executabilului
								jListingPanelList.setToolTipText(op.getDesc());
						}
				} else {

					jListingPanelList.setToolTipText(null);
				}
			}
		});

		jChoicesPanelList.setCellRenderer(new ListElement());

		//Afisare tool tips cand mouse-ul este in dreptul unui element din lista
		jChoicesPanelList.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {

				int index = jChoicesPanelList.locationToIndex(e.getPoint());
				ArrayList<Operation> allOps = new ArrayList<Operation>();
				allOps.addAll(oldSelection);
				allOps.addAll(newSelection);

				Rectangle rectangle = jChoicesPanelList.getCellBounds(index,
						index);

				if (rectangle != null && index >= 0
						&& index < jChoicesPanelList.getModel().getSize()
						&& rectangle.contains(e.getPoint())) {

					if (!allOps.get(index).toString().isEmpty()) {
						jChoicesPanelList.setToolTipText(allOps.get(index)
								.toString());
					}
				} else {

					jChoicesPanelList.setToolTipText(null);
				}
			}
		});

		jScrollPane1.setViewportView(jListingPanelList);
		jScrollPane2.setViewportView(jChoicesPanelList);

		jAddButton.setText("Add");
		jAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jAddButtonActionPerformed(evt);

			}
		});

		jRemoveButton.setText("Remove");
		jRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRemoveButtonActionPerformed(evt);
			}
		});

		jOKButton.setText("OK");
		jOKButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				SecondaryWindow.this.okClicked(evt);
			}
		});

		jCancelButton.setText("Cancel");
		jCancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				SecondaryWindow.this.cancelClicked(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(86, 86, 86)
								.addComponent(jOKButton,
										GroupLayout.PREFERRED_SIZE, 100,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jCancelButton,
										GroupLayout.PREFERRED_SIZE, 100,
										GroupLayout.PREFERRED_SIZE)
								.addGap(87, 87, 87))
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane1,
										GroupLayout.PREFERRED_SIZE, 233,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														jLabel3,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														jAddButton,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														jRemoveButton,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jScrollPane2,
										GroupLayout.PREFERRED_SIZE, 233,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(65, 65,
																		65)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						jScrollPane1,
																						GroupLayout.DEFAULT_SIZE,
																						210,
																						Short.MAX_VALUE)
																				.addComponent(
																						jScrollPane2)))
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addComponent(
																		jLabel3,
																		GroupLayout.PREFERRED_SIZE,
																		33,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(49, 49,
																		49)
																.addComponent(
																		jAddButton)
																.addGap(64, 64,
																		64)
																.addComponent(
																		jRemoveButton)))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(jOKButton)
												.addComponent(jCancelButton))
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		pack();
	}

	/**
	 * Operatii care se executa la deschiderea ferestrei.
	 */
	public void init() {

		jListingModel.clear();
		for (Operation op : operations) {

			jListingModel.addElement(op.getName());
		}

		jChoicesModel.clear();
		for (Operation op : oldSelection) {

			jChoicesModel.addElement(op.getName());
		}
	}

	/**
	 * Metoda care adauga un nume de excutabil in panelul din stanga.
	 * 
	 * @param elem
	 *            operatia al carui nume va fi adaugat in lista din stanga
	 */
	public void addListElement(Operation elem) {

		/*
		 *  Metoda va fi apelata in metoda init() din MainWindow;
		 */
		operations.add(elem);
	}

	/**
	 * Metoda care adauga o operatie cu tot cu valorile parametrilor acesteia in
	 * lista de operatii de executat asupra imaginii. - apelata de fereastra de
	 * parametri, pentru a transmite parametrii operatiei curente
	 */
	public void addExec(Operation op) {

		/*
		 *  Operatia se va adauga listei curente de executabile;
		 *  doar la apasarea butonului "OK" va fi transmisa mai departe in
		 *  fereastra principala, spre a fi lansata in executie
		 */

		op.setId(Operation.opGlobalId++);
		newSelection.add(op);

		// Executabilul ales din stanga trece in lista din dreapta
		changeLists();
	}

	/**
	 * Metoda apelata cand se apasa pe butonul "Add".
	 * 
	 * @param evt
	 */
	protected abstract void jAddButtonActionPerformed(ActionEvent evt);

	/**
	 * Metoda apelata cand se apasa pe butonul "Remove".
	 * 
	 * @param evt
	 */
	protected abstract void jRemoveButtonActionPerformed(ActionEvent evt);

	/**
	 * Metoda apelata cand se apasa pe butonul "OK".
	 * 
	 * @param evt
	 *            evenimentul mouse clicked
	 */
	protected void okClicked(MouseEvent evt) {

		mainWindow.setEnabled(true);
		this.setVisible(false);

		oldSelection.addAll(newSelection);
	}

	/**
	 * Metoda apelata cand se apasa pe butonul "Cancel".
	 * 
	 * @param evt
	 *            evenimentul mouse clicked
	 */
	protected void cancelClicked(MouseEvent evt) {

		if (!removedOps.isEmpty() || !newSelection.isEmpty()) {

			// Se intreaba utilizatorul daca este sigur ca vrea sa iasa din
			// fereastra, cu riscul de a pierde operatiile nou adaugate
			int result = JOptionPane
					.showConfirmDialog(
							this,
							"Are you sure you want to Cancel? All your operations will be lost!",
							"Canceling...", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {

				// Se executa instructiunile legate de iesirea din fereastra
				close();
			}
		} else
			close();
	}

	/**
	 * Metoda care realizeaza transferul unui nume de executabil din lista din
	 * stanga in lista din dreapta.
	 */
	public abstract void changeLists();

	/**
	 * Metoda in care sunt definite o serie de instructiuni de executat atnci
	 * cand se doreste inchiderea ferestrei, fara salvarea modificarilor asupra
	 * listei de excutabile.
	 */
	public void close() {

		newSelection.clear();

		ListIterator<Operation> iter =
			    new ArrayList<Operation>(removedOps.keySet()).listIterator(removedOps.size());

		/*
		 * Daca se vrea iesirea directa din fereastra, fara apasarea OK,
		 * atunci eventualele operatii eliminate din lista se trec inapoi, pe aceleasi pozitii
		 */
		while (iter.hasPrevious()) {
			
		   Operation op = iter.previous();
		   oldSelection.add(removedOps.get(op), op);			    
		}
		
		removedOps.clear();

		mainWindow.setEnabled(true);
		this.setVisible(false);
	}
}
