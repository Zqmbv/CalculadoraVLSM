package calculadoravlsm;

import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JTextField;

public class UIVerificator {
    static final int MAX_NAME_SUBNET_LENGTH = 32;

    //region IP
    public static int[] getInitialIP(JTextField tfInitialIP){
        if(tfInitialIP.getText().trim().equals("") || tfInitialIP.getForeground()==UIStyle.cTextUnfocused){
            UIGeneral.errorMessages.add("La IP inicial esta vacía.");
            return null;
        }
        
        String stringIP[] = tfInitialIP.getText().trim().split("\\.");
        if(stringIP.length!=4){
            UIGeneral.errorMessages.add("La IP debe contener 4 octetos en el siguiente formato: X.X.X.X");
            return null;
        }
        
        int IP[] = new int[4];
        for(int i=0; i<IP.length; i++){
            try {
                int oct = Integer.parseInt(stringIP[i]);
                if (oct<0||oct>255){
                    UIGeneral.errorMessages.add("Los octetos deben estar conformados de números enteros entre 0 y 255 cada uno.");
                    return null;
                }
                IP[i] = oct;
            } catch (Exception e) {
                UIGeneral.errorMessages.add("La IP es inválida");
                return null;
            }
        }
        return IP;
    } 

    //region Mask
    public static int getInitialMask(JTextField input){
        if(input.getText().trim().equals("") || input.getForeground()==UIStyle.cTextUnfocused){
            UIGeneral.errorMessages.add("La máscara inicial (prefijo) está vacía.");
            return -1;
        }

        int prefijo;
        try {
            prefijo = Integer.parseInt(input.getText().trim());
            if(prefijo<0||prefijo>32){
                UIGeneral.errorMessages.add("La máscara inicial (prefijo) debe ser entre 0 y 32.");
                return -1;
            }
        } catch (Exception e) {
            UIGeneral.errorMessages.add("La máscara inicial (prefijo) debe ser un entero.");
            return -1;
        }
        return prefijo;
    }

    //region Subnet
    public static ArrayList<Subnet> getSubnets(ArrayList<JSubnet> JSubnets){
        ArrayList<Subnet> subnets = new ArrayList<>();

        if(JSubnets.size()==0){
            UIGeneral.errorMessages.add("Debe agregar una subred.");
            return null;
        } 

        String sbName, sbNameTemp; 
        for(int i=0; i<JSubnets.size(); i++){
            sbName = JSubnets.get(i).sbName.getText().trim();
            if(sbName.length()>MAX_NAME_SUBNET_LENGTH){
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
                    UIGeneral.errorMessages.add("El nombre de la subred  \""+sbNameTemp+"\" está repetido.");
                    return null;
                }
            }
            
            int sbHost;
            try {
                sbHost = Integer.parseInt(JSubnets.get(i).sbHost.getText().trim());
                if(sbHost<=0) {
                    UIGeneral.errorMessages.add("La subred ("+(i+1)+") tiene cantidad de host inválida.");
                    return null;
                } 
            } catch (Exception e) {
                UIGeneral.errorMessages.add("La subred ("+(i+1)+") tiene cantidad de host inválida.");
                return null;
            }
            subnets.add(new Subnet(sbName, sbHost));
        }   
        subnets.sort(Comparator.comparingInt(Subnet::getHost).reversed());
        return subnets;
    }
}
