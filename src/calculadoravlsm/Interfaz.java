
package calculadoravlsm;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

//region Class JSubRed
// Agrupar los JComponentes para cada Subred en el panel
class JSubRed {
    public JLabel lID;
    public JTextField sbName;
    public JTextField sbHost;
    public JButton bErase;

    public JSubRed(String sbName, String sbHost){
        this.lID = new JLabel();
        this.sbName = new JTextField(sbName);
        this.sbHost = new JTextField(sbHost);
        this.bErase = new JButton("X");
    }
}

public class Interfaz extends JFrame implements ActionListener,FocusListener,ChangeListener{
    //region Components
    static ArrayList<String> Errores = new ArrayList<>();
    ArrayList<JSubRed> JSubRedes = new ArrayList<>();

    String nameFont = "Sans Serif";

    Font fPanelTitle = new Font(nameFont,Font.BOLD,16);
    Font fSubTitle = new Font(nameFont,Font.BOLD,12);
    Font fText = new Font(nameFont,Font.PLAIN,13);
    Font fTableText = new Font(nameFont,Font.PLAIN,11);
    Font fTableHeader = new Font(nameFont,Font.BOLD,11);

    Color cFrameBackground = new Color(25,25,25);
    Color cPanelBackground = new Color(45,45,45);
    Color cSubTitle = new Color(220,220,220);
    Color cTextFieldBackground = new Color(65,65,65);
    Color cTextUnfocused = new Color(180,180,180);
    Color cText = new Color(255,255,255);
    Color cBotonBackground = new Color(45,45,195);
    Color cPressedButtonBackground = new Color(15,15,135);
    Color cPressedEraseBackground = new Color(115,5,5);
    Color cEraseBackground = new Color(175,25,25);
    Color cBorder = new Color(160,160,160);
    Color cScrollPaneBorder = new Color(100,100,100);

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
    public Interfaz(){
        // Deben estar activadas las 4 funciones para que la interfaz quede bonita.
        setPosition();                          // Colocar los componentes con GridLayout.
        setStyle();                             // Agregarle un tema personalizado.
        setConfigComponents();                  // Ajustar orientación de componentes y otras funcionalidades.
        setFont();                              // Agregarle la tipografía.

        // CONFIGURACION FRAME
        setTitle("Calculadora VLSM");
        setSize(1450,800);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //region Position
    public void setPosition(){
        setLayout(GBL);

        mFile.add(miExport);
        mFile.add(miImport);
        menuBar.add(mFile);

        GBC.fill = GridBagConstraints.BOTH; GBC.anchor = GridBagConstraints.NORTH;

        // PANEL INPUT & OUTPUT DINAMICO
        GBC.insets = new Insets(15, 20, 20, 20);
        GBC.weightx = 1; GBC.weighty = 1;
        GBC.gridx = 0; GBC.gridy = 1; add(splitPane, GBC);

        // MENU BAR
        GBC.weightx = 0; GBC.weighty = 0;
        GBC.insets = new Insets(0, 0, 0, 0);
        GBC.gridx = 0; GBC.gridy = 0; GBC.gridwidth = 2; add(menuBar, GBC);

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

    //region Style
    public void setScrollPaneStyle(JScrollPane JSC){
        JSC.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = cTextFieldBackground;
                this.trackColor = cFrameBackground;   
            }
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x+2, thumbBounds.y+2, thumbBounds.width-4, thumbBounds.height-4, 0, 0);
                g2.dispose();
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
        JSC.setBorder(BorderFactory.createLineBorder(cScrollPaneBorder));
    }

    public void setStyleJSubRed(JSubRed JSR){
        JSR.lID.setForeground(cSubTitle);

        JSR.bErase.setForeground(cText);
        JSR.bErase.setBackground(cEraseBackground);
        JSR.bErase.setBorder(BorderFactory.createEmptyBorder());
        JSR.bErase.setFocusPainted(false);
        JSR.bErase.setContentAreaFilled(false); 
        JSR.bErase.setOpaque(true); 
        JSR.bErase.addChangeListener(e -> {
            ButtonModel model = JSR.bErase.getModel();
            if (model.isPressed()) JSR.bErase.setBackground(cPressedEraseBackground); 
            else JSR.bErase.setBackground(cEraseBackground);
        });

        JSR.sbName.setForeground(cText);
        JSR.sbName.setBorder(BorderFactory.createLineBorder(cBorder));
        JSR.sbName.setBackground(cTextFieldBackground);
        JSR.sbName.setCaretColor(cText);

        JSR.sbHost.setForeground(cText);
        JSR.sbHost.setBorder(BorderFactory.createLineBorder(cBorder));
        JSR.sbHost.setBackground(cTextFieldBackground);
        JSR.sbHost.setCaretColor(cText);
    }

    public void setStyle(){
        this.setBackground(cBotonBackground);
        this.getContentPane().setBackground(cFrameBackground);

        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(cFrameBackground); g.fillRect(0, 0, getWidth(), getHeight());
                        g.setColor(cSubTitle); 
                        g.fillRect(8, getHeight()/3, 1, getHeight()/3);
                        g.fillRect(4, getHeight()/3, 1, getHeight()/3);
                    }
                };
            }
        });

        setScrollPaneStyle(spPanelInputCenter);
        setScrollPaneStyle(spTable);

        JPanel lCorner = new JPanel();
        lCorner.setBackground(cFrameBackground);
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER,lCorner);

        tbInput.setTitleColor(cSubTitle);
        tbOutput.setTitleColor(cSubTitle);

        panelInput.setBackground(cPanelBackground);
        panelOutput.setBackground(cPanelBackground);

        tfInitialIP.setForeground(cTextUnfocused);
        tfInitialIP.setBorder(BorderFactory.createLineBorder(cBorder));
        tfInitialIP.setBackground(cTextFieldBackground);
        tfInitialIP.setCaretColor(cText);

        lPrefixSymbol.setForeground(cSubTitle);

        tfInitialMask.setForeground(cTextUnfocused);
        tfInitialMask.setBorder(BorderFactory.createLineBorder(cBorder));
        tfInitialMask.setBackground(cTextFieldBackground);
        tfInitialMask.setCaretColor(cText);

        panelInputNorth.setOpaque(false);
        panelInputCenter.setOpaque(true);
        panelInputCenter.setBackground(cPanelBackground);
        panelInputSouth.setOpaque(false);

        jtTable.getTableHeader().setBackground(cFrameBackground);
        jtTable.getTableHeader().setForeground(cSubTitle); 
        jtTable.getTableHeader().setBorder(BorderFactory.createLineBorder(cBorder));
        jtTable.setBackground(cPanelBackground); 
        jtTable.setForeground(cText); 
        jtTable.setSelectionBackground(cBotonBackground); 
        jtTable.setSelectionForeground(cText);
        jtTable.setShowVerticalLines(false);
        jtTable.setShowHorizontalLines(true);
        jtTable.setGridColor(cScrollPaneBorder);

        miImport.setBackground(cPanelBackground);
        miImport.setForeground(cSubTitle);
        miImport.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));

        mFile.setForeground(cSubTitle);
        mFile.setBackground(cFrameBackground);
        mFile.setOpaque(true);
        mFile.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        menuBar.setBackground(cFrameBackground);
        menuBar.setForeground(cFrameBackground);
        menuBar.setBorder(BorderFactory.createLineBorder(cBorder, 1));

        bGenerate.setBackground(cBotonBackground);
        bGenerate.setForeground(cText);
        bGenerate.setBorder(BorderFactory.createEmptyBorder());
        bGenerate.setFocusPainted(false);
        bGenerate.setContentAreaFilled(false); 
        bGenerate.setOpaque(true); 
        bGenerate.addChangeListener(this);

        bAdd.setBackground(cBotonBackground);
        bAdd.setForeground(cText);
        bAdd.setBorder(BorderFactory.createEmptyBorder());
        bAdd.setFocusPainted(false);
        bAdd.setContentAreaFilled(false); 
        bAdd.setOpaque(true); 
        bAdd.addChangeListener(this);

        UIManager.put("OptionPane.background", cFrameBackground);
        UIManager.put("OptionPane.messageForeground",cText);
        UIManager.put("Button.background",cBotonBackground);
        UIManager.put("Button.foreground",cText);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));           
        UIManager.put("Button.select", cPressedButtonBackground);                 
        UIManager.put("Panel.background", cFrameBackground);
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
            currentJSB.lID.setText(""+(i+1)+"");

            // Posicion
            if(i==0) GBC.insets = new Insets(10, 5, 5, 5);
            else GBC.insets = new Insets(5, 5, 5, 5);
            GBC.fill = GridBagConstraints.HORIZONTAL; GBC.anchor = GridBagConstraints.CENTER;
            GBC.gridwidth = 1; GBC.gridheight = 1;
            GBC.ipadx = 0; GBC.ipady = 8; GBC.weighty = 0;

            GBC.gridx = 0; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.lID,GBC);
            GBC.gridx = 1; GBC.gridy = i; GBC.weightx = 1; panelInputCenter.add(currentJSB.sbName,GBC);
            GBC.gridx = 2; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.sbHost,GBC);
            GBC.gridx = 3; GBC.gridy = i; GBC.weightx = 0; panelInputCenter.add(currentJSB.bErase,GBC);
            
            // Estilo
            setStyleJSubRed(currentJSB);

            // ConfigComponent
            currentJSB.lID.setPreferredSize(dimLabelNum);
            currentJSB.lID.setHorizontalAlignment(JLabel.CENTER);

            currentJSB.sbName.setPreferredSize(dimSubRed);
            currentJSB.sbName.setBorder(BorderFactory.createCompoundBorder(currentJSB.sbName.getBorder(),BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        
            currentJSB.sbHost.setPreferredSize(dimHost);
            currentJSB.sbHost.setBorder(BorderFactory.createCompoundBorder(currentJSB.sbHost.getBorder(),BorderFactory.createEmptyBorder(2, 5, 2, 5)));
            
            currentJSB.bErase.setPreferredSize(dimErase);
            currentJSB.bErase.addActionListener(this);

            // Tipografia
            currentJSB.sbName.setFont(fText);
            currentJSB.lID.setFont(fText);
            currentJSB.lID.setFont(fSubTitle);
            currentJSB.bErase.setFont(fSubTitle);
        }
        GBC.weightx = 1; GBC.weighty = 1; 
        GBC.gridwidth = 4;
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
            if (tfInitialIP.getForeground()==cTextUnfocused) sIPInicial = "";

            String sMascaraInicial = tfInitialMask.getText().trim();
            if (tfInitialMask.getForeground()==cTextUnfocused) sMascaraInicial = "";

            VLSM myVLSM = new VLSM(sIPInicial, sMascaraInicial, JSubRedes);
            System.out.println(myVLSM);
            if(Errores.size()!=0){
                JOptionPane.showMessageDialog(this,Errores.get(0),"ERROR",JOptionPane.ERROR_MESSAGE);
                dtmModel.setRowCount(0);
                return;
            }
            else{
                dtmModel.setRowCount(0);
                for(int i=0; i<myVLSM.getSubredes().size(); i++){
                    dtmModel.addRow(myVLSM.getSubredes().get(i).toStringTable());
                }
                JOptionPane.showMessageDialog(this,"VLSM realizado correctamente.","ÉXITO",JOptionPane.INFORMATION_MESSAGE);
            }
            panelOutput.revalidate();
            panelOutput.repaint();
        }

        if(ae.getSource()==miImport){
            FileDialog FD = new FileDialog(this, "Abrir Configuración VLSM", FileDialog.LOAD);
            FD.setFile("*.txt;*.csv"); // Filtrar archivos TXT o CSV
            FD.setVisible(true);
            
            if(FD.getFile()==null){
                return;
            }

            String sFile = FD.getFile();
            if(!sFile.endsWith(".txt") && !sFile.endsWith(".csv")){
                JOptionPane.showMessageDialog(this, "Formato de archivo inválido.");
                return;
            }

            String sPath = FD.getDirectory()+sFile;
            try {
                ArrayList<ArrayList<String>> sVLSM = ManejadorTexto.readFile(sPath);
                tfInitialIP.setText(sVLSM.get(0).get(0)); tfInitialIP.setForeground(cText);
                tfInitialMask.setText(sVLSM.get(0).get(1)); tfInitialMask.setForeground(cText);
                panelInputCenter.removeAll(); JSubRedes.clear();
                for(ArrayList<String> sSubRed: sVLSM) JSubRedes.add(new JSubRed(sSubRed.get(0), sSubRed.get(1)));
                updatePanelJSB();
            } catch (IOException IOe) {
                JOptionPane.showMessageDialog(this,"Archivo inválido","ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
        if(ae.getSource()==miExport){
            // LocalDateTime LDT = LocalDateTime.now();
            // DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            // String sDateTime = LDT.format(DTF);
            // FileDialog FD = new FileDialog(this, "Exportar Reporte VLMS PDF", FileDialog.SAVE);
            // FD.setFile("Reporte_VLSM_"+sDateTime+".pdf");
            // FD.setVisible(true);

            // if(FD.getFile()==null){
            //     return;
            // }
        }
    }

    @Override
    public void focusGained(FocusEvent fe) {
        if(fe.getComponent()==tfInitialIP && tfInitialIP.getText().equals(defaultInformation[0])){
            tfInitialIP.setText("");
            tfInitialIP.setForeground(cText);
        }
        if(fe.getComponent()==tfInitialMask && tfInitialMask.getText().equals(defaultInformation[1])){
            tfInitialMask.setText("");
            tfInitialMask.setForeground(cText);
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
        if(fe.getComponent()==tfInitialIP && tfInitialIP.getText().equals("")){
            tfInitialIP.setText(defaultInformation[0]);
            tfInitialIP.setForeground(cTextUnfocused);
        }
        if(fe.getComponent()==tfInitialMask && tfInitialMask.getText().equals("")){
            tfInitialMask.setText(defaultInformation[1]);
            tfInitialMask.setForeground(cTextUnfocused);
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if(ce.getSource()==bAdd){
            ButtonModel model = bAdd.getModel();
            if (model.isPressed()) bAdd.setBackground(cPressedButtonBackground); 
            else bAdd.setBackground(cBotonBackground);
        }
        if(ce.getSource()==bGenerate){
            ButtonModel model = bGenerate.getModel();
            if (model.isPressed()) bGenerate.setBackground(cPressedButtonBackground); 
            else bGenerate.setBackground(cBotonBackground);
        }
    }
}