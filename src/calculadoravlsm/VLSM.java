package calculadoravlsm;

import java.util.ArrayList;
import java.util.Comparator;

public class VLSM {
    private int[] initialIP;
    private int initialPrefix;
    private ArrayList<Subred> subnets;

    public VLSM(String inputIP, String inputPrefijo, ArrayList<JSubRed> JSubRedes){
        this.initialIP = getIPInicial(inputIP);
        this.initialPrefix = getPrefijoInicial(inputPrefijo);
        this.subnets = getSubredes(JSubRedes);
        if(this.subnets!=null && this.initialIP!=null){
            ajustarIPPrincipal();
            getDireccionesRed();
        }
    }

    public ArrayList<Subred> getSubredes() {
        return subnets;
    }

    public static int[] getIPInicial(String inputIP){
        int IP[] = new int[4];
        String stringIP[] = inputIP.trim().split("\\.");
        if(inputIP.trim().equals("")){
            ManejadorError.getError("La IP inicial esta vacía.");
            return null;
        }
        if(stringIP.length!=4){
            ManejadorError.getError("La IP debe contener 4 octetos en el siguiente formato: X.X.X.X");
            return null;
        }
        for(int i=0; i<4; i++){
            try {
                int oct = Integer.parseInt(stringIP[i]);
                if (oct<0||oct>255){
                    ManejadorError.getError("Los octetos deben estar conformados de números enteros entre 0 y 255 cada uno.");
                    return null;
                }
                IP[i] = oct;
            } catch (Exception e) {
                ManejadorError.getError("La IP es inválida");
                return null;
            }
        }
        return IP;
    } 

    public static int getPrefijoInicial(String input){
        int prefijo;
        if(input.trim().equals("")){
            ManejadorError.getError("La máscara inicial (prefijo) está vacía.");
            return -1;
        }
        try {
            prefijo = Integer.parseInt(input.trim());
            if(prefijo<0||prefijo>32){
                ManejadorError.getError("La máscara inicial (prefijo) debe estar entre 0 y 32.");
                return -1;
            }
        } catch (Exception e) {
            ManejadorError.getError("La máscara inicial (prefijo) debe ser un entero.");
            return -1;
        }
        return prefijo;
    }

    public static ArrayList<Subred> getSubredes(ArrayList<JSubRed> JSubRedes){
        ArrayList<Subred> SubRedes = new ArrayList<>();
        String sbNombre; int sbHost;
        if(JSubRedes.size()==0){
            ManejadorError.getError("Debe agregar una subred.");
            return null;
        } 
        for(int i=0; i<JSubRedes.size(); i++){
            sbNombre = JSubRedes.get(i).sbName.getText().trim();
            if(sbNombre.equals("")) {
                ManejadorError.getError("La subred ("+(i+1)+") no tiene nombre.");
                return null;
            }
            try {
                sbHost = Integer.parseInt(JSubRedes.get(i).sbHost.getText().trim());
                if(sbHost<=0) {
                    ManejadorError.getError("La subred ("+(i+1)+") tiene cantidad de host inválida.");
                    return null;
                } 
            } catch (Exception e) {
                ManejadorError.getError("La subred ("+(i+1)+") tiene cantidad de host inválida.");
                return null;
            }
            SubRedes.add(new Subred(sbNombre, sbHost));
        }   
        SubRedes.sort(Comparator.comparingInt(Subred::getHost).reversed());
        return SubRedes;
    }

    public static int[] getOctetoCritico(Subred SubRedes){
        int octetoCantidad[] = {3,SubRedes.getAsignado()};
        while(octetoCantidad[1]>=256){
            octetoCantidad[1] /= 256;
            octetoCantidad[0]--;
        }
        return octetoCantidad;
    }

    public void ajustarIPPrincipal(){
        int octetoCritico[] = getOctetoCritico(this.subnets.get(0));
        int division = (int) this.initialIP[octetoCritico[0]] / octetoCritico[1];
        if(division != this.initialIP[octetoCritico[0]]){
            this.initialIP[octetoCritico[0]] = division*octetoCritico[1];
            for(int oct=octetoCritico[0]+1; oct<4; oct++){
                this.initialIP[oct] = 0;
            }
        }
    }

    public static int[] saltosIP(int[] ip, long num){
        int newIP[] = ip.clone();
        int salto[] = new int[4];
        double temp = num * 1.0 / Math.pow(256,3);
        for(int i=0; i<4; i++){
            salto[i] = (int) temp;
            temp -= (int) temp;
            temp *= 256.0;
        }
        for(int i=3; i>=0; i--){
            newIP[i] += salto[i];
            if(newIP[i]>=256 && i!=0){
                newIP[i] %= 256;
                newIP[i-1]++;
            }
            if(newIP[i]<0 && i!=0){
                newIP[i] += 256;
                newIP[i-1]--;
            }
        }
        return newIP;
    }

    public void getDireccionesRed(){ 
        for(int i=0; i<this.subnets.size(); i++){
            Subred subredActual = this.subnets.get(i);
            int newDirIP[];
            if(i==0){
                newDirIP = this.initialIP.clone();
                subredActual.setDireccionRed(newDirIP);
            }
            else{
                Subred subredAnterior = this.subnets.get(i-1);
                newDirIP = subredAnterior.getDireccionRed().clone();
                subredActual.setDireccionRed(saltosIP(newDirIP,subredAnterior.getAsignado()));
                newDirIP = subredActual.getDireccionRed().clone();
            }
            subredActual.setPrimeraUtil(saltosIP(newDirIP, 1));
            subredActual.setUltimaUtil(saltosIP(newDirIP,subredActual.getAsignado()-2));
            subredActual.setBroadcast(saltosIP(newDirIP,subredActual.getAsignado()-1));
        }
    }

    public static String arrayToString(int[] input){
        if (input==null) return "null";
        String str = "";
        for(int i=0; i<input.length; i++){
            str += String.valueOf(input[i]);
            if(i<3) str += ".";
        }
        return str;
    }
    public static String arrayToString(String[] input){
        String str = "";
        for(int i=0; i<input.length; i++){
            str += input[i];
            if(i<3) str += ".";
        }
        return str;
    }

    public String subredToString(){
        if(this.subnets==null) return "null";
        String str = "";
        for(Subred SubRedes: this.subnets){
            str += SubRedes+"\n";
        }
        return str;
    }

    public String toString(){
        return  "IP inicial .......... "+arrayToString(this.initialIP)+"\n"+
                "Prefijo Inicial ..... "+this.initialPrefix+"\n"+
                String.format("| %-20s | %10s | %10s | %2s | %15s | %16s | %15s | %15s | %15s | %15s |\n",
                "SUBRED","HOST","ASIGNADO","/","MASCARA DE RED","MASCARA WILDCARD","DIRECCION RED","PRIMERA UTIL","ULTIMA UTIL","BROADCAST")+
                subredToString();

    }



}
