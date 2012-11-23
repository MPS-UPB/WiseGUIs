package mps.GUI.window.implementation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import sun.security.krb5.internal.crypto.CksumType;

import mps.parser.implementation.Parser;

/**
 *
 * @author Liliana
 * @version Last modified by Roxana 11/15/2012
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel basePanel;
    private JButton binarizationButton;
    private JButton browseButton;
    private JButton compareButton;
    private JCheckBox compareCheckBox1;
    private JCheckBox compareCheckBox2;
    private JPanel imagePanel;
    private JScrollPane imageScrollPane;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel leftPanel;
    private JPanel newImgPanel;
    private JTextField pathTextField;
    private JButton preprocessingButton;
    private JPanel rightPanel;
    private JButton updateButton;
    private JCheckBox updateCheckBox;
    private BinarizationWindow binarizationWindow;
    private PreprocessingWindow preprocessingWindow;
    private CompareImagesWindow compareWindow;
    //tipurile de executabile
    List<Operation> execTypes;

    public MainWindow() {

        super("Preprocesing GUI - Main Window");


        // execTypes = new ArrayList<Operation>();

        //imaginile trebuie date dinamic - sau cream cate o fereastra de cmparare de fiecare data cand avem de comparat ceva?
        //     compareWindow = new CompareImagesWindow("image1.jpg", "image2.jpg");

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
        compareCheckBox1 = new JCheckBox();
        jPanel1 = new JPanel();
        compareCheckBox2 = new JCheckBox();
        jPanel2 = new JPanel();
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

        jPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 222, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 144, Short.MAX_VALUE));

        jPanel2.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 226, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 139, Short.MAX_VALUE));

        GroupLayout newImgPanelLayout = new GroupLayout(newImgPanel);
        newImgPanel.setLayout(newImgPanelLayout);
        newImgPanelLayout.setHorizontalGroup(
                newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addComponent(compareCheckBox1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addComponent(compareCheckBox2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE)));
        newImgPanelLayout.setVerticalGroup(
                newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addGroup(newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(compareCheckBox1)))
                .addGroup(newImgPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(newImgPanelLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(compareCheckBox2)))
                .addContainerGap(74, Short.MAX_VALUE)));

        imageScrollPane.setViewportView(newImgPanel);

        updateCheckBox.setText("Immediate Update ");
        updateCheckBox.setActionCommand("Immediate Update ");
        updateCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateCheckBoxActionPerformed(evt);
            }
        });




        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        if (twoImagesSelected()) {
                            compareWindow = new CompareImagesWindow(
                                    "image1.jpg", "image2.jpg");
                            compareWindow.setVisible(true);
                        }
                    }
                });

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

    private void formComponentResized(ComponentEvent evt) {
    }

    private void updateCheckBoxActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void init() {

        //rulare parser
        //parserul citeste toate fisierele xsd dintr-un folder specificat (hard coded)
        //intoarce un set de Operatii abstracte - typesOfOperations
        //in run-ul parserului: med.setExecTypes()
        //sau direct aici: execTypes = parser.getTypes(); si gata

        //pasare lista executabile de binarizare/preprocesare catre ferestrele aferente

        //aici fac deosebirea tipurilor generale de executabile: preprocesare / binarizare
        execTypes = Parser.getExecTypes();

        for (Operation execType : execTypes) {

            if (execType.getType() == 0) {  //de tip preprocesare

                preprocessingWindow.addListElement(execType);
            } else if (execType.getType() == 1) {

                binarizationWindow.addListElement(execType);
            }
        }

        this.setVisible(true);
    }

    public void launchPreprocOperations(List<Operation> preprocOperations) {
        //TODO
    }

    public void launchBinarizOperations(List<Operation> binarizOperations) {
        //TODO
    }

    private boolean twoImagesSelected() {
        // TODO Auto-generated method stub
        return compareCheckBox1.isSelected() && compareCheckBox2.isSelected();
    }

    public static void main(String args[]) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                MainWindow mainWindow = new MainWindow();


            }
        });
    }
}
