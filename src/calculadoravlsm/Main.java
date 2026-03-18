package calculadoravlsm;

import java.io.IOException;
import javax.swing.JFrame;

public class Main{

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Calculadora VLSM");
        new Interfaz(frame);

        // CONFIGURACION FRAME
        frame.setSize(1450,800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }
    
}
