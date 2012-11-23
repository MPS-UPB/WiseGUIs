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
    protected MainWindow mainWindow;
    protected ParametersWindow parametersWindow;
    protected ArrayList<Operation> operations;
    protected ArrayList<Operation> selectedExecs;
    protected int selectedIndex;

    /**
     * Creates new form SecondaryWindow
     */
    public SecondaryWindow(MainWindow window) {

        this.mainWindow = window;

        operations = new ArrayList<Operation>();
        selectedExecs = new ArrayList<Operation>();
        jListingModel = new DefaultListModel();
        jChoicesModel = new DefaultListModel();
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

        //aici e cam dubios 

        //NU fac dispose la fereastra cand se inchide, ci fac clear la elementele din lista dreapta
        //tre' sa verific daca mi se deschide aceeasi fereastra la apasarea butonului preproc / binariz

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);




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

    public void addListElement(Operation elem) {

        jListingModel.addElement(elem.getName());
        operations.add(elem);
    }

    public void addExec(Operation op) {

        selectedExecs.add(op);
    }

    protected void swap() {
        
        for (int index = 0; index < jChoicesModel.getSize(); index++) {
            jListingModel.addElement(jChoicesModel.get(index));
        }
        jChoicesModel.removeAllElements();
    }

    protected abstract void okClicked(MouseEvent evt);

    protected abstract void cancelClicked(MouseEvent evt);

    protected abstract void jRemoveButtonActionPerformed(ActionEvent evt);

    protected abstract void jAddButtonActionPerformed(ActionEvent evt);

    protected abstract void addListener();

    public abstract void changeLists();
}
