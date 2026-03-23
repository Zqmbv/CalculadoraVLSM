package FrontEnd;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.*;

public class UIStyle implements ChangeListener{

    UIGeneral UI;
    static final Color cTextUnfocused = new Color(180,180,180);
    static final Color cText = new Color(255,255,255);
    static final Color cFrameBackground = new Color(25,25,25);
    static final Color cPanelBackground = new Color(45,45,45);
    static final Color cSubTitle = new Color(220,220,220);
    static final Color cTextFieldBackground = new Color(65,65,65);
    static final Color cBotonBackground = new Color(45,45,195);
    static final Color cPressedButtonBackground = new Color(15,15,135);
    static final Color cPressedEraseBackground = new Color(115,5,5);
    static final Color cEraseBackground = new Color(175,25,25);
    static final Color cBorder = new Color(160,160,160);
    static final Color cScrollPaneBorder = new Color(100,100,100);
    static final Color cError = new Color(200,25,25);

    static final String nameFont = "Sans Serif";
    static final Font fPanelTitle = new Font(nameFont,Font.BOLD,16);
    static final Font fSubTitle = new Font(nameFont,Font.BOLD,12);
    static final Font fText = new Font(nameFont,Font.PLAIN,13);
    static final Font fTableText = new Font(nameFont,Font.PLAIN,11);
    static final Font fTableHeader = new Font(nameFont,Font.BOLD,11);

    //region Constructor
    public UIStyle(UIGeneral UI){
        this.UI = UI;
        UI.master.getContentPane().setBackground(cFrameBackground);

        UI.splitPane.setUI(new BasicSplitPaneUI() {
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
        
        JPanel lCorner = new JPanel();
        setPanelStyle(lCorner,cFrameBackground,true);
        UI.spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER,lCorner);
        
        UI.tbInput.setTitleColor(cSubTitle);
        UI.tbOutput.setTitleColor(cSubTitle);

        setLabelStyle(UI.lPrefixSymbol, cSubTitle, cSubTitle, false, fSubTitle);
        setLabelStyle(UI.lID, cSubTitle, cSubTitle, false, fSubTitle);
        setLabelStyle(UI.lNombre, cSubTitle, cSubTitle, false, fSubTitle);
        setLabelStyle(UI.lHost, cSubTitle, cSubTitle, false, fSubTitle);
        setLabelStyle(UI.lUser, cSubTitle, cSubTitle, false, fSubTitle);
        
        setPanelStyle(UI.panelInput,cPanelBackground,true);
        setPanelStyle(UI.panelOutput,cPanelBackground,true);
        setPanelStyle(UI.panelInputNorth,cPanelBackground,false);
        setPanelStyle(UI.panelInputCenter,cPanelBackground,true);
        setPanelStyle(UI.panelInputSouth,cPanelBackground,false);

        setScrollPaneStyle(UI.spPanelInputCenter, cTextFieldBackground,cFrameBackground);
        setScrollPaneStyle(UI.spTable, cTextFieldBackground,cFrameBackground);

        setTextFieldStyle(UI.tfInitialIP,cTextUnfocused,cBorder,cTextFieldBackground,cText,fText);
        setTextFieldStyle(UI.tfInitialMask,cTextUnfocused,cBorder,cTextFieldBackground,cText,fText);

        setButtonStyle(UI.bSaveVLSM,cText,cBotonBackground,null,false,false,true,fSubTitle,this);
        setButtonStyle(UI.bGenerate,cText,cBotonBackground,null,false,false,true,fSubTitle,this);
        setButtonStyle(UI.bAdd,cText,cBotonBackground,null,false,false,true,fSubTitle,this);
        
        UI.jtTable.getTableHeader().setBackground(cFrameBackground);
        UI.jtTable.getTableHeader().setForeground(cSubTitle); 
        UI.jtTable.getTableHeader().setBorder(BorderFactory.createLineBorder(cBorder));
        UI.jtTable.setBackground(cPanelBackground); 
        UI.jtTable.setForeground(cText); 
        UI.jtTable.setSelectionBackground(cBotonBackground); 
        UI.jtTable.setSelectionForeground(cText);
        UI.jtTable.setShowVerticalLines(false);
        UI.jtTable.setShowHorizontalLines(true);
        UI.jtTable.setGridColor(cScrollPaneBorder);

        setComponentStyle(UI.miImportConfig,cSubTitle,cPanelBackground,true,BorderFactory.createEmptyBorder(4, 5, 4, 5));
        setComponentStyle(UI.miExportConfig,cSubTitle,cPanelBackground,true,BorderFactory.createEmptyBorder(4, 5, 4, 5));
        setComponentStyle(UI.mFile,cSubTitle,cPanelBackground,false,BorderFactory.createEmptyBorder(4, 8, 4, 8));
        setComponentStyle(UI.menuBar,cFrameBackground,cFrameBackground,true,BorderFactory.createLineBorder(cBorder, 1));

        UIManager.put("OptionPane.background", cFrameBackground);
        UIManager.put("OptionPane.messageForeground",cText);
        UIManager.put("Button.background",cBotonBackground);
        UIManager.put("Button.foreground",cText);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));           
        UIManager.put("Button.select", cPressedButtonBackground);                 
        UIManager.put("Panel.background", cFrameBackground);

        setFont();
    }

    //region Font
    public void setFont(){
        UI.tbInput.setTitleFont(fPanelTitle);
        UI.tbOutput.setTitleFont(fPanelTitle);

        UI.jtTable.getTableHeader().setFont(fTableHeader);
        UI.jtTable.setFont(fTableText);
        
        UIManager.put("Button.font",fSubTitle);
        UIManager.put("OptionPane.messageFont",fText);
    }

    //region Auxiliary
    // JLABEL
    public static void setLabelStyle(JLabel JL, Color Foreground, Color Background ,Boolean Opaque, Font font){
        JL.setForeground(Foreground);
        JL.setBackground(Background);
        JL.setOpaque(Opaque);
        JL.setFont(font);
    }

    // JMENU / JITEM / JMENUBAR
    public void setComponentStyle(JComponent JC, Color Foreground, Color Background ,Boolean Opaque, Border BF){
        JC.setForeground(Foreground);
        JC.setBackground(Background);
        JC.setBorder(BF);
        JC.setOpaque(Opaque); 
    }

    // JPANEL
    public void setPanelStyle(JPanel JP, Color Background, Boolean Opaque){
        JP.setBackground(Background);
        JP.setOpaque(Opaque);
    }

    // JSCROLLPANEL
    public void setScrollPaneStyle(JScrollPane JSP, Color ThumbColor, Color TrackColor){
        JSP.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = ThumbColor;
                this.trackColor = TrackColor;   
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
        JSP.setBorder(BorderFactory.createLineBorder(cScrollPaneBorder));
    }

    // JTEXTFIELD
    public static void setTextFieldStyle(JTextField JTF, Color Foreground, Color Border, Color Background, Color Caret, Font Font){
        JTF.setForeground(Foreground);
        JTF.setBorder(BorderFactory.createLineBorder(Border));
        JTF.setBackground(Background);
        JTF.setCaretColor(Caret);
        JTF.setFont(Font);
    }

    // JSPINNER
    public static void setSpinnerStyle(JSpinner JS, Color Foreground, Color Border, Color BackGround1, Color BackGround2, Color Caret, Font font){
        JSpinner.NumberEditor NE = new JSpinner.NumberEditor(JS, "#");
        JS.setEditor(NE);
        JS.setBorder(BorderFactory.createLineBorder(Border,1));
        
        JTextField JTF = NE.getTextField();
        JTF.setOpaque(true); 
        JTF.setForeground(Foreground);
        JTF.setBackground(BackGround1);
        JTF.setCaretColor(Caret);
        JTF.setFont(font);

        for (Component C: JS.getComponents()) {
            if (C instanceof JButton) {
                JButton JB = (JButton) C;
                JB.setBackground(BackGround2);
                JB.setOpaque(true);
                JB.setBorder(BorderFactory.createLineBorder(Border, 1));
            }
        }

        JS.setBackground(BackGround1);
    }

    // JBUTTON
    public static void setButtonStyle(JButton JB, Color Foreground, Color Background, Color Border, Boolean FocusPainted, Boolean ContentAreaFilled, Boolean Opaque, Font Font, ChangeListener CL){
        JB.setForeground(Foreground);
        JB.setBackground(Background);
        if (Border==null) JB.setBorder(BorderFactory.createEmptyBorder());
        else JB.setBorder(BorderFactory.createLineBorder(Border));
        JB.setFocusPainted(FocusPainted);
        JB.setContentAreaFilled(ContentAreaFilled); 
        JB.setOpaque(Opaque); 
        JB.setFont(Font);
        JB.addChangeListener(CL);
    }

    public void setButtonPressedStyle(JButton JB,Color normalBackground, Color pressedBackground){
        ButtonModel model = JB.getModel();
        if (model.isPressed()) JB.setBackground(pressedBackground); 
        else JB.setBackground(normalBackground);
    }

    // JSUBNET
    public static void setStyleJSubnet(JSubnet JSR){
        setLabelStyle(JSR.ID,cSubTitle,cPanelBackground,false,fSubTitle);
        setTextFieldStyle(JSR.sbName,cText,cBorder,cTextFieldBackground,cText,fText);
        setSpinnerStyle(JSR.sbHost,cText,cBorder,cTextFieldBackground,cFrameBackground,cText,fText);
        setButtonStyle(JSR.bErase,cText,cEraseBackground,null,false,false,true,fSubTitle,null);
        JSR.bErase.addChangeListener(e -> {
            ButtonModel model = JSR.bErase.getModel();
            if (model.isPressed()) JSR.bErase.setBackground(cPressedEraseBackground); 
            else JSR.bErase.setBackground(cEraseBackground);
        });
    }

    //region Events
    @Override
    public void stateChanged(ChangeEvent ce) {
        if(ce.getSource()==UI.bAdd){
            setButtonPressedStyle(UI.bAdd,cBotonBackground,cPressedButtonBackground);
        }
        if(ce.getSource()==UI.bGenerate){
            setButtonPressedStyle(UI.bGenerate,cBotonBackground,cPressedButtonBackground);
        }
        if(ce.getSource()==UI.bSaveVLSM){
            setButtonPressedStyle(UI.bSaveVLSM,cBotonBackground,cPressedButtonBackground);
        }
    }
}
