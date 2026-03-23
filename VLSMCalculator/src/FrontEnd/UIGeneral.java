package FrontEnd;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import BackEnd.*;

//region Class JSubnet
class JSubnet{ 
    public JLabel ID;
    public JTextField sbName;
    public JSpinner sbHost;
    public JButton bErase;

    public JSubnet(String sbName, long sbHost){
        this.ID = new JLabel();
        this.sbName = new JTextField(sbName);
        this.sbHost = new JSpinner(new SpinnerNumberModel(sbHost, 0L, 4294967296L, 1L));
        this.bErase = new JButton("X");
    }
}

public class UIGeneral implements ActionListener,FocusListener{
    //region Components
    JFrame master;

    static ArrayList<String> errorMessages = new ArrayList<>();
    ArrayList<JSubnet> JSubnets = new ArrayList<>();

    GridBagLayout GBL = new GridBagLayout();
    GridBagConstraints GBC = new GridBagConstraints();

    TitledBorder tbInput = new TitledBorder("  DATOS  ");
    TitledBorder tbOutput = new TitledBorder("  RESULTADO  ");

    Border bfline = BorderFactory.createLineBorder(UIStyle.cBorder, 1);
    Border bfEmpty = BorderFactory.createEmptyBorder(2, 4, 2, 4);
    Border bfFinal = BorderFactory.createCompoundBorder(bfline,bfEmpty);

    JPanel panelInput = new JPanel();
    JPanel panelInputNorth = new JPanel();
    JPanel panelInputCenter = new JPanel(); 
    JPanel panelInputSouth = new JPanel();
    JPanel panelOutput = new JPanel();

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInput, panelOutput);
    JScrollPane spPanelInputCenter = new JScrollPane(panelInputCenter);

    JLabel lPrefixSymbol = new JLabel("/");
    JLabel lUser = new JLabel("By Zqmbv 2026");
    JLabel lID = new JLabel("ID");
    JLabel lNombre = new JLabel("Nombre");
    JLabel lHost = new JLabel("Host");

    JButton bAdd = new JButton("AGREGAR SUBRED");
    JButton bGenerate = new JButton("GENERAR");
    JButton bSaveVLSM = new JButton("GUARDAR");
    
    String sColumns[] = {"SUB RED","HOST","ASIGNADO","/","MÁSCARA","WILDCARD","DIRECCIÓN RED","PRIMERA ÚTIL","ÚLTIMA ÚTIL","BROADCAST"};
    String defaultInformation[] = {"IP Inicial (ej: 192.168.0.0) . . .", "Máscara Inicial (ej: 16) . . ."};

    DefaultTableModel DTM = new DefaultTableModel(sColumns, 0);
    JTable jtTable = new JTable(DTM);
    JScrollPane spTable = new JScrollPane(jtTable);

    JTextField tfInitialIP = new JTextField(defaultInformation[0]);
    JTextField tfInitialMask = new JTextField(defaultInformation[1]);

    Dimension dimSubRed = new Dimension(175, 20);
    Dimension dimHost = new Dimension(95, 20);
    Dimension dimErase = new Dimension(35, 20);
    Dimension dimID = new Dimension(25, 20);

    JMenuBar menuBar = new JMenuBar();
    JMenu mFile = new JMenu("Archivo");
    JMenuItem miImportConfig = new JMenuItem("Importar Configuración");
    JMenuItem miExportConfig = new JMenuItem("Exportar Congifuración");

    //region Constructor
    public UIGeneral(JFrame frame){
        this.master = frame;
        
        new UIStyle(this); // COLOR & FONT                      
        setPosition();                                  
        setConfigComponents();                                                        
    }

    //region Position
    private void setPosition(){
        this.master.setLayout(GBL);

        // mFile.add(miExportConfig);
        mFile.add(miImportConfig);
        mFile.add(miExportConfig);
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

        GBC.insets = new Insets(0, 20, 2,10);
        GBC.weightx = 0; GBC.weighty = 0;
        GBC.gridx = 0; GBC.gridy = 1; GBC.gridwidth = 1; this.master.add(lUser, GBC);

        // PANEL INPUT 
        panelInput.setLayout(GBL);
        
        GBC.insets = new Insets(5, 20, 2, 5);  
        GBC.weightx = 0;
        GBC.gridx = 0; GBC.gridy = 1; panelInput.add(lID,GBC);

        GBC.insets = new Insets(5, 22, 2, 5);  
        GBC.weightx = 1;
        GBC.gridx = 1; GBC.gridy = 1; panelInput.add(lNombre,GBC);

        GBC.insets = new Insets(5, 22, 2, 146);  
        GBC.weightx = 0;
        GBC.gridx = 2; GBC.gridy = 1; panelInput.add(lHost,GBC);
        
        GBC.gridwidth = 3;
        GBC.insets = new Insets(10, 10, 5, 10);    
        GBC.ipady = 0;
        GBC.weightx = 1; GBC.weighty = 0;
        GBC.gridx = 0; GBC.gridy = 0; panelInput.add(panelInputNorth,GBC);
        
        GBC.insets = new Insets(2, 10, 5, 10);  
        GBC.ipady = 0;
        GBC.weighty = 1;
        GBC.gridx = 0; GBC.gridy = 2; panelInput.add(spPanelInputCenter,GBC);
        
        GBC.insets = new Insets(5, 10, 10, 10);  
        GBC.ipady = 10;
        GBC.weighty = 0;
        GBC.gridx = 0; GBC.gridy = 3; panelInput.add(panelInputSouth,GBC);

        // PANEL NORTE - TITULO, IP Y MASCARA INICIAL
        panelInputNorth.setLayout(GBL);
        GBC.gridwidth = 1;
        
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

        GBC.insets = new Insets(0, 5, 0, 5);
        GBC.gridx = 1; GBC.gridy = 0; panelInputSouth.add(bGenerate,GBC);

        GBC.insets = new Insets(0, 5, 0, 0);
        GBC.gridx = 2; GBC.gridy = 0; panelInputSouth.add(bSaveVLSM,GBC);

        // PANEL OUTPUT
        panelOutput.setLayout(GBL);
        GBC.insets = new Insets(10, 10, 10, 10);
        panelOutput.add(spTable, GBC);
    }

    //region Configuration
    private void setConfigComponents(){
        lUser.setVerticalAlignment(JLabel.BOTTOM);
        lUser.setHorizontalAlignment(JLabel.RIGHT);
        lID.setHorizontalAlignment(JLabel.CENTER);

        splitPane.setDividerLocation(429); 
        splitPane.setContinuousLayout(true); 
        splitPane.setDividerSize(12); 
        splitPane.setBorder(BorderFactory.createEmptyBorder());

        panelInput.setBorder(tbInput);
        panelOutput.setBorder(tbOutput);

        tbInput.setTitleJustification(TitledBorder.CENTER);
        tbOutput.setTitleJustification(TitledBorder.CENTER);

        panelInput.setMinimumSize(new Dimension(429, 0));
        panelOutput.setMinimumSize(new Dimension(400, 0));

        tfInitialIP.setBorder(bfFinal);
        tfInitialMask.setBorder(bfFinal);

        lPrefixSymbol.setHorizontalAlignment(JLabel.CENTER);
        spPanelInputCenter.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spPanelInputCenter.getVerticalScrollBar().setUnitIncrement(16);

        spTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spTable.getVerticalScrollBar().setUnitIncrement(16);

        jtTable.setFillsViewportHeight(true);
        jtTable.setRowHeight(20);
        jtTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtTable.setEnabled(false);
        jtTable.setDragEnabled(false);

        TableColumnModel columnModel = jtTable.getColumnModel();
        int Widths[] = {100,50,50,5,100,100,100,100,100,100};
        for(int i=0; i<=9; i++) columnModel.getColumn(i).setPreferredWidth(Widths[i]);

        bSaveVLSM.setEnabled(false);

        bAdd.addActionListener(this);
        bGenerate.addActionListener(this);
        bSaveVLSM.addActionListener(this);
        miExportConfig.addActionListener(this);
        miImportConfig.addActionListener(this);

        tfInitialIP.addFocusListener(this);
        tfInitialMask.addFocusListener(this);
    }

    //region Functions/Auxiliaries
    private void updatePanelJSB(){
        panelInputCenter.removeAll();

        for(int i=0; i<JSubnets.size(); i++){
            JSubnet currentJSB = JSubnets.get(i);
            currentJSB.ID.setText(""+(i+1)+"");

            // POSITION
            GBC.fill = GridBagConstraints.HORIZONTAL; GBC.anchor = GridBagConstraints.CENTER;
            GBC.gridwidth = 1; GBC.gridheight = 1;
            GBC.ipadx = 0; GBC.ipady = 8; GBC.weighty = 0;

            int top;
            if(i==0) top = 10; else top = 5;
            
            GBC.insets = new Insets(top, 5, 5, 0);
            GBC.gridx = 0; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.ID,GBC);

            GBC.insets = new Insets(top, 5, 5, 5);
            GBC.gridx = 1; GBC.gridy = i; GBC.weightx = 1; panelInputCenter.add(currentJSB.sbName,GBC);
            
            GBC.insets = new Insets(top, 5, 5, 5);
            GBC.gridx = 2; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.sbHost,GBC);
            
            GBC.insets = new Insets(top, 5, 5, 10);
            GBC.gridx = 3; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.bErase,GBC);

            UIStyle.setStyleJSubnet(currentJSB);

            // CONFIG COMPONENT
            currentJSB.ID.setHorizontalAlignment(JLabel.CENTER);
            currentJSB.ID.setPreferredSize(dimID);

            currentJSB.sbName.setPreferredSize(dimSubRed);
            currentJSB.sbName.setBorder(bfFinal);

            ((JSpinner.DefaultEditor)currentJSB.sbHost.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
            currentJSB.sbHost.setPreferredSize(dimHost);
            currentJSB.sbHost.setBorder(bfFinal);
            
            currentJSB.bErase.setPreferredSize(dimErase);
            currentJSB.bErase.addActionListener(this);
        }
        
        // PARA QUE LOS JSUBNETS SE MANTENGAN PEGADOS EN EL NORTE DEL PANEL
        GBC.weightx = 1; GBC.weighty = 1; 
        GBC.gridwidth = 4;
        GBC.gridx = 0; GBC.gridy = 999; panelInputCenter.add(new JLabel(""), GBC); 
        
        panelInputCenter.revalidate();
        panelInputCenter.repaint();
    }

    private void removeJSubnet(ActionEvent ae){
        for (int i=0; i<JSubnets.size(); i++) {
            if (ae.getSource() == JSubnets.get(i).bErase) {
                JSubnets.remove(i); 
                updatePanelJSB();
            }
        }
    }

    private void addJSubnet(String sbName, int sbHost){
        JSubnets.add(new JSubnet(sbName,sbHost));
        updatePanelJSB();
    }

    private void generateVLSM(){
        errorMessages.clear();

        int ip[] = UIVerificator.getInitialIP(tfInitialIP);
        int mask = UIVerificator.getInitialMask(tfInitialMask);
        ArrayList<Subnet> subnets = UIVerificator.getSubnets(JSubnets);

        if(errorMessages.size()!=0){
            JOptionPane.showMessageDialog(this.master,errorMessages.get(0),"ERROR",JOptionPane.ERROR_MESSAGE);
            DTM.setRowCount(0);
            bSaveVLSM.setEnabled(false);
            return;
        }

        VLSM myVLSM = new VLSM(ip, mask, subnets);
        // System.out.println(myVLSM);

        DTM.setRowCount(0);
        for(int i=0; i<myVLSM.getSubredes().size(); i++) DTM.addRow(myVLSM.getSubredes().get(i).toStringTable());
        bSaveVLSM.setEnabled(true);
        JOptionPane.showMessageDialog(this.master,"VLSM realizado correctamente.","ÉXITO",JOptionPane.INFORMATION_MESSAGE);

        panelOutput.revalidate();
        panelOutput.repaint();
    }

    private String getSelectFileDialog(String FileDialogTitle, int PurposeFileDialog, String FileFilter){
        FileDialog FD = new FileDialog(this.master, FileDialogTitle, PurposeFileDialog);
        FD.setFile("*"+FileFilter);
        FD.setVisible(true);
        
        if(FD.getFile()==null){
            return null;
        }

        String sFile = FD.getFile();
        if(!sFile.endsWith(FileFilter)){
            JOptionPane.showMessageDialog(this.master, "Formato de archivo inválido.","ERROR",JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return FD.getDirectory()+sFile;
    }

    private void importConfig(){
        String sPath = getSelectFileDialog("Abrir Configuración VLSM",FileDialog.LOAD,".txt");
        try {
            ArrayList<ArrayList<String>> sVLSM = FileHandler.readFile(sPath);

            if(sVLSM==null){
                JOptionPane.showMessageDialog(this.master, "Archivo inválido.","ERROR",JOptionPane.ERROR_MESSAGE);
                return;
            }

            tfInitialIP.setText(sVLSM.get(0).get(0)); tfInitialIP.setForeground(UIStyle.cText);
            tfInitialMask.setText(sVLSM.get(0).get(1)); tfInitialMask.setForeground(UIStyle.cText);

            panelInputCenter.removeAll(); JSubnets.clear(); sVLSM.remove(0);
            for(ArrayList<String> sSubRed: sVLSM) JSubnets.add(new JSubnet(sSubRed.get(0),Long.parseLong(sSubRed.get(1))));
            updatePanelJSB();
            JOptionPane.showMessageDialog(this.master,"Configuración importada correctamente.","ÉXITO",JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException IOe) {
            JOptionPane.showMessageDialog(this.master,"Archivo inválido.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportConfig(){
        String sPath = getSelectFileDialog("Guardar Configuración VLSM",FileDialog.SAVE,".txt");
        try {
            ArrayList<ArrayList<String>> config = new ArrayList<ArrayList<String>>();
            ArrayList<String> data = new ArrayList<String>();

            if(tfInitialIP.getForeground()==UIStyle.cTextUnfocused) data.add("");
            else data.add(tfInitialIP.getText().trim());
            
            if(tfInitialMask.getForeground()==UIStyle.cTextUnfocused) data.add("");
            else data.add(tfInitialMask.getText().trim());
        
            config.add(data);
            
            if(JSubnets.size()!=0){
                for(JSubnet JSB: JSubnets){
                    data = new ArrayList<String>();
                    data.add(JSB.sbName.getText().trim());
                    data.add(String.valueOf(((Number) JSB.sbHost.getValue()).longValue()));
                    config.add(data);
                }
            }
            FileHandler.writeFile(sPath, config);
            JOptionPane.showMessageDialog(this.master,"Configuración exportada correctamente en:\n"+sPath,"ÉXITO",JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException IOe) {
            JOptionPane.showMessageDialog(this.master,"Archivo inválido.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveResult(){
        String sPath = getSelectFileDialog("Guardar Resultados VLSM",FileDialog.SAVE,".xlsx");
        String networkInfo = tfInitialIP.getText().trim() + " / " + tfInitialMask.getText().trim();
        try {
            if(ExcelHandler.exportToExcel(jtTable, sPath, networkInfo)){
                JOptionPane.showMessageDialog(this.master,"Resultado guardado correctamente en:\n"+sPath,"ÉXITO",JOptionPane.INFORMATION_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(this.master,"No tiene acceso al archivo porque\nestá siendo utilizado por otro proceso.","ERROR",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {}
    }

    //region Events
    @Override
    public void actionPerformed(ActionEvent ae){
        removeJSubnet(ae);
        if(ae.getSource()==bAdd) addJSubnet("",0);
        if(ae.getSource()==bGenerate) generateVLSM(); 
        if(ae.getSource()==miImportConfig) importConfig();
        if(ae.getSource()==miExportConfig) exportConfig();
        if(ae.getSource()==bSaveVLSM) saveResult();
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