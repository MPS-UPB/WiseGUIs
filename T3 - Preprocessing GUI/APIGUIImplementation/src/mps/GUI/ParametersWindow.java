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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.border.EmptyBorder;
import mps.parser.SimpleTypeParameter;
import mps.parser.ComplexTypeParameter;
import mps.parser.SimpleTypeRestriction;
import java.util.ListIterator;

//creez de fiecare data o fereastra noua? cam da
public class ParametersWindow extends javax.swing.JFrame {

    Operation crtOp;
    SecondaryWindow motherWindow;
    ArrayList<SimpleTypeParameter> params;//aici am declarat
    ArrayList<JTextField> text;
    ArrayList<JComboBox> combo;
    ArrayList<JSpinner> spinner;
    JLabel eroare1, eroare2;
    /*aici se vor adauga toate elementele grafice; indexul va corespunde indexului din params
     in functie de tipul elementului se va extrage informatia din elementul grafic - in OK
     si se va trece ca valoare in lista de parametri ai operatiei*/
    ArrayList<Component> graphicElements;

    /**
     * Aceasta functie seteaza mesajele erorilor, culoarea si fontul lor si le
     * seteaza sa nu fie afisate
     */
    private void initializareErori() {
        eroare1 = new JLabel("Trebuie sa completati toate campurile goale");
        eroare2 = new JLabel("Introduceti doar date valide(numere)");
        eroare1.setForeground(new java.awt.Color(255, 0, 0));
        eroare2.setForeground(new java.awt.Color(255, 0, 0));
        eroare1.setFont(new java.awt.Font("Tahoma", 2, 11));
        eroare2.setFont(new java.awt.Font("Tahoma", 2, 11));
        eroare1.setVisible(false);
        eroare2.setVisible(false);
    }

    /**
     * Constructorul clasei de parametri
     *
     * @param window reprezinta fereastra anterioara(de la care s-a ajuns aici)
     * @param op este un obiect al clasei operatii, obiect care este descris in
     * clasa lui
     */
    public ParametersWindow(SecondaryWindow window, Operation op) {

        initComponents();
        setTitle(op.getName());
        this.motherWindow = window;

        text = new ArrayList<JTextField>();
        combo = new ArrayList<JComboBox>();
        spinner = new ArrayList<JSpinner>();

        graphicElements = new ArrayList<Component>();

        initializareErori();
        generateFields(op); //in functie de numarul de parametrii din op se genereaza dinamic fereastra
    }

    /**
     * metoda generata automat de java swing pentru initializare
     */
    private void initComponents() {

    //    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);    //fereastra nu poate fi largita/micsorata cu mouse-ul
        setLocationRelativeTo(null); //pozitionarea ferestrei in centrul ecranului

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
        
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 433, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 306, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * aceasta metoda specifica ce se intampla la apasarea butonului OK. Se
     * preiau datele introduse in TextField, ComboBox si Spinner (doar daca au
     * fost introduse corect, altfel se afisaza un mesaj de eroare) si se
     * transmit catre fereastra secundara(anterioara)
     *
     * @param evt este evenimentu ce reprezinta click-ul pe OK
     */
    private void okClicked(java.awt.event.MouseEvent evt) {

        int k, okay1 = 1, okay2 = 1;
        Pattern p = Pattern.compile("[^0-9]");
        //analizez fiecare "casuta" in care se pot introduce parametri
        for (k = 0; k < text.size(); k++) {
            //daca am gasit un camp necompletat marchez eroarea
            if (text.get(k).getText().equals("")) {
                okay1 = 0;
            }
            //daca utilizatorul a introdus caractere nepermise, marchez si a doua eroare
            if (p.matcher(text.get(k).getText()).find()) {
                okay2 = 0;
            }
        }
        if (okay1 == 0) {
            eroare1.setVisible(true);
        } else if (okay2 == 0) {
            eroare2.setVisible(true);
            eroare1.setVisible(false);
        } //daca nu sunt erori(datele au fost introduse corect)
        else {
            //intorc referinta crtOp
            Collections.reverse(graphicElements);

            //completez parametrii
            for (int i = 2; i < params.size(); i++) {

                SimpleTypeParameter param = params.get(i);


                //caz special - cand parametrul are restrictie de tip enumeration
                if (param.getRestrictions().enumeration != null) {

                    param.setValue(((JComboBox) graphicElements.get(i - 2)).getSelectedItem().toString());
                } else {

                    String type = param.getBaseType();

                    if (type.equals("decimal") || type.equals("integer") || type.equals("int")
                            || type.equals("negativeInteger") || type.equals("nonNegativeInteger")
                            || type.equals("nonPositiveInteger") || type.equals("positiveInteger")
                            || type.equals("float") || type.equals("double")) {


                        param.setValue(((JSpinner) graphicElements.get(i - 2)).getValue().toString());
                    }

                    if (type.equals("string")) {

                        param.setValue(((JTextField) graphicElements.get(i - 2)).getText());
                    }
                }
                //daca e de tip complex, atunci se ia panelul corespunzator si se parcurg toate componentele din el
            }

            motherWindow.addExec(crtOp);
            //pun erorile pe fals, deoarece daca se mai intra o data in aceasta fereastra, ele nu trebuie afisate
            eroare1.setVisible(false);
            eroare2.setVisible(false);
            dispose();
        }
    }

    /**
     * ne intoarce la fereastra precedenta fara niciun efect(se ignora orice
     * date introduse de utilizatori)
     *
     * @param evt este evenimentul ce reprezinta apasarea cu mosue-ul a
     * butonului 'cancel'
     */
    private void cancelClicked(MouseEvent evt) {

        /*
         int result = JOptionPane.showConfirmDialog(
         this,
         "Are you sure you want to Cancel? All your operations will be lost!",
         "Canceling...",
         JOptionPane.YES_NO_OPTION);
         if (result == JOptionPane.YES_OPTION){
         dispose();
         }
         */

       close();
    }

    /**
     * In functie de obiectul op (care contine mai multe informatii legate de
     * numarul de parametrii si tipul lor) afisam dinamic butoanele in pagina
     *
     * @param op este obiectul de operatii
     */
    void generateFields(Operation op) {

        params = op.getParameters();
        crtOp = op;

        //facem un tabel dinamic cu 2 coloane si numar variabil de linii depinzand de numarul de parametri primit
        this.setLayout(new GridLayout(params.size() + 1 + 1, 2, 20, 15));

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        this.add(okButton, params.size(), 0);
        this.add(cancelButton, params.size(), 1);

        this.add(eroare1, params.size(), 0);
        this.add(eroare2, params.size(), 1);

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
        //sau il initializez in constructor
        //asta poate sa fie orice, nu conteaza
        int ySmallPanel = -23;
        int k;

        for (int ii = op.getParameters().size() - 1; ii >= 2; ii--) {

            SimpleTypeParameter param = op.getParameters().get(ii);

            //genereaza un label cu numele parametrului (atributul name din element corespunzator parametrului)      
            JLabel numeParam = new JLabel(param.getName());
            numeParam.setBounds(10, ySmallPanel, 50, 50);
            numeParam.setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(numeParam, ySmallPanel, 0);

            //in functie de tipul parametrului, genereaza elementul grafic asociat 
            //(am putea direct sa stabilim o corespondenta intre 
            //elementele grafice si sa stabilim de la inceput ce generam, 
            //nu de fiecare data cand avem fereastra de parametri sa stam sa analizam)        

            //caz special - cand parametrul are restrictie de tip enumeration


            if (param.getRestrictions().enumeration != null) {

                int n = param.getRestrictions().enumeration.length;

                JComboBox elem = new JComboBox();
                elem.setPreferredSize(new Dimension(140, 30));
                this.add(elem, ySmallPanel, 1);

                for (int i = 0; i < n; i++) {

                    elem.addItem(param.getRestrictions().enumeration[i]);
                }

                graphicElements.add(elem);
                combo.add(elem);
            } else {


                String type = param.getBaseType();


                if (type.equals("decimal") || type.equals("float") || type.equals("double")) {

                    //daca exista restrictii de tipul minVal / maxVal, atunci spinner-ul sa nu permita introducerea acestor valori

                    double minValue = -1000.000;
                    double maxValue = 1000.000;

                    if (param.getRestrictions().minValue != null) {

                        minValue = Double.parseDouble(param.getRestrictions().minValue);
                    }

                    if (param.getRestrictions().maxValue != null) {

                        maxValue = Double.parseDouble(param.getRestrictions().maxValue);
                    }

                    SpinnerNumberModel spinnerModel = new SpinnerNumberModel(Math.max(minValue, 0), minValue, maxValue, 0.001);

                    JSpinner elem = new JSpinner(spinnerModel);
                    //      elem.setEditor(new JSpinner.NumberEditor(elem,"#00.00"));
                    elem.setBounds(70, ySmallPanel, 140, 50);
                    this.add(elem, ySmallPanel, 1);
                    graphicElements.add(elem);
                    spinner.add(elem);
                }

                if (type.equals("integer") || type.equals("int")
                        || type.equals("negativeInteger") || type.equals("nonNegativeInteger")
                        || type.equals("nonPositiveInteger") || type.equals("positiveInteger")) {

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

                    //presupunem ca minValue si maxvalue sunt date corect, tinand cont de restrictia de tip                
                    if (param.getRestrictions().minValue != null) {

                        minValue = Integer.parseInt(param.getRestrictions().minValue);
                    }

                    if (param.getRestrictions().maxValue != null) {

                        maxValue = Integer.parseInt(param.getRestrictions().maxValue);
                    }

                    SpinnerNumberModel spinnerModel = new SpinnerNumberModel(Math.max(minValue, 0), minValue, maxValue, 1);

                    JSpinner elem = new JSpinner(spinnerModel);
                    //      elem.setEditor(new JSpinner.NumberEditor(elem,"#00.00"));
                    elem.setBounds(70, ySmallPanel, 140, 50);
                    this.add(elem, ySmallPanel, 1);
                    graphicElements.add(elem);
                    spinner.add(elem);
                }


                /*
                 if (type.equals("float") || type.equals("double")) {

                 SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0000, -1000.0000, 1000.0000, 0.0001);

                 JSpinner elem = new JSpinner(spinnerModel);
                 //elem.setEditor(new JSpinner.NumberEditor(elem,"#00.00"));
                 elem.setBounds(70, ySmallPanel, 140, 50);
                 this.add(elem, ySmallPanel, 1);
                 graphicElements.add(elem);
                 spinner.add(elem);
                 }
                 */

                if (type.equals("string")) {

                    //ar trebui sa fac mai mare text box-ul, ca sa incapa orice sir
                    JTextField elem = new JTextField(10);
                    this.add(elem, ySmallPanel, 1);
                    graphicElements.add(elem);
                    text.add(elem);
                }

            }

            //daca e de tip complex, atunci se va crea un panel in care vor fi adaugate si atributele
        }
    }
    
    private void close() {
        
         eroare1.setVisible(false);
        eroare2.setVisible(false);
        dispose();
    }
}