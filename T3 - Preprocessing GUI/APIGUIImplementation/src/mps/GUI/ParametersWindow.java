package mps.GUI;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Liz
 */
import mps.parser.Operation;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.border.EmptyBorder;

import mps.parser.Attribute;
import mps.parser.SimpleTypeParameter;
import mps.parser.ComplexTypeParameter;
import mps.parser.SimpleTypeRestriction;
import java.util.ListIterator;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.text.DefaultFormatter;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

//creez de fiecare data o fereastra noua? cam da
public class ParametersWindow extends javax.swing.JDialog {

	Operation crtOp;
	SecondaryWindow motherWindow;
	ArrayList<SimpleTypeParameter> params;// aici am declarat
	ArrayList<JTextField> textFields;
	ArrayList<JComboBox<String>> combos;
	ArrayList<JSpinner> spinners;
	ArrayList<JCheckBox> checkboxes;
	String eroare1, eroare2;
	/*
	 * aici se vor adauga toate elementele grafice; indexul va corespunde
	 * indexului din params in functie de tipul elementului se va extrage
	 * informatia din elementul grafic - in OK si se va trece ca valoare in
	 * lista de parametri ai operatiei
	 */
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
		generateFields(op); // in functie de numarul de parametrii din op se
							// genereaza dinamic fereastra
	}

	/**
	 * metoda generata automat de java swing pentru initializare
	 */
	private void initComponents() {
		setLocationRelativeTo(null); // pozitionarea ferestrei in centrul
										// ecranului

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				
				motherWindow.setEnabled(false);
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

				// cand se inchide fereastra de la "X", nu se salveaza
				// modificarile
				
				close();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// mainWindow.setEnabled(false);
			}
		});

		/*
		addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {

				motherWindow.setEnabled(true);
				close();
			}

			public void componentShown(ComponentEvent e) {
				// mainWindow.setEnabled(false);
			}
		});
		*/

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
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Aceasta functie seteaza mesajele erorilor, culoarea si fontul lor si le
	 * seteaza sa nu fie afisate
	 */
	private void initializareErori() {
		eroare1 = "Trebuie sa completati toate campurile goale";
		eroare2 = "Introduceti doar date valide(litere)";
	}

	/**
	 * aceasta metoda specifica ce se intampla la apasarea butonului OK. Se
	 * preiau datele introduse in TextField, ComboBox si Spinner (doar daca au
	 * fost introduse corect, altfel se afisaza un mesaj de eroare) si se
	 * transmit catre fereastra secundara(anterioara)
	 * 
	 * @param evt
	 *            este evenimentu ce reprezinta click-ul pe OK
	 */
	private void okClicked(java.awt.event.MouseEvent evt) {

		int k, okay1 = 1, okay2 = 1;
		Pattern p = Pattern.compile("[0-9]");
		// analizez fiecare "casuta" in care se pot introduce parametri
		for (k = 0; k < textFields.size(); k++) {
			// daca am gasit un camp necompletat marchez eroarea
			// if (textFields.get(k).getText().equals("")) {
			// okay1 = 0;
			// }
			// daca utilizatorul a introdus caractere nepermise, marchez si a
			// doua eroare
			// if (p.matcher(textFields.get(k).getText()).find()) {
			// okay2 = 0;
			// }
		}
		if (okay1 == 0) {
			errorLabel.setText(eroare1);
		} else if (okay2 == 0) {
			errorLabel.setText(eroare2);
		} // daca nu sunt erori(datele au fost introduse corect)
		else {
			// intorc referinta crtOp

			// numar elementele in total ; e indice pt graphicElements
			int ii = -1;
			// numar doar atributele; e indice pt checkboxes
			int jj = -1;

			// completez parametrii
			for (int i = 2; i < params.size(); i++) {

				SimpleTypeParameter param = params.get(i);
				ii++;

				// caz special - cand parametrul are restrictie de tip
				// enumeration
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

							// caz special - cand parametrul are restrictie de
							// tip
							// enumeration
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

			motherWindow.addExec(crtOp);
			// pun erorile pe fals, deoarece daca se mai intra o data in aceasta
			// fereastra, ele nu trebuie afisate
			close();
		}
	}

	/**
	 * ne intoarce la fereastra precedenta fara niciun efect(se ignora orice
	 * date introduse de utilizatori)
	 * 
	 * @param evt
	 *            este evenimentul ce reprezinta apasarea cu mosue-ul a
	 *            butonului 'cancel'
	 */
	private void cancelClicked(MouseEvent evt) {

		/*
		 * int result = JOptionPane.showConfirmDialog( this,
		 * "Are you sure you want to Cancel? All your operations will be lost!",
		 * "Canceling...", JOptionPane.YES_NO_OPTION); if (result ==
		 * JOptionPane.YES_OPTION){ dispose(); }
		 */

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

		for (int ii = 2; ii < op.getParameters().size(); ii++) {

			SimpleTypeParameter param = op.getParameters().get(ii);

			paramsPanel.add(Box.createVerticalStrut(10));
			// genereaza un label cu numele parametrului (atributul name din
			// element corespunzator parametrului)
			param1Panel = new JPanel();
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

			// in functie de tipul parametrului, genereaza elementul grafic
			// asociat
			// (am putea direct sa stabilim o corespondenta intre
			// elementele grafice si sa stabilim de la inceput ce generam,
			// nu de fiecare data cand avem fereastra de parametri sa stam sa
			// analizam)

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

					// daca exista restrictii de tipul minVal / maxVal, atunci
					// spinner-ul sa nu permita introducerea acestor valori

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

					elem.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {

							doubleSpinnerListener(e);
						}
					});

					/*
					 * elem.addInputMethodListener(new InputMethodListener() {
					 * public void caretPositionChanged(InputMethodEvent event)
					 * { } public void inputMethodTextChanged(InputMethodEvent
					 * event) {
					 * 
					 * doubleSpinnerListener(event); } });
					 */

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
					// formatter.setCommitsOnValidEdit(true);
					param1Panel.add(elem);

					elem.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {

							integerSpinnerListener(e);
						}
					});

					/*
					 * elem.addInputMethodListener(new InputMethodListener() {
					 * public void caretPositionChanged(InputMethodEvent event)
					 * { } public void inputMethodTextChanged(InputMethodEvent
					 * event) {
					 * 
					 * integerSpinnerListener(event); } });
					 */

					graphicElements.add(elem);
					spinners.add(elem);
				}

				/*
				 * if (type.equals("float") || type.equals("double")) {
				 * 
				 * SpinnerNumberModel spinnerModel = new
				 * SpinnerNumberModel(0.0000, -1000.0000, 1000.0000, 0.0001);
				 * 
				 * JSpinner elem = new JSpinner(spinnerModel);
				 * //elem.setEditor(new JSpinner.NumberEditor(elem,"#00.00"));
				 * elem.setBounds(70, ySmallPanel, 140, 50); this.add(elem,
				 * ySmallPanel, 1); graphicElements.add(elem);
				 * spinner.add(elem); }
				 */

				if (type.equals("string")) {

					// ar trebui sa fac mai mare text box-ul, ca sa incapa orice
					// sir

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

		for (int ii = 0; ii < complexParam.getAttributes().size(); ii++) {

			Attribute attr = complexParam.getAttributes().get(ii);

			attrPanel.add(Box.createVerticalStrut(10));

			// genereaza un label cu numele parametrului (atributul name din
			// element corespunzator parametrului)
			param1Panel = new JPanel();
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

			// in functie de tipul parametrului, genereaza elementul grafic
			// asociat
			// (am putea direct sa stabilim o corespondenta intre
			// elementele grafice si sa stabilim de la inceput ce generam,
			// nu de fiecare data cand avem fereastra de parametri sa stam sa
			// analizam)

			// caz special - cand parametrul are restrictie de tip enumeration

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

					elem.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {

							doubleSpinnerListener(e);
						}
					});

					/*
					 * elem.addInputMethodListener(new InputMethodListener() {
					 * public void caretPositionChanged(InputMethodEvent event)
					 * { } public void inputMethodTextChanged(InputMethodEvent
					 * event) {
					 * 
					 * doubleSpinnerListener(event); } });
					 */

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
					// formatter.setCommitsOnValidEdit(true);
					param1Panel.add(elem);

					elem.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {

							integerSpinnerListener(e);
						}
					});

					/*
					 * elem.addInputMethodListener(new InputMethodListener() {
					 * public void caretPositionChanged(InputMethodEvent event)
					 * { } public void inputMethodTextChanged(InputMethodEvent
					 * event) {
					 * 
					 * integerSpinnerListener(event); } });
					 */

					graphicElements.add(elem);
					spinners.add(elem);
				}

				/*
				 * if (type.equals("float") || type.equals("double")) {
				 * 
				 * SpinnerNumberModel spinnerModel = new
				 * SpinnerNumberModel(0.0000, -1000.0000, 1000.0000, 0.0001);
				 * 
				 * JSpinner elem = new JSpinner(spinnerModel);
				 * //elem.setEditor(new JSpinner.NumberEditor(elem,"#00.00"));
				 * elem.setBounds(70, ySmallPanel, 140, 50); this.add(elem,
				 * ySmallPanel, 1); graphicElements.add(elem);
				 * spinner.add(elem); }
				 */

				if (type.equals("string")) {

					// ar trebui sa fac mai mare text box-ul, ca sa incapa orice
					// sir

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

	private void close() {

		motherWindow.setEnabled(true);
		errorLabel.setText("");
		dispose();
	}

	public void doubleSpinnerListener(ChangeEvent e) {

		// identific parametrul coresp.
		JSpinner source = (JSpinner) e.getSource();
		int index = graphicElements.indexOf(source);
		SimpleTypeParameter param = params.get(index);

		double minValue = -1000.000;
		double maxValue = 1000.000;

		JComponent comp = source.getEditor();
		JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);

		if (param.getRestrictions().minValue != null) {

			minValue = Double.parseDouble(param.getRestrictions().minValue);
		}

		if (param.getRestrictions().maxValue != null) {

			maxValue = Double.parseDouble(param.getRestrictions().maxValue);
		}

		try {
			field.commitEdit();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		double input = (Double) field.getValue();

		if (input > maxValue || input < minValue) {

			// afiseaza eroare
			// revine la valoarea anterioara
			// afiseaza eroare
			errorLabel.setText("Ati depasit limitele valorilor!");
			// revine la valoarea anterioara
			source.setValue(source.getModel().getValue());
		} else {

			// totul e ok
		}
	}

	public void integerSpinnerListener(ChangeEvent e) {

		// identific parametrul coresp.
		JSpinner source = (JSpinner) e.getSource();
		int index = graphicElements.indexOf(source);
		SimpleTypeParameter param = params.get(index);

		Integer minValue = -1000;
		Integer maxValue = 1000;

		if (param.getRestrictions().minValue != null) {

			minValue = Integer.valueOf(param.getRestrictions().minValue);
		}

		if (param.getRestrictions().maxValue != null) {

			maxValue = Integer.valueOf(param.getRestrictions().maxValue);
		}

		if ((Integer) source.getValue() > maxValue
				|| (Integer) source.getValue() < minValue) {

			// afiseaza eroare
			errorLabel.setText("Ati depasit limitele valorilor!");
			// revine la valoarea anterioara
			source.setValue(source.getModel().getValue());
		} else {
			// totul e ok
		}
	}
}