package mps.GUI.window.implementation;

import mps.parser.implementation.Operation;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import mps.parser.implementation.Parser;

/**
 *
 * @author Liliana
 * @version Last modified by Roxana 11/15/2012
 */
public class MainWindow extends JFrame{

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
     *  Vector cu operatiile care trebuie facute pe imagine
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
    
    public MainWindow() {

        super("Preprocesing GUI - Main Window");

        // execTypes = new ArrayList<Operation>();
        imageList = new Hashtable<JCheckBox, String>() ;
        operations = new ArrayList<Operation>();

        initComponents();

        binarizationWindow = new BinarizationWindow(this);
        preprocessingWindow = new PreprocessingWindow(this);
        setLocationRelativeTo(null);
        init();
    }

    private void initComponents() {

        basePanel = new JPanel();
        leftPanel = new JPanel();
        browseButton = new JButton("Browse");
        pathTextField = new JTextField();
        imagePanel = new JPanel();
        binarizationButton = new JButton("Binarization");
        preprocessingButton = new JButton("Preprocessing");
        rightPanel = new JPanel();
        imageScrollPane = new JScrollPane();
        newImgPanel = new JPanel();
        updateCheckBox = new JCheckBox();
        updateButton = new JButton("Update");
        compareButton = new JButton("Compare");
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(620, 550));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                formComponentResized(evt);
            }
        });


        final JFileChooser fileChooser = new JFileChooser();
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        int returnVal = fileChooser.showOpenDialog(leftPanel);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {

                            // Setam calea catre fisier in textBox
                            pathTextField.setText(fileChooser.getSelectedFile()
                                    .getAbsolutePath());

                            // salvam calea catre imagine
                            pathImage = pathTextField.getText();
                            
                            // Incarcam imaginea in panel
                            int width = imagePanel.getSize().width;
                            int height = imagePanel.getSize().height;

                            ImageIcon myPicture = new ImageIcon(new ImageIcon(pathTextField.getText())
                                    .getImage().getScaledInstance(width, height,
                                    Image.SCALE_SMOOTH));
                            JLabel picLabel = new JLabel(myPicture);
                            picLabel.setSize(new Dimension(width, height));

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

        // la apasarea butonului de update se executa toate operatiile salvate
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // sunt executate toate operatiile
                        for (Operation oper : operations){             
                            if(oper.getType() == 0){
                                executePreprocesing(oper);
                            }
                            
                            if(oper.getType() == 1){
                                executeBinarization(oper);
                            }
                            imageScrollPane.setViewportView(newImgPanel);
                            imageScrollPane.revalidate();
                            imageScrollPane.repaint();
                        }
                        operations.clear();
                    }
                });
            }
        });
        
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        imagePanel.setForeground(new Color(255, 255, 255));

        GroupLayout imagePanelLayout = new GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
                imagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE));
        imagePanelLayout.setVerticalGroup(
                imagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE));

        binarizationButton.setPreferredSize(new Dimension(99, 23));

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

        GroupLayout leftPanelLayout = new GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
                leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(binarizationButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(preprocessingButton)
                .addContainerGap())
                .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(imagePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(browseButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pathTextField, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)))));
        leftPanelLayout.setVerticalGroup(
                leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leftPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(pathTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(browseButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imagePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addComponent(binarizationButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(preprocessingButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));

        binarizationButton.getAccessibleContext().setAccessibleName("");

        imageScrollPane.setBorder(null);
        imageScrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        newImgPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        GroupLayout newImgPanelLayout = new GroupLayout(newImgPanel);
        newImgPanel.setLayout(newImgPanelLayout);
        newImgPanelLayout.setHorizontalGroup(
                newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()))
                .addContainerGap(39, Short.MAX_VALUE)));
               
        newImgPanelLayout.setVerticalGroup(
                newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addGroup(newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newImgPanelLayout.createSequentialGroup())))
                .addContainerGap(74, Short.MAX_VALUE)));
               
        imageScrollPane.setViewportView(newImgPanel);

        updateCheckBox.setText("Immediate Update ");
        updateCheckBox.setActionCommand("Immediate Update ");

        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Enumeration<JCheckBox> enumKey=imageList.keys();
                JCheckBox elem;
                ArrayList<String> pictureSelected = new ArrayList<String>();
                while(enumKey.hasMoreElements()){
                    elem = enumKey.nextElement();
                    if (elem.isSelected()){
                        pictureSelected.add(imageList.get(elem));              
                    }
                }
                
                // Decomentati ce e mai jos pentru verificare daca doua checkboxuri sunt selectate
            /*    if (pictureSelected.size() == 2) {
                    compareWindow = new CompareImagesWindow(pictureSelected.get(0), pictureSelected.get(1));
                    compareWindow.setVisible(true);
                }
                */
                
                // Comentati bucata de mai jos pentru intreaga functionalitate(la sfarsit)
                 try {
                        compareWindow = new CompareImagesWindow("image11.png", "image22.png");
                    } 
                 catch (Exception ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    compareWindow.setVisible(true);
                
            }
        });


        compareButton.setPreferredSize(new Dimension(99, 23));

        GroupLayout rightPanelLayout = new GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
                rightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addComponent(imageScrollPane, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
                .addGroup(GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(compareButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
                .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(updateCheckBox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateButton, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()))));
        rightPanelLayout.setVerticalGroup(
                rightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(updateCheckBox)
                .addComponent(updateButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imageScrollPane, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compareButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)));

        GroupLayout basePanelLayout = new GroupLayout(basePanel);
        basePanel.setLayout(basePanelLayout);
        basePanelLayout.setHorizontalGroup(
                basePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(basePanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(rightPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        basePanelLayout.setVerticalGroup(
                basePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(leftPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addComponent(basePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(0, 18, Short.MAX_VALUE)
                .addComponent(basePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE)));

        //setLocationRelativeTo(null);

        pack();
    }

    /**
     * Metoda in care se initializeaza alti parametri din fereastra principala
     * si care incepe desfasurarea proceselor
     */
    private void init() {

        /* 
         * Rulare parser
         * 
         * Parserul citeste toate fisierele xsd dintr-un folder specificat (hard coded)
         * Intoarce un set de Operatii abstracte - execTypes
         */
        execTypes = Parser.getExecTypes();

        /*
         * Pasare lista executabile de binarizare/preprocesare catre ferestrele aferente;
         * Aici fac deosebirea tipurilor generale de executabile: preprocesare / binarizare
         */
        for (Operation execType : execTypes) {
            //tip preprocesare
            if (execType.getType() == 0) {
                preprocessingWindow.addListElement(execType);
            } //tip binarizare
            else if (execType.getType() == 1) {
                binarizationWindow.addListElement(execType);
            }
        }

        //Se afiseaza fereastra principala (se da drumul aplicatiei)
        this.setVisible(true);
    }

    /**
     * Metoda care primeste un set de operatii de preprocesare si le aplica pe
     * imaginea originala.
     *
     * @param preprocOperations operatiile de preprocesare de executat
     */
    public void launchPreprocOperations(List<Operation> preprocOperations) {
        for (int i=0; i< preprocOperations.size(); i++){
            execTypes.add(preprocOperations.get(i));
            // Fisierul de input este pus in lista de parametri;
            preprocOperations.get(i).getParameters().get(preprocOperations.get(i).getParameters().indexOf("inputFile")).setValue(pathTextField.getText());
            if(updateCheckBox.isSelected()){
                executePreprocesing(preprocOperations.get(i));
            }
        }
    }   

    /**
     * Metoda care primeste un set de operatii de binarizare si le aplica pe
     * imaginea orginara.
     *
     * @param binarizOperations operatiile de binarizare de executat
     */
    public void launchBinarizOperations(List<Operation> binarizOperations) {
        /* TODO
         * se completeaza lista de parametri ai fiecarei operatii cu numele fisierului de intrare;
         */
        
        for (int i=0; i< binarizOperations.size(); i++){
            execTypes.add(binarizOperations.get(i));
            // TODO: de verificat partea cu fisierul de input
            
            if(updateCheckBox.isSelected()){
                executeBinarization(binarizOperations.get(i));
            }
        }
    }
    
    /**
     * Metoda care primeste o operatie de preprocesare, o executa si updateaza
     * panelul cu imaginea din stanga
     * @param oper operatia de preprocesare de executat
     */
    private void executePreprocesing(Operation oper){
        // Setam calea catre fisier in textBox
        oper.execute();
        pathTextField.setText(oper.getParameters().get(oper.getParameters().indexOf("outputImage")).getValue());

        // salvam calea catre imagine
        pathImage = pathTextField.getText();

        // Incarcam imaginea in panel
        int width = imagePanel.getSize().width;
        int height = imagePanel.getSize().height;

        ImageIcon myPicture = new ImageIcon(new ImageIcon(pathTextField.getText())
            .getImage().getScaledInstance(width, height,
            Image.SCALE_SMOOTH));
        JLabel picLabel = new JLabel(myPicture);
        picLabel.setSize(new Dimension(width, height));
        imagePanel.removeAll();
        imagePanel.add(picLabel);
        MainWindow.this.repaint();
    }
    
    /**
     * Metoda care primeste o operatie de binarizare, o executa si updateaza
     * panelul cu imaginea din stanga
     * @param oper operatia de binarizare de executat
     */
    private void executeBinarization(Operation oper){
        oper.execute();
        JCheckBox box = new JCheckBox();
        String path = oper.getParameters().get(oper.getParameters().indexOf("outputFile")).getValue();
        ImageIcon myPicture = new ImageIcon(new ImageIcon(path)
            .getImage().getScaledInstance(imageScrollPane.getSize().width-70, 150,
            Image.SCALE_SMOOTH));
        JLabel label = new JLabel(myPicture);
        int size = imageList.size();
        label.setPreferredSize(new Dimension(imageScrollPane.getSize().width-70, 150));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        box.setPreferredSize(new Dimension(20, 30));
        JPanel containerPanel = new JPanel();
        containerPanel.setSize(imageScrollPane.getSize().width-10,180);
        //containerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        containerPanel.setLocation(1, size*180+1);
        containerPanel.add(box);
        containerPanel.add(label);
        imageList.put(box, path);
        newImgPanel.setPreferredSize(new Dimension(containerPanel.getX(), newImgPanel.getHeight()+containerPanel.getY()));
        newImgPanel.add(containerPanel);        
    }
    
     private void formComponentResized(ComponentEvent evt) {
    }


    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                MainWindow mainWindow = new MainWindow();
            }
        });
    }


}
