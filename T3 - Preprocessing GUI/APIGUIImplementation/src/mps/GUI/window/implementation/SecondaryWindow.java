/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI.window.implementation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

/**
 * Clasa care descrie partea comuna a ferestrelor de binarizare si de
 * preprocesare.
 *
 * @author Liz
 */
public abstract class SecondaryWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    protected DefaultListModel jListingModel;
    protected DefaultListModel jChoicesModel;
    protected JButton jAddButton;
    protected JButton jOKButton;
    protected JButton jCancelButton;
    protected JLabel jLabel3;
    protected JList jChoicesPanelList;
    protected JList jListingPanelList;
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
     * Lista curenta cu executabilele selectate (pana sa se dea iar OK).
     */
    ArrayList<Operation> currentSelection;

    /**
     * Creates new form SecondaryWindow.
     */
    public SecondaryWindow(MainWindow window) {

        this.mainWindow = window;

        operations = new ArrayList<Operation>();
        jListingModel = new DefaultListModel();
        jChoicesModel = new DefaultListModel();
        oldSelection = new ArrayList<Operation>();
        currentSelection = new ArrayList<Operation>();
        //initial nu este selectat nimic
        selectedIndex = -1;
        initComponents();
        setLocationRelativeTo(null);
    }

    protected void initComponents() {

        jScrollPane1 = new JScrollPane();
        jListingPanelList = new JList(jListingModel);
        jScrollPane2 = new JScrollPane();
        jChoicesPanelList = new JList(jChoicesModel);
        jAddButton = new JButton();
        jRemoveButton = new JButton();
        jOKButton = new JButton();
        jCancelButton = new JButton();
        jLabel3 = new JLabel("Select ops from left:");

//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

        addListener();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jOKButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCancelButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jAddButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRemoveButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addComponent(jScrollPane2)))
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jAddButton)
                .addGap(64, 64, 64)
                .addComponent(jRemoveButton)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jOKButton)
                .addComponent(jCancelButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }

    /**
     * Metoda care adauga un nume de excutabil in panelul din stanga.
     *
     * @param elem operatia al carui nume va fi adaugat in lista din stanga
     */
    public void addListElement(Operation elem) {

        //Metoda va fi apelata in metoda init() din MainWindow; 
        //Adaugarea acestor elemente se va face astfel la pornirea aplicatiei
        jListingModel.addElement(elem.getName());
        //Se adauga operatia si in lista de operatii de preprocesare / de binarizare
        operations.add(elem);
    }

    /**
     * Metoda care adauga o operatie cu tot cu valorile parametrilor acesteia in
     * lista de operatii de executat asupra imaginii. - apelata de fereastra de
     * parametri, pentru a transmite parametrii operatiei curente
     */
    public void addExec(Operation op) {

        //Operatia se va adauga listei curente de executabile; 
        //doar la apasarea butonului "OK" va fi transmisa mai departe in fereastra principala, spre a fi lansata in executie
        currentSelection.add(op);

        //Pentru debugging
        System.out.println(op.getParamsValues().toString());

        //Executabilul ales din stanga trece in lista din dreapta
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
     * @param evt evenimentul mouse clicked
     */
    protected abstract void okClicked(MouseEvent evt);

    /**
     * Metoda apelata cand se apasa pe butonul "Cancel".
     *
     * @param evt evenimentul mouse clicked
     */
    protected abstract void cancelClicked(MouseEvent evt);

    /**
     * Metoda care adauga listeneri pe fereastra.
     */
    protected abstract void addListener();

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
    public abstract void close();
}
