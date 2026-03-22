package FrontEnd;

import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JTextField;
import BackEnd.Subnet;

public class UIVerificator {
    static final int MAX_SUBNET_NAME_LENGTH = 32;

    //region IP
    public static int[] getInitialIP(JTextField tfInitialIP){
        // ESTA VACIA (SIN TEXTO O QUE ESTE UNFOCUSED -> COLOR GRIS)
        String strInitialIP = tfInitialIP.getText().trim();
        if(strInitialIP.equals("") || tfInitialIP.getForeground()==UIStyle.cTextUnfocused){
            UIGeneral.errorMessages.add("La IP inicial esta vacía.");
            return null;
        }
        
        // SI NO CONTIENE 4 OCTETOS EN LA SIGUIENTE FORMA X.X.X.X
        String stringIP[] = strInitialIP.split("\\.");
        if(stringIP.length!=4){
            UIGeneral.errorMessages.add("La IP debe contener 4 octetos en el siguiente formato: X.X.X.X");
            return null;
        }
        
        // SI EL OCTETO ES MENOR A 0 O MAYOR A 255; NUMERO INVALIDO
        int IP[] = new int[4];
        for(int i=0; i<IP.length; i++){
            try {
                int octect = Integer.parseInt(stringIP[i]);
                if (octect<0||octect>255){
                    UIGeneral.errorMessages.add("Cada octeto debe valer entre 0 y 255.");
                    return null;
                }
                IP[i] = octect;
            } catch (Exception e) {
                UIGeneral.errorMessages.add("La IP es inválida");
                return null;
            }
        }
        return IP;
    } 

    //region Mask
    public static int getInitialMask(JTextField tfInitialMask){
        // ESTA VACIA (SIN TEXTO O QUE ESTE UNFOCUSED -> COLOR GRIS)
        String strInitialMask = tfInitialMask.getText().trim(); 
        if(strInitialMask.equals("") || tfInitialMask.getForeground()==UIStyle.cTextUnfocused){
            UIGeneral.errorMessages.add("La máscara inicial (prefijo) está vacía.");
            return -1;
        }

        int prefix;
        try { // SI LA MASCARA ES MENOR A 0 O MAYOR A 32; NUMERO INVALIDO
            prefix = Integer.parseInt(strInitialMask);
            if(prefix<0||prefix>32){
                UIGeneral.errorMessages.add("La máscara inicial (prefijo) debe ser entre 0 y 32.");
                return -1;
            }
        } catch (Exception e) {
            UIGeneral.errorMessages.add("La máscara inicial (prefijo) debe ser un entero.");
            return -1;
        }
        return prefix;
    }

    //region Subnet
    public static ArrayList<Subnet> getSubnets(ArrayList<JSubnet> JSubnets){
        // SI ESTA VACIO
        if(JSubnets.size()==0){
            UIGeneral.errorMessages.add("Debe agregar una subred.");
            return null;
        } 
        
        ArrayList<Subnet> subnets = new ArrayList<>();
        String sbName, sbNameTemp; 
        for(int i=0; i<JSubnets.size(); i++){

            sbName = JSubnets.get(i).sbName.getText().trim();
            if(sbName.length()>MAX_SUBNET_NAME_LENGTH){
                UIGeneral.errorMessages.add("El nombre de la subred es de máximo 32 carácteres.");
                return null;
            }

            if(sbName.equals("")) {
                UIGeneral.errorMessages.add("La subred ("+(i+1)+") no tiene nombre.");
                return null;
            }
            
            for(int j=0; j<JSubnets.size(); j++){
                sbNameTemp = JSubnets.get(j).sbName.getText().trim();
                if(i==j) continue;
                if(sbName.equalsIgnoreCase(sbNameTemp)){
                    UIGeneral.errorMessages.add("Las subredes ["+(i+1)+"] "+sbName+" y ["+(j+1)+"] "+sbNameTemp+"\ntienen el nombre repetido.");
                    return null;
                }
            }
            
            long sbHost;
            try {
                sbHost = ((Number) JSubnets.get(i).sbHost.getValue()).longValue();
                if(sbHost<=0) {
                    UIGeneral.errorMessages.add("La subred ["+(i+1)+"] "+sbName+" debe tener mínimo 1 host.");
                    return null;
                } 
                if(sbHost>16777214L) {
                    UIGeneral.errorMessages.add("La subred ["+(i+1)+"] "+sbName+" debe tener máximo 16777214 host.");
                    return null;
                } 
            } catch (Exception e) {
                UIGeneral.errorMessages.add("La subred ["+(i+1)+"] "+sbName+" tiene cantidad de host inválida.");
                return null;
            }
            subnets.add(new Subnet(sbName, sbHost));
        }   
        // ORDENA LAS SUBREDES POR SU TAMAÑO EN HOST (DESCENDENTE)
        subnets.sort(Comparator.comparingLong(Subnet::getHost).reversed());
        return subnets;
    }
}
