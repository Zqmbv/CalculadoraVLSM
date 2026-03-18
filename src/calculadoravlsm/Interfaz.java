
package calculadoravlsm;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

//region Class JSubRed
// Agrupar los JComponentes para cada Subred en el panel
class JSubRed {
    public JTextField sbName;
    public JTextField sbHost;
    public JButton bErase;

    public JSubRed(String sbName, String sbHost){
        this.sbName = new JTextField(sbName);
        this.sbHost = new JTextField(sbHost);
        this.bErase = new JButton("X");
    }
}

public class Interfaz implements ActionListener,FocusListener{
    //region Components
    JFrame master;

    static ArrayList<String> Errores = new ArrayList<>();
    ArrayList<JSubRed> JSubRedes = new ArrayList<>();

    String nameFont = "Sans Serif";

    Font fPanelTitle = new Font(nameFont,Font.BOLD,16);
    Font fSubTitle = new Font(nameFont,Font.BOLD,12);
    Font fText = new Font(nameFont,Font.PLAIN,13);
    Font fTableText = new Font(nameFont,Font.PLAIN,11);
    Font fTableHeader = new Font(nameFont,Font.BOLD,11);

    GridBagLayout GBL = new GridBagLayout();
    GridBagConstraints GBC = new GridBagConstraints();

    TitledBorder tbInput = new TitledBorder("  DATOS  ");
    TitledBorder tbOutput = new TitledBorder("  RESULTADO  ");
    
    JPanel panelInput = new JPanel();
    JPanel panelInputNorth = new JPanel();
    JPanel panelInputCenter = new JPanel(); 
    JPanel panelInputSouth = new JPanel();
    JPanel panelOutput = new JPanel();

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInput, panelOutput);
    JScrollPane spPanelInputCenter = new JScrollPane(panelInputCenter);

    JLabel lPrefixSymbol = new JLabel("/");

    JButton bAdd = new JButton("AGREGAR SUBRED");
    JButton bGenerate = new JButton("GENERAR");
    
    String sColumns[] = {"SUB RED","HOST","ASIGNADO","/","MÁSCARA","WILDCARD","DIRECCIÓN RED","PRIMERA ÚTIL","ÚLTIMA ÚTIL","BROADCAST"};
    String defaultInformation[] = {"IP Inicial (ej: 192.168.0.0) . . .", "Máscara Inicial (ej: 16) . . ."};

    DefaultTableModel dtmModel = new DefaultTableModel(sColumns, 0);
    JTable jtTable = new JTable(dtmModel);
    JScrollPane spTable = new JScrollPane(jtTable);

    JTextField tfInitialIP = new JTextField(defaultInformation[0]);
    JTextField tfInitialMask = new JTextField(defaultInformation[1]);

    Dimension dimSubRed = new Dimension(175, 20);
    Dimension dimHost = new Dimension(85, 20);
    Dimension dimErase = new Dimension(45, 20);
    Dimension dimLabelNum = new Dimension(25, 20);

    JMenuBar menuBar = new JMenuBar();
    JMenu mFile = new JMenu("Archivo");
    JMenuItem miImport = new JMenuItem("Importar Configuración");
    JMenuItem miExport = new JMenuItem("Exportar Cálculo");

    //region Constructor
    public Interfaz(JFrame frame){
        this.master = frame;

        // Deben estar activadas las 4 funciones para que la interfaz quede bonita.
        setPosition();                                  
        new UIStyle(this);                              
        setConfigComponents();                          
        setFont();                                      
    }

    //region Position
    public void setPosition(){
        this.master.setLayout(GBL);

        // mFile.add(miExport);
        mFile.add(miImport);
        menuBar.add(mFile);

        GBC.fill = GridBagConstraints.BOTH; GBC.anchor = GridBagConstraints.NORTH;

        // PANEL INPUT & OUTPUT DINAMICO
        GBC.insets = new Insets(15, 20, 20, 20);
        GBC.weightx = 1; GBC.weighty = 1;
        GBC.gridx = 0; GBC.gridy = 1; this.master.add(splitPane, GBC);

        // MENU BAR
        GBC.weightx = 0; GBC.weighty = 0;
        GBC.insets = new Insets(0, 0, 0, 0);
        GBC.gridx = 0; GBC.gridy = 0; GBC.gridwidth = 2; this.master.add(menuBar, GBC);

        // PANEL INPUT 
        panelInput.setLayout(GBL);
        GBC.gridwidth = 1;

        GBC.insets = new Insets(10, 10, 5, 10);    
        GBC.ipady = 0;
        GBC.weightx = 1; GBC.weighty = 0;
        GBC.gridx = 0; GBC.gridy = 0; panelInput.add(panelInputNorth,GBC);
        
        GBC.insets = new Insets(5, 10, 5, 10);  
        GBC.ipady = 0;
        GBC.weighty = 1;
        GBC.gridx = 0; GBC.gridy = 1; panelInput.add(spPanelInputCenter,GBC);
        
        GBC.insets = new Insets(5, 10, 10, 10);  
        GBC.ipady = 10;
        GBC.weighty = 0;
        GBC.gridx = 0; GBC.gridy = 2; panelInput.add(panelInputSouth,GBC);

        // PANEL NORTE - TITULO, IP Y MASCARA INICIAL
        panelInputNorth.setLayout(GBL);
        
        GBC.fill = GridBagConstraints.BOTH;
        GBC.insets = new Insets(0, 0, 0, 0);    
        GBC.ipadx = 0; GBC.ipady = 8;
        GBC.weightx = 1; GBC.weighty = 0;
        GBC.gridx = 0; GBC.gridy = 1; panelInputNorth.add(tfInitialIP,GBC);

        GBC.anchor = GridBagConstraints.CENTER; GBC.fill = GridBagConstraints.NONE;
        GBC.ipadx = 20; GBC.ipady = 8;
        GBC.weightx = 0; 
        GBC.gridx = 1; GBC.gridy = 1; panelInputNorth.add(lPrefixSymbol,GBC);
        
        GBC.fill = GridBagConstraints.BOTH;
        GBC.ipadx = 0; GBC.ipady = 8;
        GBC.weightx = 0.75;
        GBC.gridx = 2; GBC.gridy = 1; panelInputNorth.add(tfInitialMask,GBC);

        // PANEL CENTRO - NOMBRE Y HOST DE SUBREDES
        panelInputCenter.setLayout(GBL);

        GBC.anchor = GridBagConstraints.NORTH; GBC.fill = GridBagConstraints.VERTICAL;
        GBC.insets = new Insets(10, 5, 5, 5);
        GBC.weightx = 1; GBC.weighty = 1; 
        GBC.gridx = 0; GBC.gridy = 999; GBC.gridwidth = 4; panelInputCenter.add(new JLabel(""), GBC);

        // PANEL SUR
        panelInputSouth.setLayout(GBL);
        GBC.gridwidth = 1;
        GBC.fill = GridBagConstraints.BOTH; GBC.anchor = GridBagConstraints.CENTER;
        
        GBC.insets = new Insets(0, 0, 0, 5);
        GBC.weightx = 1; GBC.weighty = 1;
        GBC.gridx = 0; GBC.gridy = 0; panelInputSouth.add(bAdd,GBC);

        GBC.insets = new Insets(0, 5, 0, 0);
        GBC.gridx = 1; GBC.gridy = 0; panelInputSouth.add(bGenerate,GBC);

        // PANEL OUTPUT
        panelOutput.setLayout(GBL);
        GBC.insets = new Insets(10, 10, 10, 10);
        panelOutput.add(spTable, GBC);
    }

    //region Configuration
    public void setConfigComponents(){
        splitPane.setDividerLocation(425); 
        splitPane.setContinuousLayout(true); 
        splitPane.setDividerSize(12); 
        splitPane.setBorder(BorderFactory.createEmptyBorder());

        panelInput.setBorder(tbInput);
        panelOutput.setBorder(tbOutput);

        tbInput.setTitleJustification(TitledBorder.CENTER);
        tbOutput.setTitleJustification(TitledBorder.CENTER);

        panelInput.setMinimumSize(new Dimension(425, 0));
        panelOutput.setMinimumSize(new Dimension(400, 0));

        tfInitialIP.setBorder(BorderFactory.createCompoundBorder(tfInitialIP.getBorder(),BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        tfInitialMask.setBorder(BorderFactory.createCompoundBorder(tfInitialMask.getBorder(),BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        lPrefixSymbol.setHorizontalAlignment(JLabel.CENTER);
        spPanelInputCenter.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spPanelInputCenter.getVerticalScrollBar().setUnitIncrement(16);
        spTable.getVerticalScrollBar().setUnitIncrement(16);

        jtTable.setFillsViewportHeight(true);
        jtTable.setRowHeight(20);
        jtTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtTable.setEnabled(false);

        TableColumnModel columnModel = jtTable.getColumnModel();
        int Widths[] = {100,50,50,5,100,100,100,100,100,100};
        for(int i=0; i<=9; i++) columnModel.getColumn(i).setPreferredWidth(Widths[i]);

        bAdd.addActionListener(this);
        bGenerate.addActionListener(this);
        //miExport.addActionListener(this);
        miImport.addActionListener(this);

        tfInitialIP.addFocusListener(this);
        tfInitialMask.addFocusListener(this);
    }

    //region Tipography
    public void setFont(){
        lPrefixSymbol.setFont(fSubTitle);

        tbInput.setTitleFont(fPanelTitle);
        tbOutput.setTitleFont(fPanelTitle);

        jtTable.getTableHeader().setFont(fTableHeader);
        jtTable.setFont(fTableText);

        tfInitialIP.setFont(fText);
        tfInitialMask.setFont(fText);

        bAdd.setFont(fSubTitle);
        bGenerate.setFont(fSubTitle);
        
        UIManager.put("Button.font",fSubTitle);
        UIManager.put("OptionPane.messageFont",fText);
    }

    //region Functions
    public void updatePanelJSB(){
        panelInputCenter.removeAll();
        for(int i=0; i<JSubRedes.size(); i++){
            JSubRed currentJSB = JSubRedes.get(i);
            currentJSB.sbName.setText("Subred "+(i+1)+"");

            // Posicion
            if(i==0) GBC.insets = new Insets(10, 10, 5, 5);
            else GBC.insets = new Insets(5, 10, 5, 10);
            GBC.fill = GridBagConstraints.HORIZONTAL; GBC.anchor = GridBagConstraints.CENTER;
            GBC.gridwidth = 1; GBC.gridheight = 1;
            GBC.ipadx = 0; GBC.ipady = 8; GBC.weighty = 0;

            int top;
            if(i==0) top = 10;
            else top = 5;
            
            GBC.insets = new Insets(top, 10, 5, 5);
            GBC.gridx = 0; GBC.gridy = i; GBC.weightx = 1; panelInputCenter.add(currentJSB.sbName,GBC);
            GBC.insets = new Insets(top, 5, 5, 5);
            GBC.gridx = 1; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.sbHost,GBC);
            GBC.insets = new Insets(top, 5, 5, 10);
            GBC.gridx = 2; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.bErase,GBC);

            UIStyle.setStyleJSubRed(currentJSB);

            // ConfigComponent
            currentJSB.sbName.setPreferredSize(dimSubRed);
            currentJSB.sbName.setBorder(BorderFactory.createCompoundBorder(currentJSB.sbName.getBorder(),BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        
            currentJSB.sbHost.setPreferredSize(dimHost);
            currentJSB.sbHost.setBorder(BorderFactory.createCompoundBorder(currentJSB.sbHost.getBorder(),BorderFactory.createEmptyBorder(2, 5, 2, 5)));
            
            currentJSB.bErase.setPreferredSize(dimErase);
            currentJSB.bErase.addActionListener(this);

            // Tipografia
            currentJSB.sbName.setFont(fText);
            currentJSB.bErase.setFont(fSubTitle);
        }
        GBC.weightx = 1; GBC.weighty = 1; 
        GBC.gridwidth = 3;
        GBC.gridx = 0; GBC.gridy = 999; panelInputCenter.add(new JLabel(""), GBC); 
        
        panelInputCenter.revalidate();
        panelInputCenter.repaint();
    }

    //region Events
    @Override
    public void actionPerformed(ActionEvent ae){

        for (int i=0; i<JSubRedes.size(); i++) {
            if (ae.getSource() == JSubRedes.get(i).bErase) {
                JSubRedes.remove(i); 
                updatePanelJSB();
            }
        }
        
        if(ae.getSource()==bAdd){
            JSubRedes.add(new JSubRed("",""));
            updatePanelJSB();
        }

        if(ae.getSource()==bGenerate){
            Errores.clear();

            String sIPInicial = tfInitialIP.getText().trim();
            if (tfInitialIP.getForeground()==UIStyle.cTextUnfocused) sIPInicial = "";

            String sMascaraInicial = tfInitialMask.getText().trim();
            if (tfInitialMask.getForeground()==UIStyle.cTextUnfocused) sMascaraInicial = "";

            VLSM myVLSM = new VLSM(sIPInicial, sMascaraInicial, JSubRedes);
            System.out.println(myVLSM);
            if(Errores.size()!=0){
                JOptionPane.showMessageDialog(this.master,Errores.get(0),"ERROR",JOptionPane.ERROR_MESSAGE);
                dtmModel.setRowCount(0);
                return;
            }
            else{
                dtmModel.setRowCount(0);
                for(int i=0; i<myVLSM.getSubredes().size(); i++){
                    dtmModel.addRow(myVLSM.getSubredes().get(i).toStringTable());
                }
                JOptionPane.showMessageDialog(this.master,"VLSM realizado correctamente.","ÉXITO",JOptionPane.INFORMATION_MESSAGE);
            }
            panelOutput.revalidate();
            panelOutput.repaint();
        }

        if(ae.getSource()==miImport){
            FileDialog FD = new FileDialog(this.master, "Abrir Configuración VLSM", FileDialog.LOAD);
            FD.setFile("*.txt;*.csv"); // Filtrar archivos TXT o CSV
            FD.setVisible(true);
            
            if(FD.getFile()==null){
                return;
            }

            String sFile = FD.getFile();
            if(!sFile.endsWith(".txt") && !sFile.endsWith(".csv")){
                JOptionPane.showMessageDialog(this.master, "Formato de archivo inválido.");
                return;
            }

            String sPath = FD.getDirectory()+sFile;
            try {
                ArrayList<ArrayList<String>> sVLSM = ManejadorTexto.readFile(sPath);
                tfInitialIP.setText(sVLSM.get(0).get(0)); tfInitialIP.setForeground(UIStyle.cText);
                tfInitialMask.setText(sVLSM.get(0).get(1)); tfInitialMask.setForeground(UIStyle.cText);
                panelInputCenter.removeAll(); JSubRedes.clear(); sVLSM.remove(0);
                for(ArrayList<String> sSubRed: sVLSM) JSubRedes.add(new JSubRed(sSubRed.get(0), sSubRed.get(1)));
                updatePanelJSB();
            } catch (IOException IOe) {
                JOptionPane.showMessageDialog(this.master,"Archivo inválido","ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
        // if(ae.getSource()==miExport){
        //     LocalDateTime LDT = LocalDateTime.now();
        //     DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        //     String sDateTime = LDT.format(DTF);
        //     FileDialog FD = new FileDialog(this, "Exportar Reporte VLMS PDF", FileDialog.SAVE);
        //     FD.setFile("Reporte_VLSM_"+sDateTime+".pdf");
        //     FD.setVisible(true);

        //     if(FD.getFile()==null){
        //         return;
        //     }
        // }
    }

    @Override
    public void focusGained(FocusEvent fe) {
        if(fe.getComponent()==tfInitialIP && tfInitialIP.getText().equals(defaultInformation[0])){
            tfInitialIP.setText("");
            tfInitialIP.setForeground(UIStyle.cText);
        }
        if(fe.getComponent()==tfInitialMask && tfInitialMask.getText().equals(defaultInformation[1])){
            tfInitialMask.setText("");
            tfInitialMask.setForeground(UIStyle.cText);
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
        if(fe.getComponent()==tfInitialIP && tfInitialIP.getText().equals("")){
            tfInitialIP.setText(defaultInformation[0]);
            tfInitialIP.setForeground(UIStyle.cTextUnfocused);
        }
        if(fe.getComponent()==tfInitialMask && tfInitialMask.getText().equals("")){
            tfInitialMask.setText(defaultInformation[1]);
            tfInitialMask.setForeground(UIStyle.cTextUnfocused);
        }
    }

}