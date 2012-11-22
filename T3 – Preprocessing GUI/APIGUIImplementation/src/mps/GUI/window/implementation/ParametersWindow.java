package mps.GUI.window.implementation;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Liz
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;

//creez de fiecare data o fereastra noua? cam da
public class ParametersWindow extends javax.swing.JFrame {

    Operation crtOp;
    SecondaryWindow motherWindow;
    
    public ParametersWindow() {

        initComponents();
    }

    public ParametersWindow(SecondaryWindow window) {

        initComponents();
        this.motherWindow = window;
    }
  
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

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

    private void okClicked(java.awt.event.MouseEvent evt) {

        //intorc referinta crtOp
        motherWindow.addExec(crtOp);
        motherWindow.changeLists();
        dispose();
    }

    private void cancelClicked(MouseEvent evt) {
    	
    	int result = JOptionPane.showConfirmDialog(
	            this,
	            "Are you sure you want to Cancel? All your operations will be lost!",
	            "Canceling...",
	            JOptionPane.YES_NO_OPTION);
	    if (result == JOptionPane.YES_OPTION){
	         dispose();
	    }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //map dummy de transmis
    void generateFields(Operation op) {

        setTitle(op.getName());
        
        LinkedHashMap<String, String> params = op.getParamsList();
        crtOp = op;
        
        this.setLayout(new GridLayout(params.size() + 1, 2, 10, 35));

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        this.add(okButton, params.size(), 0);
        this.add(cancelButton, params.size(), 1);

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

        ArrayList<String> keys = new ArrayList<String>(params.keySet());
        for (k = params.size() - 1; k >= 0; k--) {
            String paramName = keys.get(k);
            String graphicType = params.get(keys.get(k));

            //genereaza un label cu numele parametrului (atributul name din element corespunzator parametrului)      
            JLabel numeParam = new JLabel(paramName);
            numeParam.setBounds(10, ySmallPanel, 50, 50);
            numeParam.setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(numeParam, ySmallPanel, 0);

            //in functie de tipul parametrului, genereaza elementul grafic asociat 
            //(am putea direct sa stabilim o corespondenta intre 
            //elementele grafice si sa stabilim de la inceput ce generam, 
            //nu de fiecare data cand avem fereastra de parametri sa stam sa analizam)        

            if (graphicType.equals("JTextField")) {
                JTextField elem = new JTextField(10);
                this.add(elem, ySmallPanel, 1);
            }

            if (graphicType.equals("JComboBox")) {
                JComboBox elem = new JComboBox();
                elem.setPreferredSize(new Dimension(140, 30));
                this.add(elem, ySmallPanel, 1);
            }

            if (graphicType.equals("JSpinner")) {
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, -1000.0, 1000.0, 1.0);
                JSpinner elem = new JSpinner(spinnerModel);
                elem.setBounds(70, ySmallPanel, 140, 50);
                this.add(elem, ySmallPanel, 1);
            }

        }

    }
}
//sa generez niste ferestre de avertizare, in cazul in care user-ul nu a completat niste campuri!!!
//sa nu-l las pana nu completeaza sau da cancel