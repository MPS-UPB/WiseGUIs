package mps.GUI;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Liz
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

import mps.parser.Attribute;
import mps.parser.ComplexTypeParameter;
import mps.parser.Operation;
import mps.parser.SimpleTypeParameter;

/**
 * Fereastra de parametri
 * @author Luiza
 */

public class ParametersWindow extends javax.swing.JDialog {

	/**
	 * Operatia ale carei campuri pentru parametri vor fi generate
	 */
	Operation crtOp;
	/**
	 * Fereastra de binarizare sau de preprocesare care a deschis fereastra de parametri
	 */
	SecondaryWindow motherWindow;
	/**
	 * Lista de parametri ai operatiei
	 */
	ArrayList<SimpleTypeParameter> params;
	/**
	 * Lista de elemente JTextField
	 */
	ArrayList<JTextField> textFields;
	/**
	 * Lista de elemente JComboBox
	 */
	ArrayList<JComboBox<String>> combos;
	/**
	 * Lista de elemente JSpinner
	 */
	ArrayList<JSpinner> spinners;
	/**
	 * Lista de elemente JCheckBox (pentru atribute)
	 */
	ArrayList<JCheckBox> checkboxes;
	/**
	 * Eroarea de necompletare camp.
	 */
	String eroare1;
	/**
	 * Eroarea de completare gresita camp.
	 */
	String eroare2;

	
	ArrayList<Component> graphicElements;
	protected JPanel buttonsPanel;
	protected JButton okButton;
	protected JButton cancelButton;
	protected Component horizontalStrut;
	protected Component horizontalGlue;
	protected Component horizontalGlue_1;
	protected JPanel paramsPanel;
	protected JPanel param1Panel;
	protected JLabel elemName;
	protected Component verticalStrut;
	protected Component horizontalStrut_1;
	protected Component horizontalStrut_2;
	protected Component horizontalStrut_3;
	protected JSpinner elem;
	protected Component verticalStrut_1;
	protected JLabel errorLabel;
	protected JPanel attrPanel;
	protected JCheckBox chckbxNewCheckBox;

	/**
	 * Constructorul clasei de parametri
	 * 
	 * @param window
	 *            reprezinta fereastra anterioara(de la care s-a ajuns aici)
	 * @param op
	 *            este un obiect al clasei operatii, obiect care este descris in
	 *            clasa lui
	 */
	public ParametersWindow(SecondaryWindow window, Operation op) {
		setMinimumSize(new Dimension(300, 120));

		initComponents();
		setTitle(op.getName());
		this.motherWindow = window;

		textFields = new ArrayList<JTextField>();
		combos = new ArrayList<JComboBox<String>>();
		spinners = new ArrayList<JSpinner>();
		checkboxes = new ArrayList<JCheckBox>();

		graphicElements = new ArrayList<Component>();

		initializareErori();
		//Generarea dinamica a campurilor pentru introducerea parametrilor
		generateFields(op); 
	}

	/**
	 * Metoda pentru initializarea componentelor grafice ale ferestrei
	 */
	private void initComponents() {
		
		//Pozitionarea ferestrei in centrul ecranului
		setLocationRelativeTo(null); 

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {

				motherWindow.setEnabled(false);
			}

			@Override
			public void windowClosing(WindowEvent arg0) {

				close();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
		});	

		setResizable(false);
		setPreferredSize(new Dimension(300, 120));

		paramsPanel = new JPanel();
		getContentPane().add(paramsPanel, BorderLayout.CENTER);
		paramsPanel.setLayout(new BoxLayout(paramsPanel, BoxLayout.Y_AXIS));

		errorLabel = new JLabel("");
		errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		errorLabel.setPreferredSize(new Dimension(300, 15));
		errorLabel.setMinimumSize(new Dimension(300, 15));
		errorLabel.setMaximumSize(new Dimension(300, 15));

		buttonsPanel = new JPanel();
		buttonsPanel.setMaximumSize(new Dimension(250, 50));
		buttonsPanel.setMinimumSize(new Dimension(250, 50));
		buttonsPanel.setPreferredSize(new Dimension(250, 50));
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		horizontalGlue = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue);

		okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(100, 30));
		okButton.setMaximumSize(new Dimension(100, 30));
		okButton.setMinimumSize(new Dimension(100, 30));
		buttonsPanel.add(okButton);

		horizontalStrut = Box.createHorizontalStrut(20);
		buttonsPanel.add(horizontalStrut);

		cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(100, 30));
		cancelButton.setMaximumSize(new Dimension(100, 30));
		cancelButton.setMinimumSize(new Dimension(100, 30));
		buttonsPanel.add(cancelButton);

		horizontalGlue_1 = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue_1);

		okButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ParametersWindow.this.okClicked(evt);
			}
		});

		cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ParametersWindow.this.cancelClicked(evt);
			}
		});

		pack();
	}

	/**
	 * Initializarea string-urilor de eroare
	 */
	private void initializareErori() {
		eroare1 = "Trebuie sa completati toate campurile goale";
		eroare2 = "Introduceti doar date valide(litere)";
	}

	/**
	 * Aceasta metoda specifica ce se intampla la apasarea butonului OK. Se
	 * preiau datele introduse in TextField, ComboBox si Spinner (doar daca au
	 * fost introduse corect, altfel se afisaza un mesaj de eroare) si se
	 * transmit catre fereastra secundara(anterioara)
	 * 
	 * @param evt
	 *            este evenimentu ce reprezinta click-ul pe OK
	 */
	private void okClicked(java.awt.event.MouseEvent evt) {

		//Pattern pentru verificarea corectitudinii datelor introduse in TextFields
		Pattern p = Pattern.compile("[0-9]");
	
		// numar elementele in total; e indice pt graphicElements
		int ii = -1;
		// numar doar atributele; e indice pt checkboxes
		int jj = -1;

		// completez parametrii
		for (int i = 2; i < params.size(); i++) {

			SimpleTypeParameter param = params.get(i);
			ii++;

			// caz special - cand parametrul are restrictie de tip enumeration
			if (param.getRestrictions().enumeration != null) {

				param.setValue(((JComboBox<String>) graphicElements.get(ii))
						.getSelectedItem().toString());
			} else {

				String type = param.getBaseType();

				if (type.equals("decimal") || type.equals("integer")
						|| type.equals("int")
						|| type.equals("negativeInteger")
						|| type.equals("nonNegativeInteger")
						|| type.equals("nonPositiveInteger")
						|| type.equals("positiveInteger")
						|| type.equals("float") || type.equals("double")) {

					param.setValue(((JSpinner) graphicElements.get(ii))
							.getValue().toString());
				}

				if (type.equals("string")) {

					if (((JTextField) graphicElements.get(ii)).getText()
							.isEmpty()) {
						errorLabel.setText(eroare1);
						graphicElements.get(ii).requestFocus();
						return;
					}

					if ((p.matcher(((JTextField) graphicElements.get(ii))
							.getText()).find())) {

						errorLabel.setText(eroare2);
						graphicElements.get(ii).requestFocus();
						return;
					}

					param.setValue(((JTextField) graphicElements.get(ii))
							.getText());
				}
			}
			// daca e de tip complex, atunci se ia panelul corespunzator si
			// se parcurg toate componentele din el

			if (param instanceof ComplexTypeParameter) {

				ArrayList<Attribute> attributes = ((ComplexTypeParameter) param)
						.getAttributes();

				for (int j = 0; j < attributes.size(); j++) {

					int coord = ii + j;
					jj++;

					Attribute attr = attributes.get(j);

					JCheckBox elemName = checkboxes.get(jj);

					// daca utilizatorul vrea acel atribut, atunci i se
					// completeaza valoarea
					if (elemName.isSelected()) {

						// caz special - cand parametrul are restrictie de tip enumeration
						if (attr.getRestrictions().enumeration != null) {

							attr.setValue(((JComboBox<String>) graphicElements
									.get(coord)).getSelectedItem()
									.toString());
						} else {

							String type = attr.getBaseType();

							if (type.equals("decimal")
									|| type.equals("integer")
									|| type.equals("int")
									|| type.equals("negativeInteger")
									|| type.equals("nonNegativeInteger")
									|| type.equals("nonPositiveInteger")
									|| type.equals("positiveInteger")
									|| type.equals("float")
									|| type.equals("double")) {

								attr.setValue(((JSpinner) graphicElements
										.get(coord)).getValue().toString());
							}

							if (type.equals("string")) {

								if (((JTextField) graphicElements
										.get(coord)).getText().isEmpty()) {
									errorLabel.setText(eroare1);
									graphicElements.get(coord)
											.requestFocus();
									return;
								}

								if ((p.matcher(((JTextField) graphicElements
										.get(coord)).getText()).find())) {

									errorLabel.setText(eroare2);
									graphicElements.get(coord)
											.requestFocus();
									return;
								}

								attr.setValue(((JTextField) graphicElements
										.get(coord)).getText());
							}
						}
					}
				}

				ii += attributes.size() - 1;
			}
		}

		//Se trimite operatia finala in fereastra de binarizare / preprocesare
		motherWindow.addExec(crtOp);
		close();
	}

	/**
	 * Ne intoarce la fereastra precedenta fara niciun efect(se ignora orice
	 * date introduse de utilizatori)
	 * 
	 * @param evt
	 *            este evenimentul ce reprezinta apasarea cu mosue-ul a
	 *            butonului 'cancel'
	 */
	private void cancelClicked(MouseEvent evt) {
	
		close();
	}

	/**
	 * In functie de obiectul op (care contine mai multe informatii legate de
	 * numarul de parametrii si tipul lor) afisam dinamic butoanele in pagina
	 * 
	 * @param op
	 *            este obiectul de operatii
	 */
	void generateFields(Operation op) {

		params = op.getParameters();
		crtOp = op;

		int totalSize = params.size();

		/*
		 * Pentru fiecare parametru se creeaza un panel continand un label cu
		 * numele parametrului si un element grafic pentru introducerea datelor,
		 * generat in functie de tipul parametrului:
		 * JTextField -> pentru parametri de tip String
		 * JComboBox -> pentru parametri tip enumeratie
		 * JSpinner -> pentru parametri numerici
		 */
		
		for (int ii = 2; ii < op.getParameters().size(); ii++) {

			SimpleTypeParameter param = op.getParameters().get(ii);

			paramsPanel.add(Box.createVerticalStrut(10));
			param1Panel = new JPanel();
			param1Panel.setPreferredSize(new Dimension(320, 30));
			param1Panel.setMinimumSize(new Dimension(320, 30));
			param1Panel.setMaximumSize(new Dimension(320, 30));
			paramsPanel.add(param1Panel);
			param1Panel.setLayout(new BoxLayout(param1Panel, BoxLayout.X_AXIS));

			horizontalStrut_1 = Box.createHorizontalStrut(20);
			horizontalStrut_1.setPreferredSize(new Dimension(10, 0));
			horizontalStrut_1.setMinimumSize(new Dimension(10, 0));
			horizontalStrut_1.setMaximumSize(new Dimension(10, 32767));
			param1Panel.add(horizontalStrut_1);

			elemName = new JLabel(param.getName());
			elemName.setPreferredSize(new Dimension(100, 15));
			elemName.setMinimumSize(new Dimension(100, 15));
			elemName.setMaximumSize(new Dimension(100, 15));
			elemName.setHorizontalAlignment(SwingConstants.RIGHT);
			param1Panel.add(elemName);

			horizontalStrut_2 = Box.createHorizontalStrut(20);
			horizontalStrut_2.setMaximumSize(new Dimension(10, 32767));
			horizontalStrut_2.setMinimumSize(new Dimension(10, 0));
			horizontalStrut_2.setPreferredSize(new Dimension(10, 0));
			param1Panel.add(horizontalStrut_2);

			// in functie de tipul parametrului, genereaza elementul grafic asociat

			// caz special - cand parametrul are restrictie de tip enumeration

			if (param.getRestrictions().enumeration != null) {

				JComboBox<String> elem = new JComboBox<String>(
						new DefaultComboBoxModel<String>(
								param.getRestrictions().enumeration));
				elem.setPreferredSize(new Dimension(150, 25));
				elem.setMinimumSize(new Dimension(150, 25));
				elem.setMaximumSize(new Dimension(150, 25));
				param1Panel.add(elem);

				graphicElements.add(elem);
				combos.add(elem);

			} else {

				String type = param.getBaseType();

				if (type.equals("decimal") || type.equals("float")
						|| type.equals("double")) {

					/*
					 *  daca exista restrictii de tipul minVal / maxVal, atunci
					 *  spinner-ul sa nu permita introducerea acestor valori
					 */
					double minValue = -1000.000;
					double maxValue = 1000.000;

					if (param.getRestrictions().minValue != null) {

						minValue = Double
								.parseDouble(param.getRestrictions().minValue);
					}

					if (param.getRestrictions().maxValue != null) {

						maxValue = Double
								.parseDouble(param.getRestrictions().maxValue);
					}

					SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
							Math.max(minValue, 0), minValue, maxValue, 0.001);

					JSpinner elem = new JSpinner(spinnerModel);
					elem.setPreferredSize(new Dimension(150, 25));
					elem.setMinimumSize(new Dimension(150, 25));
					elem.setMaximumSize(new Dimension(150, 25));
					JComponent comp = elem.getEditor();
					JFormattedTextField field = (JFormattedTextField) comp
							.getComponent(0);
					DefaultFormatter formatter = (DefaultFormatter) field
							.getFormatter();
					formatter.setCommitsOnValidEdit(true);
					param1Panel.add(elem);

					graphicElements.add(elem);
					spinners.add(elem);
				}

				if (type.equals("integer") || type.equals("int")
						|| type.equals("negativeInteger")
						|| type.equals("nonNegativeInteger")
						|| type.equals("nonPositiveInteger")
						|| type.equals("positiveInteger")) {

					int minValue = -1000;
					int maxValue = 1000;

					if (type.equals("negativeInteger")) {

						maxValue = -1;
					}

					if (type.equals("nonNegativeInteger")) {

						minValue = 0;
					}

					if (type.equals("nonPositiveInteger")) {

						maxValue = 0;
					}

					if (type.equals("positiveInteger")) {

						minValue = 1;
					}

					// presupunem ca minValue si maxvalue sunt date corect,
					// tinand cont de restrictia de tip
					if (param.getRestrictions().minValue != null) {

						minValue = Integer
								.parseInt(param.getRestrictions().minValue);
					}

					if (param.getRestrictions().maxValue != null) {

						maxValue = Integer
								.parseInt(param.getRestrictions().maxValue);
					}

					SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
							Math.max(minValue, 0), minValue, maxValue, 1);

					JSpinner elem = new JSpinner(spinnerModel);
					elem.setPreferredSize(new Dimension(150, 25));
					elem.setMinimumSize(new Dimension(150, 25));
					elem.setMaximumSize(new Dimension(150, 25));
					JComponent comp = elem.getEditor();
					JFormattedTextField field = (JFormattedTextField) comp
							.getComponent(0);
					DefaultFormatter formatter = (DefaultFormatter) field
							.getFormatter();
					param1Panel.add(elem);

					graphicElements.add(elem);
					spinners.add(elem);
				}

				if (type.equals("string")) {

					JTextField elem = new JTextField();
					elem.setPreferredSize(new Dimension(150, 25));
					elem.setMinimumSize(new Dimension(150, 25));
					elem.setMaximumSize(new Dimension(150, 25));
					param1Panel.add(elem);
					elem.setColumns(10);

					elem.addCaretListener(new CaretListener() {
						public void caretUpdate(CaretEvent e) {

							errorLabel.setText("");
						}
					});

					graphicElements.add(elem);
					textFields.add(elem);
				}

			}

			// daca e de tip complex, atunci se va crea un panel in care vor fi
			// adaugate si atributele
			if (param instanceof ComplexTypeParameter) {

				createAttributesFields((ComplexTypeParameter) param);
				totalSize += ((ComplexTypeParameter) param).getAttributes()
						.size() + 1;
			}

			horizontalStrut_3 = Box.createHorizontalStrut(20);
			horizontalStrut_3.setPreferredSize(new Dimension(10, 0));
			horizontalStrut_3.setMinimumSize(new Dimension(10, 0));
			horizontalStrut_3.setMaximumSize(new Dimension(10, 32767));
			param1Panel.add(horizontalStrut_3);
		}

		paramsPanel.add(errorLabel);

		// trebuie redimensionata fereastra, dupa nr de parametri = nr de
		// panel-uri care vor trebui sa incapa
		this.setPreferredSize(new Dimension(300, 40 * (totalSize) + 20));

		pack();
	}

	/**
	 * Metoda care creeaza un panel pentru atributele parametrului
	 * @param complexParam parametrul ale carui atribute trebuie afisate in panel
	 */
	public void createAttributesFields(ComplexTypeParameter complexParam) {

		System.out.println("a intrat");

		paramsPanel.add(Box.createVerticalStrut(10));

		attrPanel = new JPanel();
		attrPanel.setBorder(new TitledBorder(null, complexParam.getName()
				+ " attributes", TitledBorder.LEADING, TitledBorder.TOP, null,
				null));
		paramsPanel.add(attrPanel);
		attrPanel.setLayout(new BoxLayout(attrPanel, BoxLayout.Y_AXIS));

		paramsPanel.add(attrPanel);

		/*
		 * Asemanator cu generarea elementelor pentru lista de parametri ai unei operatii;
		 * diferenta este ca aici se parcurge lista de atribute a unui parametru
		 */
		
		for (int ii = 0; ii < complexParam.getAttributes().size(); ii++) {

			Attribute attr = complexParam.getAttributes().get(ii);

			attrPanel.add(Box.createVerticalStrut(10));

			/*
			 *  genereaza un checkbox cu numele atributului (atributul name din
			 *  element corespunzator parametrului)
			 *  CHeckbox-ul va fi initial setat, daca atributul este required
			 */
			param1Panel = new JPanel();
			param1Panel.setPreferredSize(new Dimension(320, 30));
			param1Panel.setMinimumSize(new Dimension(320, 30));
			param1Panel.setMaximumSize(new Dimension(320, 30));
			attrPanel.add(param1Panel);
			param1Panel.setLayout(new BoxLayout(param1Panel, BoxLayout.X_AXIS));

			horizontalStrut_1 = Box.createHorizontalStrut(30);
			param1Panel.add(horizontalStrut_1);

			JCheckBox elemName = new JCheckBox(attr.getName());
			elemName.setPreferredSize(new Dimension(100, 15));
			elemName.setMinimumSize(new Dimension(100, 15));
			elemName.setMaximumSize(new Dimension(100, 15));
			elemName.setHorizontalAlignment(SwingConstants.LEFT);

			checkboxes.add(elemName);

			// daca atributul e required, atunci este obligatoriu sa fie
			// introdusa o valoare acolo -> nu poate fi debifat
			if (attr.getUse().equals("required")) {
				elemName.setSelected(true);
				elemName.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {

						((JCheckBox) e.getSource()).setSelected(true);
					}
				});
			}
			param1Panel.add(elemName);

			horizontalStrut_2 = Box.createHorizontalStrut(10);
			param1Panel.add(horizontalStrut_2);

			if (attr.getRestrictions().enumeration != null) {

				JComboBox<String> elem = new JComboBox<String>(
						new DefaultComboBoxModel<String>(
								attr.getRestrictions().enumeration));
				elem.setPreferredSize(new Dimension(120, 25));
				elem.setMinimumSize(new Dimension(120, 25));
				elem.setMaximumSize(new Dimension(120, 25));
				param1Panel.add(elem);

				graphicElements.add(elem);
				combos.add(elem);

			} else {

				String type = attr.getBaseType();

				if (type.equals("decimal") || type.equals("float")
						|| type.equals("double")) {

					// daca exista restrictii de tipul minVal / maxVal, atunci
					// spinner-ul sa nu permita introducerea acestor valori

					double minValue = -1000.000;
					double maxValue = 1000.000;

					if (attr.getRestrictions().minValue != null) {

						minValue = Double
								.parseDouble(attr.getRestrictions().minValue);
					}

					if (attr.getRestrictions().maxValue != null) {

						maxValue = Double
								.parseDouble(attr.getRestrictions().maxValue);
					}

					SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
							Math.max(minValue, 0), minValue, maxValue, 0.001);

					JSpinner elem = new JSpinner(spinnerModel);
					elem.setPreferredSize(new Dimension(120, 25));
					elem.setMinimumSize(new Dimension(120, 25));
					elem.setMaximumSize(new Dimension(120, 25));
					JComponent comp = elem.getEditor();
					JFormattedTextField field = (JFormattedTextField) comp
							.getComponent(0);
					DefaultFormatter formatter = (DefaultFormatter) field
							.getFormatter();
					formatter.setCommitsOnValidEdit(true);
					param1Panel.add(elem);				

					graphicElements.add(elem);
					spinners.add(elem);
				}

				if (type.equals("integer") || type.equals("int")
						|| type.equals("negativeInteger")
						|| type.equals("nonNegativeInteger")
						|| type.equals("nonPositiveInteger")
						|| type.equals("positiveInteger")) {

					int minValue = -1000;
					int maxValue = 1000;

					if (type.equals("negativeInteger")) {

						maxValue = -1;
					}

					if (type.equals("nonNegativeInteger")) {

						minValue = 0;
					}

					if (type.equals("nonPositiveInteger")) {

						maxValue = 0;
					}

					if (type.equals("positiveInteger")) {

						minValue = 1;
					}

					// presupunem ca minValue si maxvalue sunt date corect,
					// tinand cont de restrictia de tip
					if (attr.getRestrictions().minValue != null) {

						minValue = Integer
								.parseInt(attr.getRestrictions().minValue);
					}

					if (attr.getRestrictions().maxValue != null) {

						maxValue = Integer
								.parseInt(attr.getRestrictions().maxValue);
					}

					SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
							Math.max(minValue, 0), minValue, maxValue, 1);

					JSpinner elem = new JSpinner(spinnerModel);
					elem.setPreferredSize(new Dimension(120, 25));
					elem.setMinimumSize(new Dimension(120, 25));
					elem.setMaximumSize(new Dimension(120, 25));
					JComponent comp = elem.getEditor();
					JFormattedTextField field = (JFormattedTextField) comp
							.getComponent(0);
					DefaultFormatter formatter = (DefaultFormatter) field
							.getFormatter();
					param1Panel.add(elem);

					graphicElements.add(elem);
					spinners.add(elem);
				}

				if (type.equals("string")) {

					JTextField elem = new JTextField();
					elem.setPreferredSize(new Dimension(120, 25));
					elem.setMinimumSize(new Dimension(120, 25));
					elem.setMaximumSize(new Dimension(120, 25));
					param1Panel.add(elem);
					elem.setColumns(10);

					elem.addCaretListener(new CaretListener() {
						public void caretUpdate(CaretEvent e) {

							errorLabel.setText("");
						}
					});

					graphicElements.add(elem);
					textFields.add(elem);
				}

			}

			horizontalStrut_3 = Box.createHorizontalStrut(20);
			horizontalStrut_3.setPreferredSize(new Dimension(10, 0));
			horizontalStrut_3.setMinimumSize(new Dimension(10, 0));
			horizontalStrut_3.setMaximumSize(new Dimension(10, 32767));
			param1Panel.add(horizontalStrut_3);
		}
	}

	/**
	 * Instructiuni efectuate la inchiderea ferestrei.
	 */
	private void close() {

		motherWindow.setEnabled(true);
		errorLabel.setText("");
		dispose();
	}
}