package calculadoravlsm;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.*;

public class UIStyle implements ChangeListener{

    //region Attributes
    Interfaz UI;
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

    //region Style - UI
    public UIStyle(Interfaz UI){
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
        lCorner.setBackground(cFrameBackground);
        UI.spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER,lCorner);
        
        UI.tbInput.setTitleColor(cSubTitle);
        UI.tbOutput.setTitleColor(cSubTitle);
        
        UI.panelInput.setBackground(cPanelBackground);
        UI.panelOutput.setBackground(cPanelBackground);

        UI.panelInputNorth.setOpaque(false);
        UI.panelInputCenter.setOpaque(true);
        UI.panelInputCenter.setBackground(cPanelBackground);
        UI.panelInputSouth.setOpaque(false);

        UI.lPrefixSymbol.setForeground(cSubTitle);
        
        setScrollPaneStyle(UI.spPanelInputCenter, cTextFieldBackground,cFrameBackground);
        setScrollPaneStyle(UI.spTable, cTextFieldBackground,cFrameBackground);

        setTextFieldStyle(UI.tfInitialIP,cTextUnfocused,cBorder,cTextFieldBackground,cText);
        setTextFieldStyle(UI.tfInitialMask,cTextUnfocused,cBorder,cTextFieldBackground,cText);

        setButtonStyle(UI.bGenerate,cText,cBotonBackground,null,false,false,true,this);
        setButtonStyle(UI.bAdd,cText,cBotonBackground,null,false,false,true,this);
        
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

        UI.miImport.setBackground(cPanelBackground);
        UI.miImport.setForeground(cSubTitle);
        UI.miImport.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));

        UI.mFile.setForeground(cSubTitle);
        UI.mFile.setBackground(cFrameBackground);
        UI.mFile.setOpaque(true);
        UI.mFile.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        UI.menuBar.setBackground(cFrameBackground);
        UI.menuBar.setForeground(cFrameBackground);
        UI.menuBar.setBorder(BorderFactory.createLineBorder(cBorder, 1));

        UIManager.put("OptionPane.background", cFrameBackground);
        UIManager.put("OptionPane.messageForeground",cText);
        UIManager.put("Button.background",cBotonBackground);
        UIManager.put("Button.foreground",cText);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));           
        UIManager.put("Button.select", cPressedButtonBackground);                 
        UIManager.put("Panel.background", cFrameBackground);
    }

    //region Style - JScrollPane
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

    //region Style - JTextField
    public static void setTextFieldStyle(JTextField JFT, Color Foreground, Color Border, Color Background, Color Caret){
        JFT.setForeground(Foreground);
        JFT.setBorder(BorderFactory.createLineBorder(Border));
        JFT.setBackground(Background);
        JFT.setCaretColor(Caret);
    }
    
    // region Style - JButton
    public static void setButtonStyle(JButton JB, Color Foreground, Color Background, Color Border, Boolean FocusPainted, Boolean ContentAreaFilled, Boolean Opaque, ChangeListener CL){
        JB.setForeground(Foreground);
        JB.setBackground(Background);
        if (Border==null) JB.setBorder(BorderFactory.createEmptyBorder());
        else JB.setBorder(BorderFactory.createLineBorder(Border));
        JB.setFocusPainted(FocusPainted);
        JB.setContentAreaFilled(ContentAreaFilled); 
        JB.setOpaque(Opaque); 
        JB.addChangeListener(CL);
    }

    //region Style - JSubRed
    public static void setStyleJSubRed(JSubRed JSR){
        setTextFieldStyle(JSR.sbName,cText,cBorder,cTextFieldBackground,cText);
        setTextFieldStyle(JSR.sbHost,cText,cBorder,cTextFieldBackground,cText);
        setButtonStyle(JSR.bErase,cText,cEraseBackground,null,false,false,true,null);
        JSR.bErase.addChangeListener(e -> {
            ButtonModel model = JSR.bErase.getModel();
            if (model.isPressed()) JSR.bErase.setBackground(cPressedEraseBackground); 
            else JSR.bErase.setBackground(cEraseBackground);
        });
    }


    public void setButtonPressedStyle(JButton JB,Color normalBackground, Color pressedBackground){
        ButtonModel model = JB.getModel();
        if (model.isPressed()) JB.setBackground(pressedBackground); 
        else JB.setBackground(normalBackground);
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if(ce.getSource()==UI.bAdd){
            setButtonPressedStyle(UI.bAdd,cBotonBackground,cPressedButtonBackground);
        }
        if(ce.getSource()==UI.bGenerate){
            setButtonPressedStyle(UI.bGenerate,cBotonBackground,cPressedButtonBackground);
        }
    }
}
