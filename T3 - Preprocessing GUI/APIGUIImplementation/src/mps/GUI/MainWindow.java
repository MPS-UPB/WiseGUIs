package mps.GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import mps.parser.ComplexTypeParameter;
import mps.parser.Parser;
import mps.parser.Operation;
import mps.parser.SimpleTypeParameter;

/**
 *
 * @author Liliana
 * @version Last modified by Liliana 12/12/2012
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
    
    
    /*
     * Lista perechilor checkBox - Label
     * Pentru update
     */
    Hashtable<JCheckBox,JLabel> labelList;
   
    
    /**
    * Vector cu checkboxurile din lista din dreapta 
    * care sunt bifate 
    */
   ArrayList<JCheckBox> checkedImages;
   
   
   /*
    * Lista cu toate checkboxurile din lista din dreapta
    */
   ArrayList<JCheckBox> allImages;
   
   
   /*
    * Lista cu toate containerele din partea dreapta
    * Folosit la remove
    */
   ArrayList<JPanel> containerList;
    
    public MainWindow() {

        super("Preprocesing GUI - Main Window");

        // execTypes = new ArrayList<Operation>();
        imageList = new Hashtable<JCheckBox, String>() ;
        labelList = new Hashtable<JCheckBox,JLabel>();
        operations = new ArrayList<Operation>();
        checkedImages = new ArrayList<JCheckBox>();
        allImages = new ArrayList<JCheckBox>();
        containerList = new ArrayList<JPanel>();

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
        checkedImages = new ArrayList<JCheckBox>();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(620, 550));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                formComponentResized(evt);
            }

            private void formComponentResized(ComponentEvent evt) {
            }
        });


        final JFileChooser fileChooser = new JFileChooser("poze de test");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                     //  fileChooser.setCurrentDirectory(
                       //         new File("../../../../../poze de test"));
                    //    fileChooser.setCurrentDirectory(
                    //            new File("E:\\[01] Politehnica\\Anul 4\\Sem 1\\MPS\\Repository\\WiseGUIs\\T3 - Preprocessing GUI\\APIGUIImplementation\\poze de test").getAbsoluteFile());
                        
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
         * La apasarea butonului Update:
         *  - se verifica daca exista cel putin un checkbox selectat in dreapta
         *  - se schimba imaginea de input al operatiilor corespunzatoare
         *  - se executa din nou operatia de binarizare cu noua imagine de input
         *     dar cu aceiasi parametri ca la inceput.
         *  - se updateaza Label-ul cu noua imagine.
         */
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        /*
                         * Daca exista cel putin un checkbox selectat caut numarul de ordine
                         * al fiecarui checkbox selectat, astfel incat sa pot modifica
                         * operatia corespunzatoare din vectorul "operations"
                         */
                         if(checkedImages.size() > 0){
                             for (int i = 0 ; i < checkedImages.size(); i++){
                                 JCheckBox checked = checkedImages.get(i);
                                 for(int j = 0 ; j < allImages.size() ;j ++){
                                     JCheckBox item = allImages.get(i);
                                     // numarul de ordine este j
                                     if(checked == item){
                                         // modific operatia
                                         ((ComplexTypeParameter)operations.get(j).getParameter("inputFile")).setAttribute
                                                 ("name",pathImage);
                                         
                                         // execut operatia pentru a obtine imaginea de output
                                         operations.get(j).execute();
                                         
                                         // construiesc noua imagine din calea obtinuta
                                         String path = ((ComplexTypeParameter)operations.get(j).getParameter("outputFile")).getAttribute("name").getValue();
                                         ImageIcon myPicture = new ImageIcon(new ImageIcon(path)
                                            .getImage().getScaledInstance(imageScrollPane.getSize().width-70, 
                                            150,
                                            Image.SCALE_SMOOTH));
                                         
                                         // Inlocuiesc Label-ul din dreapta
                                         JLabel label = new JLabel(myPicture);
                                         // daca cheia exista deja (cum e in cazul asta) , valoarea e inlocuita
                                         labelList.put(checked,label);          
                                         
                                         // fac refresh la JScrollPane
                                         imageScrollPane.revalidate();
                                         imageScrollPane.repaint();
                                         
                                         // inlocuiesc calea in imageList
                                         imageList.put(checked,path);
                                     }
                                 }
                             }
                         }
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
               
                // Decomentati ce e mai jos pentru verificare daca doua checkboxuri sunt selectate
            /*    if (pictureSelected.size() == 2) {
                    compareWindow = new CompareImagesWindow(pictureSelected.get(0), pictureSelected.get(1));
                    compareWindow.setVisible(true);
                }
                */
                
                // Comentati bucata de mai jos pentru intreaga functionalitate(la sfarsit)
                 try {
                        compareWindow = new CompareImagesWindow("image11.png", "image22.png");
                        //if(checkedImages.size() == 2)
                            //compareWindow = new CompareImagesWindow(imageList.get(checkedImages.get(0)), imageList.get(checkedImages.get(1)));
                        
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
          
            ((ComplexTypeParameter)preprocOperations.get(i).getParameter("inputFile")).setAttribute("name",pathImage);
            
            executePreprocesing(preprocOperations.get(i));
            
            if(updateCheckBox.isSelected())
            {
                for(int j=0;j<operations.size();j++){
                    // se face update cu noua valoare (cheia exista)
                    
                    ((ComplexTypeParameter)operations.get(j).getParameter("inputFile")).setAttribute("name",pathImage);
                    
                    // Executam operatia ca sa obtinem outputFile
                    operations.get(j).execute();
                    
                    // construiesc noua imagine din calea obtinuta
                 
                     String path = ((ComplexTypeParameter)operations.get(j).getParameter("outputFile")).getAttribute("name").getValue();
                    ImageIcon myPicture = new ImageIcon(new ImageIcon(path)
                       .getImage().getScaledInstance(imageScrollPane.getSize().width-70, 
                       150,
                       Image.SCALE_SMOOTH));

                    // Inlocuiesc Label-ul din dreapta
                    JLabel label = new JLabel(myPicture);
                    // Checkboxul j din vectorul allImages corespunde operatiei j din operations
                    JCheckBox item = allImages.get(j);      
                    // daca cheia exista deja (cum e in cazul asta) , valoarea e inlocuita
                    labelList.put(item,label);          

                    
                    // inlocuiesc calea in imageList
                    imageList.put(item,path);
                    
                }
                // fac refresh la JScrollPane
                imageScrollPane.revalidate();
                imageScrollPane.repaint();
            }
        }
    }   

    /**
     * Metoda care primeste un set de operatii de binarizare si le aplica pe
     * imaginea orginara.
     * Operatiile sunt noi, fiind adaugate in vectorul operations
     * Nu e nevoie de verificare de dubluri, operatiile care vin vor fi unice
     *
     * @param binarizOperations operatiile de binarizare de executat
     */
    public void launchBinarizOperations(List<Operation> binarizOperations) {
        
        /*
         * Pentru fiecare operatie de binarizare se completeaza imaginea de input
         * si se executa operatia
         * Dupa executie, operatia se salveaza in vectorul operations
         */
        for (int i=0; i< binarizOperations.size(); i++){
            execTypes.add(binarizOperations.get(i));
         
           ((ComplexTypeParameter)binarizOperations.get(i).getParameter("inputFile")).setAttribute
                   ("name",pathImage);
            executeBinarization(binarizOperations.get(i));
            operations.add(binarizOperations.get(i));
        }
    }
    
    /*
     * Metoda primeste o operatie de BINARIZARE
     * Se sterge din vectorul de operatii
     * Se sterge din lista din dreapta
     */
    public void removeBinarization(Operation op){
        for(int i = 0 ; i < operations.size() ; i++){
            if(operations.get(i) == op){
                JCheckBox deleted = allImages.get(i);
                
                // stergem din hashtable-uri
                labelList.remove(deleted);
                imageList.remove(deleted);
                
                // stergem din panelul mare din dreapta
                newImgPanel.remove(i);
                
                // Verific daca trebuie sters si in vectorul de checkedImages
                for(int j = 0 ; j < checkedImages.size();j++){
                    if(checkedImages.get(j) == deleted){
                        checkedImages.remove(j);
                    }
                }
                
                // Il sterg intr-un final si din vectorul ce contine toate checkboxurile
                allImages.remove(i); 
                
                // refresh la scrollPane
                imageScrollPane.revalidate();
                imageScrollPane.repaint();
                
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
       
         String path =((ComplexTypeParameter)oper.getParameter("outputFile")).getAttribute("name").getValue();
         pathTextField.setText(path);
         System.out.println(path);         
        
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
       
        
        JLabel  lab = (JLabel)imagePanel.getComponent(0);
        lab.setIcon(myPicture);
        MainWindow.this.repaint();
    }
    
    /**
     * Metoda care primeste o operatie de binarizare, o executa si adauga
     * un checkbox cu poza in lista din dreapta
     * @param oper operatia de binarizare de executat
     */
    private void executeBinarization(Operation oper){
        /*
         * Executam operatia (generare de XML + rulare executabil)
         * In urma executiei, operatia are completata calea catre fisierul
         * de output ("outputFile")
         */
        oper.execute();
        
        // Se construieste checkboxul cu fotografie
        final JCheckBox box = new JCheckBox();
        String path = ((ComplexTypeParameter)oper.getParameter("outputFile")).getAttribute("name").getValue();
        System.out.println(path);
        ImageIcon myPicture = new ImageIcon(new ImageIcon(path)
            .getImage().getScaledInstance(imageScrollPane.getSize().width-70, 150,
            Image.SCALE_SMOOTH));
        
        JLabel label = new JLabel(myPicture);
        int size = imageList.size();
        label.setPreferredSize(new Dimension(imageScrollPane.getSize().width-70, 150));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        box.setPreferredSize(new Dimension(20, 30));
        
        /*
         * Ascultator pentru checkbox
         * Daca se bifeaza aces checkbox si deja sunt bifate alte 2 checkboxuri
         *  se inlocuieste primul bifat cu acesta
         * Daca se debifeaza acest checkbox, atunci el se elimina din vectorul
         * checkboxurilor selectate
         */
        box.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent arg0) {
               java.awt.EventQueue.invokeLater(new Runnable() {
                   public void run() {
                       if (box.isSelected()){
                           if(checkedImages.size() == 2){
                               JCheckBox jcb=checkedImages.get(0);
                               jcb.setSelected(false);
                               checkedImages.remove(0);
                           }
                           checkedImages.add(box);
                       }
                       else {
                           checkedImages.remove(box);
                       }
                   }
               });

           }
       });
        
        // Salvez checkboxul
        allImages.add(box);
        
        // Construiesc un panel care va cuprinde checkboxul si Label-ul cu imaginea
        JPanel containerPanel = new JPanel();
        containerPanel.setSize(imageScrollPane.getSize().width-10,180);
        //containerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        containerPanel.setLocation(1, size*180+1);
        containerPanel.add(box);
        containerPanel.add(label);
        
        // Se salveaza containerul
        containerList.add(containerPanel);
        
        // Se salveaza perechea checkbox-cale imagine intr-un Hastable
        imageList.put(box, path);
        // Se salveaza perechea checkbox-label intr-un Hashtable , pentru update
        labelList.put(box, label);
        
        // Se adauga panelul nou creat in lista din dreapta
        newImgPanel.setPreferredSize(new Dimension(containerPanel.getX(), newImgPanel.getHeight()+containerPanel.getY()));
        newImgPanel.add(containerPanel);  
        imageScrollPane.revalidate();
        imageScrollPane.repaint();
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
