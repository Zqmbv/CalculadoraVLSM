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
        UI.setBackground(cBotonBackground);
        UI.getContentPane().setBackground(cFrameBackground);

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

        setScrollPaneStyle(UI.spPanelInputCenter);
        setScrollPaneStyle(UI.spTable);

        JPanel lCorner = new JPanel();
        lCorner.setBackground(cFrameBackground);
        UI.spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER,lCorner);

        UI.tbInput.setTitleColor(cSubTitle);
        UI.tbOutput.setTitleColor(cSubTitle);

        UI.panelInput.setBackground(cPanelBackground);
        UI.panelOutput.setBackground(cPanelBackground);

        UI.tfInitialIP.setForeground(cTextUnfocused);
        UI.tfInitialIP.setBorder(BorderFactory.createLineBorder(cBorder));
        UI.tfInitialIP.setBackground(cTextFieldBackground);
        UI.tfInitialIP.setCaretColor(cText);

        UI.lPrefixSymbol.setForeground(cSubTitle);

        UI.tfInitialMask.setForeground(cTextUnfocused);
        UI.tfInitialMask.setBorder(BorderFactory.createLineBorder(cBorder));
        UI.tfInitialMask.setBackground(cTextFieldBackground);
        UI.tfInitialMask.setCaretColor(cText);

        UI.panelInputNorth.setOpaque(false);
        UI.panelInputCenter.setOpaque(true);
        UI.panelInputCenter.setBackground(cPanelBackground);
        UI.panelInputSouth.setOpaque(false);

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

        UI.bGenerate.setBackground(cBotonBackground);
        UI.bGenerate.setForeground(cText);
        UI.bGenerate.setBorder(BorderFactory.createEmptyBorder());
        UI.bGenerate.setFocusPainted(false);
        UI.bGenerate.setContentAreaFilled(false); 
        UI.bGenerate.setOpaque(true); 
        UI.bGenerate.addChangeListener(this);

        UI.bAdd.setBackground(cBotonBackground);
        UI.bAdd.setForeground(cText);
        UI.bAdd.setBorder(BorderFactory.createEmptyBorder());
        UI.bAdd.setFocusPainted(false);
        UI.bAdd.setContentAreaFilled(false); 
        UI.bAdd.setOpaque(true); 
        UI.bAdd.addChangeListener(this);

        UIManager.put("OptionPane.background", cFrameBackground);
        UIManager.put("OptionPane.messageForeground",cText);
        UIManager.put("Button.background",cBotonBackground);
        UIManager.put("Button.foreground",cText);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));           
        UIManager.put("Button.select", cPressedButtonBackground);                 
        UIManager.put("Panel.background", cFrameBackground);
    }

    //region Style - JScrollPane
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

    //region Style - JSubRed
    public static void setStyleJSubRed(JSubRed JSR){
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

    // region Style - JButton
    @Override
    public void stateChanged(ChangeEvent ce) {
        if(ce.getSource()== UI.bAdd){
            ButtonModel model = UI.bAdd.getModel();
            if (model.isPressed()) UI.bAdd.setBackground(UIStyle.cPressedButtonBackground); 
            else UI.bAdd.setBackground(UIStyle.cBotonBackground);
        }
        if(ce.getSource()== UI.bGenerate){
            ButtonModel model = UI.bGenerate.getModel();
            if (model.isPressed()) UI.bGenerate.setBackground(UIStyle.cPressedButtonBackground); 
            else UI.bGenerate.setBackground(UIStyle.cBotonBackground);
        }
    }
}
