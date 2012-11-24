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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.border.EmptyBorder;

//creez de fiecare data o fereastra noua? cam da
public class ParametersWindow extends javax.swing.JFrame {

    Operation crtOp;
    SecondaryWindow motherWindow;
    LinkedHashMap<String, String> params;//aici am declarat
    ArrayList<JTextField> text;
    ArrayList<JComboBox> combo;
    ArrayList<JSpinner> spinner;
    JLabel eroare1, eroare2;
    //aici se vor adauga toate elementele grafice; indexul va corespunde indexului din params
    //in functie de tipul elementului se va extrage informatia din elementul grafic - in OK
    //si se va trece ca valoare in lista de parametri ai operatiei
    ArrayList<Component> graphicElements;

    private void initializareErori() {
        eroare1 = new JLabel("Trebuie sa completati campurile goale");
        eroare2 = new JLabel("Introduceti doar date valide(numere)");
        eroare1.setForeground(new java.awt.Color(255, 0, 0));
        eroare2.setForeground(new java.awt.Color(255, 0, 0));
        eroare1.setFont(new java.awt.Font("Tahoma", 2, 11));
        eroare2.setFont(new java.awt.Font("Tahoma", 2, 11));
        eroare1.setVisible(false);
        eroare2.setVisible(false);
    }

    public ParametersWindow(SecondaryWindow window, Operation op) {

        initComponents();
        setTitle(op.getName());

        this.motherWindow = window;

        text = new ArrayList<JTextField>();
        combo = new ArrayList<JComboBox>();
        spinner = new ArrayList<JSpinner>();
        graphicElements = new ArrayList<Component>();

        initializareErori();
        generateFields(op);
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

        int k, okay1 = 1, okay2 = 1;
        Pattern p = Pattern.compile("[^0-9]");
        for (k = 0; k < text.size(); k++) {
            if (text.get(k).getText().equals("")) {
                okay1 = 0;
            }
            if (p.matcher(text.get(k).getText()).find()) {
                okay2 = 0;
            }
        }
        if (okay1 == 0) {
            eroare1.setVisible(true);
        } else if (okay2 == 0) {
            eroare2.setVisible(true);
            eroare1.setVisible(false);
        } else {
            //intorc referinta crtOp
            int i = 0;

            Collections.reverse(graphicElements);

            //completez parametrii
            for (Map.Entry<String, String> entry : params.entrySet()) {

                if (entry.getValue().equals("JTextField")) {

                    crtOp.getParamsValues().put(entry.getKey(), ((JTextField) graphicElements.get(i)).getText());
                }

                if (entry.getValue().equals("JComboBox")) {

                    //aici s-ar putea sa nu mearga cast-ul
                    crtOp.getParamsValues().put(entry.getKey(), (String) ((JComboBox) graphicElements.get(i)).getSelectedItem());
                }

                if (entry.getValue().equals("JSpinner")) {

                    crtOp.getParamsValues().put(entry.getKey(), ((JSpinner) graphicElements.get(i)).getValue().toString());
                }

                i++;
            }

            motherWindow.addExec(crtOp);
            eroare1.setVisible(false);
            eroare2.setVisible(false);
            dispose();
        }
    }

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

        eroare1.setVisible(false);
        eroare2.setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //map dummy de transmis
    void generateFields(Operation op) {

        params = op.getParamsList();
        crtOp = op;

        this.setLayout(new GridLayout(params.size() + 1 + 1, 2, 10, 35));

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
                graphicElements.add(elem);
                text.add(elem);
            }

            if (graphicType.equals("JComboBox")) {
                JComboBox elem = new JComboBox();
                elem.setPreferredSize(new Dimension(140, 30));
                this.add(elem, ySmallPanel, 1);
                graphicElements.add(elem);
                combo.add(elem);
            }


            //Spinner-ul asta e foarte dubios; n-am idee cum trebuie folosit
            //Vreau sa-l fac sa ia si valorile pe care i LE INTRODUC EU, cu 2 zecimale
            if (graphicType.equals("JSpinner")) {
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.00, -1000.00, 1000.00, 0.01);

                JSpinner elem = new JSpinner(spinnerModel);
                //      elem.setEditor(new JSpinner.NumberEditor(elem,"#00.00"));
                elem.setBounds(70, ySmallPanel, 140, 50);
                this.add(elem, ySmallPanel, 1);
                graphicElements.add(elem);
                spinner.add(elem);
            }

        }

    }
}