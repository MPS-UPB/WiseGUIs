package mps.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mps.parser.ComplexTypeParameter;
import mps.parser.Operation;
import mps.parser.Parser;

/**
 * Clasa care descrie fereastra principala si modulul de control al aplicatiei.
 * @author Liliana
 * @version Last modified by Liliana 20/12/2012
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel basePanel;
	private JButton binarizationButton;
	private JButton browseButton;
	private JButton compareButton;
	private JPanel imagePanel;
	private JScrollPane imageScrollPane;
	private JPanel leftPanel;
	private JPanel newImgPanel;
	private JTextField pathTextField;
	private JButton preprocessingButton;
	private JPanel rightPanel;
	private JButton updateButton;
	private JCheckBox updateCheckBox;
	/**
	 * Fereastra de alegere a executabilelor de binarizare.
	 */
	private BinarizationWindow binarizationWindow;
	/**
	 * Fereastra de alegere a executabilelor de preprocesare.
	 */
	private PreprocessingWindow preprocessingWindow;
	/**
	 * Fereastra de comparare imagini.
	 */
	private CompareImagesWindow compareWindow;
	/**
	 * Tipurile de executabile: rotate, crop, otsu etc.
	 */
	List<Operation> execTypes;
	/**
	 * Vector cu operatiile care trebuie facute pe imagine
	 */
	List<Operation> operations;

	/**
	 * Calea imaginii care va fi folosita la executie
	 */
	String pathImage;

	/**
	 * Lista perechilor checkBox - image
	 */
	Hashtable<JCheckBox, String> imageList;

	/*
	 * Lista perechilor checkBox - Label Pentru update
	 */
	Hashtable<JCheckBox, JLabel> labelList;

	/**
	 * Vector cu checkboxurile din lista din dreapta care sunt bifate
	 */
	ArrayList<JCheckBox> checkedImages;

	/*
	 * Lista cu toate checkboxurile din lista din dreapta
	 */
	ArrayList<JCheckBox> allImages;

	/*
	 * Lista cu toate containerele din partea dreapta Folosit la remove
	 */
	ArrayList<JPanel> containerList;

	// input path
	String inputPath;

	// Campuri pentru verificarea erorilor la citirea din fisierul din
	// configurare
	public static String XSD_ERROR = "Error in reading the config.txt file";
	public static String XSD_NOPATH = "No path found in the config.txt file";

	public static String xsdPath = "";
	public static String execPath = "";

	public MainWindow() {

		super("Preprocessing GUI - Main Window");

		// execTypes = new ArrayList<Operation>();
		imageList = new Hashtable<JCheckBox, String>();
		labelList = new Hashtable<JCheckBox, JLabel>();
		operations = new ArrayList<Operation>();
		checkedImages = new ArrayList<JCheckBox>();
		allImages = new ArrayList<JCheckBox>();
		containerList = new ArrayList<JPanel>();

		binarizationWindow = new BinarizationWindow(this);
		preprocessingWindow = new PreprocessingWindow(this);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));

		initComponents();

		setLocationRelativeTo(null);
		rightPanel.setMinimumSize(new Dimension(this.getWidth() / 2, 30));
		rightPanel.setPreferredSize(new Dimension(this.getWidth() / 2, 30));

		init();
	}

	private void initComponents() {

		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		rightPanel = new JPanel();
		rightPanel.setMinimumSize(new Dimension(this.getWidth() / 2, 30));
		rightPanel.setPreferredSize(new Dimension(this.getWidth() / 2, 30));
		splitPane.setLeftComponent(rightPanel);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		Component verticalStrut = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut);

		JPanel filePanel = new JPanel();
		// filePanel.setPreferredSize(new Dimension(250, 30));
		// filePanel.setMinimumSize(new Dimension(250, 30));
		filePanel.setMaximumSize(new Dimension(32767, 30));
		rightPanel.add(filePanel);
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));

		Component horizontalStrut = Box.createHorizontalStrut(10);
		filePanel.add(horizontalStrut);

		browseButton = new JButton("Browse");
		browseButton.setPreferredSize(new Dimension(70, 25));
		browseButton.setMinimumSize(new Dimension(70, 25));
		browseButton.setMaximumSize(new Dimension(70, 25));
		filePanel.add(browseButton);

		Component horizontalStrut_2 = Box.createHorizontalStrut(10);
		filePanel.add(horizontalStrut_2);

		pathTextField = new JTextField();
		pathTextField.setPreferredSize(new Dimension(200, 25));
		pathTextField.setMinimumSize(new Dimension(150, 25));
		pathTextField.setMaximumSize(new Dimension(250, 25));
		pathTextField.setEditable(false);
		filePanel.add(pathTextField);

		Component verticalStrut_1 = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut_1);

		JScrollPane scrollPane = new JScrollPane();
		rightPanel.add(scrollPane);

		final Component verticalStrut_7 = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut_7);

		JPanel buttonsPanel = new JPanel();
		// buttonsPanel.setPreferredSize(new Dimension(250, 30));
		// buttonsPanel.setMinimumSize(new Dimension(250, 30));
		buttonsPanel.setMaximumSize(new Dimension(32767, 30));
		rightPanel.add(buttonsPanel);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		buttonsPanel.add(horizontalStrut_1);

		preprocessingButton = new JButton("Preprocessing");
		preprocessingButton.setPreferredSize(new Dimension(120, 25));
		preprocessingButton.setMinimumSize(new Dimension(120, 25));
		preprocessingButton.setMaximumSize(new Dimension(120, 25));
		preprocessingButton.setEnabled(false);
		buttonsPanel.add(preprocessingButton);

		Component horizontalGlue = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue);

		binarizationButton = new JButton("Binarization");
		binarizationButton.setMinimumSize(new Dimension(120, 25));
		binarizationButton.setMaximumSize(new Dimension(120, 25));
		binarizationButton.setPreferredSize(new Dimension(120, 25));
		binarizationButton.setEnabled(false);
		buttonsPanel.add(binarizationButton);

		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		buttonsPanel.add(horizontalStrut_3);

		Component verticalStrut_3 = Box.createVerticalStrut(10);
		rightPanel.add(verticalStrut_3);

		leftPanel = new JPanel();
		splitPane.setRightComponent(leftPanel);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		Component verticalStrut_2 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_2);

		JPanel updatePanel = new JPanel();
		// buttonsPanel.setPreferredSize(new Dimension(250, 30));
		// buttonsPanel.setMinimumSize(new Dimension(250, 30));
		updatePanel.setMaximumSize(new Dimension(32767, 30));
		leftPanel.add(updatePanel);
		updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.X_AXIS));

		Component horizontalStrut_4 = Box.createHorizontalStrut(10);
		updatePanel.add(horizontalStrut_4);

		updateCheckBox = new JCheckBox();
		updateCheckBox.setText("Immediate Update ");
		updateCheckBox.setActionCommand("Immediate Update ");
		updatePanel.add(updateCheckBox);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		updatePanel.add(horizontalGlue_1);

		updateButton = new JButton("Update");
		updateButton.setMaximumSize(new Dimension(80, 25));
		updateButton.setMinimumSize(new Dimension(80, 25));
		updateButton.setPreferredSize(new Dimension(80, 25));
		updateButton.setEnabled(false);
		updatePanel.add(updateButton);

		Component horizontalStrut_5 = Box.createHorizontalStrut(10);
		updatePanel.add(horizontalStrut_5);

		Component verticalStrut_4 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_4);

		imageScrollPane = new JScrollPane();
		imageScrollPane.setBorder(null);
		leftPanel.add(imageScrollPane);

		Component verticalStrut_5 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_5);

		compareButton = new JButton("Compare");
		compareButton.setMaximumSize(new Dimension(80, 25));
		compareButton.setMinimumSize(new Dimension(80, 25));
		compareButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		compareButton.setPreferredSize(new Dimension(80, 25));
		compareButton.setEnabled(false);
		leftPanel.add(compareButton);

		Component verticalStrut_6 = Box.createVerticalStrut(10);
		leftPanel.add(verticalStrut_6);

		imagePanel = new JPanel();
		scrollPane.setViewportView(imagePanel);
		newImgPanel = new JPanel();
		checkedImages = new ArrayList<JCheckBox>();

		compareButton.setEnabled(false);
		binarizationButton.setEnabled(false);
		preprocessingButton.setEnabled(false);
		updateButton.setEnabled(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(820, 550));
		setMinimumSize(new Dimension(820, 550));

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {

				formComponentResized(evt);
			}

			private void formComponentResized(ComponentEvent evt) {
			}
		});

		final JFileChooser fileChooser = new JFileChooser("poze de test");
		pathTextField.setEditable(false);
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						int returnVal = fileChooser.showOpenDialog(leftPanel);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							// facem enabled butoanele
							compareButton.setEnabled(true);
							binarizationButton.setEnabled(true);
							preprocessingButton.setEnabled(true);
							updateButton.setEnabled(true);

							// Setam calea catre fisier in textBox
							pathTextField.setText(fileChooser.getSelectedFile()
									.getAbsolutePath());
							// salvam calea catre imagine
							pathImage = pathTextField.getText();
							// Incarcam imaginea in panel

							// construieste imagine din calea "pathImage" cu
							// dimensiunile width si height
							Image image = ImageViewer.buildImage(pathImage);

							// facem imaginea greyscale in spate
							BufferedImage image_new = null;
							File outputBlack = null;
							try {
								image_new = ImageIO.read(new File(pathTextField
										.getText()));
								BufferedImage image2 = new BufferedImage(
										image_new.getWidth(), image_new
												.getHeight(),
										BufferedImage.TYPE_BYTE_GRAY);
								Graphics g = image2.getGraphics();
								g.drawImage(image, 0, 0, null);
								g.dispose();

								// Scriem noua imagine greyscale pe disc
								String delim = "\\.";
								String[] tokens = pathTextField.getText()
										.split(delim);
								inputPath = tokens[0] + "_input.png";
								outputBlack = new File(inputPath);
								outputBlack.createNewFile();
								ImageIO.write(image2, "png", outputBlack);
							} catch (Exception e) {
							}

							// Setam imaginea originala in stanga (pana la o
							// preprocesare)
							ImageIcon myPicture = new ImageIcon(image);
							JLabel picLabel = new JLabel(myPicture);
							picLabel.setSize(new Dimension(myPicture
									.getIconWidth(), myPicture.getIconHeight()));
							imagePanel.removeAll();
							imagePanel.add(picLabel);
							MainWindow.this.repaint();
						}
					}
				});
			}
		});

		// facem disable butonul de update daca checkboxul este true
		updateCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (updateCheckBox.isSelected()) {
					updateButton.setEnabled(false);
				} else {
					updateButton.setEnabled(true);
				}

			}
		});

		/*
		 * La apasarea butonului Update: - se verifica daca exista cel putin un
		 * checkbox selectat in dreapta - se schimba imaginea de input al
		 * operatiilor corespunzatoare - se executa din nou operatia de
		 * binarizare cu noua imagine de input dar cu aceiasi parametri ca la
		 * inceput. - se updateaza Label-ul cu noua imagine.
		 */
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				/*
				 * Daca exista cel putin un checkbox selectat caut numarul de
				 * ordine al fiecarui checkbox selectat, astfel incat sa pot
				 * modifica operatia corespunzatoare din vectorul "operations"
				 */

				if (checkedImages.size() > 0) {

					for (int i = 0; i < checkedImages.size(); i++) {
						JCheckBox checked = checkedImages.get(i);
						for (int j = 0; j < allImages.size(); j++) {
							JCheckBox item = allImages.get(j);
							// numarul de ordine este j
							if (checked == item) {
								// modific operatia
								((ComplexTypeParameter) operations.get(j)
										.getParameter("inputFile"))
										.setAttribute("name", inputPath);
								// execut operatia pentru a obtine imaginea de
								// output
								operations.get(j).execute();

								// construiesc noua imagine din calea obtinuta
								String path = ((ComplexTypeParameter) operations
										.get(j).getParameter("outputFile"))
										.getAttribute("name").getValue();

								int width = 450;
								int height = 250;

								// Inlocuiesc Label-ul din dreapta
								JLabel label = labelList.get(checked);

								Image image = ImageViewer.buildImage(path);
								image = ImageViewer.resize(image,
										label.getWidth(), label.getHeight());
								ImageIcon myPicture = new ImageIcon(image);

								label.setIcon(myPicture);
							
								// fac refresh la JScrollPane
								imageScrollPane.revalidate();
								imageScrollPane.repaint();

								// inlocuiesc calea in imageList
								imageList.put(checked, path);
							}
						}
					}
				}
			}
		});

		imagePanel
				.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		imagePanel.setForeground(new Color(255, 255, 255));

		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
	
		binarizationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						binarizationWindow.setVisible(true);

					}
				});

			}
		});

		preprocessingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						preprocessingWindow.setVisible(true);
					}
				});

			}
		});

		binarizationButton.getAccessibleContext().setAccessibleName("");

		imageScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		imageScrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	
		newImgPanel.setLayout(new BoxLayout(newImgPanel, BoxLayout.Y_AXIS));

		imageScrollPane.setViewportView(newImgPanel);

		updateCheckBox.setText("Immediate Update ");
		updateCheckBox.setActionCommand("Immediate Update ");

		compareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				
				if (checkedImages.size() == 2) {
					try {
						compareWindow = new CompareImagesWindow(imageList
								.get(checkedImages.get(0)), imageList
								.get(checkedImages.get(1)));
						compareWindow.setVisible(true);
					}
				
					catch (IOException ex) {
						Logger.getLogger(MainWindow.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}

			}
		});

	}

	/**
	 * Metoda in care se initializeaza alti parametri din fereastra principala
	 * si care incepe desfasurarea proceselor
	 */
	private void init() {

		/*
		 * Citire fisier de configurare
		 * 
		 * Daca apar erori la citire, atunci utilizatorul va fi informat
		 * printr-un Message Dialog si metoda va face return, fara alte operatii
		 * (practic trebuie repornita aplicatia)
		 * 
		 * sau putem face o facilitate: un dialog window, in care intrebam daca
		 * se doreste iar citirea fisierului; daca da, atunci se reapeleaza
		 * getPath()
		 */

		String res1 = getPath("XSD:");
		String res2 = getPath("EXEC:");

		if (res1.equals(XSD_ERROR) || res2.equals(XSD_ERROR)) {

			JOptionPane.showMessageDialog(this, XSD_ERROR, this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		if (res1.equals(XSD_NOPATH) || res2.equals(XSD_NOPATH)) {

			JOptionPane.showMessageDialog(this, XSD_NOPATH, this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

                /*
                 * Construire folder XMLs daca el nu exista
                 */
                File f = new File("XMLs");
                
                if(!f.exists())
                {
                    if(f.mkdir());                    
                }
                
		/*
		 * Rulare parser
		 * 
		 * Parserul citeste toate fisierele xsd dintr-un folder specificat (hard
		 * coded) Intoarce un set de Operatii abstracte - execTypes
		 */
		execTypes = Parser.getExecTypes();

		/*
		 * Pasare lista executabile de binarizare/preprocesare catre ferestrele
		 * aferente; Aici fac deosebirea tipurilor generale de executabile:
		 * preprocesare / binarizare
		 */
		for (Operation execType : execTypes) {
			// tip preprocesare
			if (execType.getType() == 0) {
				preprocessingWindow.addListElement(execType);
			} // tip binarizare
			else if (execType.getType() == 1) {
				binarizationWindow.addListElement(execType);
			}
		}

		// Se afiseaza fereastra principala (se da drumul aplicatiei)
		this.setVisible(true);
	}

	/*
	 * Metoda care deschide fisierul de configurare si citeste de acolo calea
	 * catre folderul cu XSD-uri Se deschide fisierul "config.txt" si se citeste
	 * linia care are ca header stringul "ofWhat" Stringul "ofWhat" poate fi
	 * "XSD:" pentru a intoarce calea catre XSD-uri sau "EXEC:" pentru a
	 * intoarce calea catre executabile Daca fisierul nu se deschide/nu este
	 * gasit , metoda intoarce un string eroare MainWindow.XSD_ERROR Daca nici o
	 * cale nu se gaseste in fisierul config.txt , metoda intoarce un string
	 * eroare MainWindow.XSD_NOPATH
	 */

	public String getPath(String ofWhat) {
		String path = "";
		try {
			FileInputStream file = new FileInputStream("config.txt");
			DataInputStream in = new DataInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains(ofWhat)) {
					path = (strLine.substring(ofWhat.length())).trim();

					if (ofWhat.equals("XSD:")) {

						xsdPath = path;
					}

					if (ofWhat.equals("EXEC:")) {

						execPath = path;
					}

					break;
				}
			}
			if (path.equals("")) {
				path = MainWindow.XSD_NOPATH;
			}
		} catch (IOException e) {
			path = MainWindow.XSD_ERROR;
		}
		return path;
	}

	/**
	 * Metoda care primeste un set de operatii de preprocesare si le aplica pe
	 * imaginea originala.
	 * 
	 * @param preprocOperations
	 *            operatiile de preprocesare de executat
	 */
	public void launchPreprocOperations(List<Operation> preprocOperations) {
		
		for (int i = 0; i < preprocOperations.size(); i++) {
			execTypes.add(preprocOperations.get(i));

			// Fisierul de input este pus in lista de parametri;

			((ComplexTypeParameter) preprocOperations.get(i).getParameter(
					"inputFile")).setAttribute("name", inputPath);

			executePreprocesing(preprocOperations.get(i));

			if (updateCheckBox.isSelected()) {
				for (int j = 0; j < operations.size(); j++) {
					// se face update cu noua valoare (cheia exista)

					((ComplexTypeParameter) operations.get(j).getParameter(
							"inputFile")).setAttribute("name", inputPath);

					// Executam operatia ca sa obtinem outputFile
					operations.get(j).execute();

					// construiesc noua imagine din calea obtinuta

					String path = ((ComplexTypeParameter) operations.get(j)
							.getParameter("outputFile")).getAttribute("name")
							.getValue();

					int width = imageScrollPane.getSize().width - 70;
					int height = 150;
					ImageIcon myPicture = null;
					Image image = ImageViewer.buildImage(path);

					// Inlocuiesc Label-ul din dreapta
					JLabel label = null;
					// Checkboxul j din vectorul allImages corespunde operatiei
					// j din operations
					JCheckBox item = allImages.get(j);

					label = labelList.get(item);
					image = ImageViewer.resize(image, label.getWidth(),
							label.getHeight());
					myPicture = new ImageIcon(image);

					label.setIcon(myPicture);

					// inlocuiesc calea in imageList
					imageList.put(item, path);

				}
				// fac refresh la JScrollPane
				imageScrollPane.revalidate();
				imageScrollPane.repaint();
			}
		}
	}

	/**
	 * Metoda care primeste un set de operatii de binarizare si le aplica pe
	 * imaginea orginara. Operatiile sunt noi, fiind adaugate in vectorul
	 * operations Nu e nevoie de verificare de dubluri, operatiile care vin vor
	 * fi unice
	 * 
	 * @param binarizOperations
	 *            operatiile de binarizare de executat
	 */
	public void launchBinarizOperations(List<Operation> binarizOperations) {

		/*
		 * Pentru fiecare operatie de binarizare se completeaza imaginea de
		 * input si se executa operatia Dupa executie, operatia se salveaza in
		 * vectorul operations
		 */
		for (int i = 0; i < binarizOperations.size(); i++) {
			execTypes.add(binarizOperations.get(i));

			((ComplexTypeParameter) binarizOperations.get(i).getParameter(
					"inputFile")).setAttribute("name", inputPath);
			executeBinarization(binarizOperations.get(i));
			operations.add(binarizOperations.get(i));
		}
	}

	/*
	 * Metoda primeste o operatie de BINARIZARE Se sterge din vectorul de
	 * operatii Se sterge din lista din dreapta
	 */
	public void removeBinarization(Operation op) {
		for (int i = 0; i < operations.size(); i++) {
			if (operations.get(i) == op) {
				JCheckBox deleted = allImages.get(i);

				// stergem din hashtable-uri
				labelList.remove(deleted);
				imageList.remove(deleted);			

				// stergem din panelul mare din dreapta
				// stergem checkbox-ul
				newImgPanel.remove(i * 2);
				//stergem label-ul
				newImgPanel.remove(i * 2);

				// Verific daca trebuie sters si in vectorul de checkedImages
				for (int j = 0; j < checkedImages.size(); j++) {
					if (checkedImages.get(j) == deleted) {
						checkedImages.remove(j);
					}
				}

				// Il sterg intr-un final si din vectorul ce contine toate
				// checkboxurile
				allImages.remove(i);

				// refresh la scrollPane
				imageScrollPane.revalidate();
				imageScrollPane.repaint();

				// si din operations
				operations.remove(i);
				
				break;
			}
		}
	}

	/**
	 * Metoda care primeste o operatie de preprocesare, o executa si updateaza
	 * panelul cu imaginea din stanga
	 * 
	 * @param oper
	 *            operatia de preprocesare de executat
	 */
	private void executePreprocesing(Operation oper) {
		// Setam calea catre fisier in textBox
		oper.execute();

		String path = ((ComplexTypeParameter) oper.getParameter("outputFile"))
				.getAttribute("name").getValue();
		inputPath = path;

		// salvam calea catre imagine
		pathImage = pathTextField.getText();

		// Incarcam imaginea in panel
		int width = imagePanel.getSize().width;
		int height = imagePanel.getSize().height;

		Image image = ImageViewer.buildImage(inputPath);
		ImageIcon myPicture = new ImageIcon(image);

		JLabel lab = (JLabel) imagePanel.getComponent(0);
		lab.setSize(new Dimension(myPicture.getIconWidth(), myPicture
				.getIconHeight()));
		lab.setIcon(myPicture);

		MainWindow.this.repaint();
	}

	/**
	 * Metoda care primeste o operatie de binarizare, o executa si adauga un
	 * checkbox cu poza in lista din dreapta
	 * 
	 * @param oper
	 *            operatia de binarizare de executat
	 */
	private void executeBinarization(Operation oper) {
		/*
		 * Executam operatia (generare de XML + rulare executabil) In urma
		 * executiei, operatia are completata calea catre fisierul de output
		 * ("outputFile")
		 */
		oper.execute();

		// Se construieste checkboxul cu fotografie
		final JCheckBox box = new JCheckBox();
		String path = ((ComplexTypeParameter) oper.getParameter("outputFile"))
				.getAttribute("name").getValue();
		int width = imageScrollPane.getSize().width - 70;
		int height = 150;

		Image image = ImageViewer.buildImage(path);
		ImageIcon myPicture = new ImageIcon(image);

		JLabel label = new JLabel(myPicture);
		int size = imageList.size();
		label.setAlignmentX(LEFT_ALIGNMENT);
		label.setIcon(myPicture);
		label.setPreferredSize(new Dimension(myPicture.getIconWidth(),
				myPicture.getIconHeight()));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box.setPreferredSize(new Dimension(20, 30));

		/*
		 * Ascultator pentru checkbox Daca se bifeaza aces checkbox si deja sunt
		 * bifate alte 2 checkboxuri se inlocuieste primul bifat cu acesta Daca
		 * se debifeaza acest checkbox, atunci el se elimina din vectorul
		 * checkboxurilor selectate
		 */
		box.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						if (box.isSelected()) {
							if (checkedImages.size() == 2) {
								JCheckBox jcb = checkedImages.get(0);
								jcb.setSelected(false);
								checkedImages.remove(0);
							}
							checkedImages.add(box);
						} else {
							checkedImages.remove(box);
						}
					}
				});

			}
		});

		// Salvez checkboxul
		allImages.add(box);

		// Construiesc un panel care va cuprinde checkboxul si Label-ul cu
		// imaginea
		JPanel containerPanel = new JPanel();

		containerPanel.setMaximumSize(new Dimension(32767, 32767));
		containerPanel.setMinimumSize(new Dimension(100 + myPicture
				.getIconWidth(), myPicture.getIconHeight()));
		containerPanel.setPreferredSize(new Dimension(100 + myPicture
				.getIconWidth(), myPicture.getIconHeight()));
		containerPanel.setSize(100 + myPicture.getIconWidth(),
				myPicture.getIconHeight());
		containerPanel.setLocation(1, size * 180 + 1);
		containerPanel.add(box);
		containerPanel.add(label);

		label.setToolTipText(oper.toString2());
	
		// Se salveaza containerul
		containerList.add(containerPanel);

		// Se salveaza perechea checkbox-cale imagine intr-un Hastable
		imageList.put(box, path);
		// Se salveaza perechea checkbox-label intr-un Hashtable , pentru update
		labelList.put(box, label);

		// Se adauga panelul nou creat in lista din dreapta
		newImgPanel.add(containerPanel);
		newImgPanel.add(Box.createVerticalStrut(10));
		imageScrollPane.revalidate();
		imageScrollPane.repaint();
	}

	public static void main(String args[]) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				MainWindow mainWindow = new MainWindow();
			}
		});
	}

}
