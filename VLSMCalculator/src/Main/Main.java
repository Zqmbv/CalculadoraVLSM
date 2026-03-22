package Main;

import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import FrontEnd.UIGeneral;

public class Main{

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Calculadora VLSM");
        new UIGeneral(frame);

        // CONFIGURACION FRAME
        frame.setSize(1450,800);
        frame.setMinimumSize(new Dimension(1210,400));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }
}
