package BackEnd;

import java.util.ArrayList;

public class VLSM {
    private int[] initialIP;
    private int initialPrefix;
    private ArrayList<Subnet> subnets;

    public VLSM(int initialIP[] , int initialPrefix,  ArrayList<Subnet> JSubRedes){
        this.initialIP = initialIP;
        this.initialPrefix = initialPrefix;
        this.subnets = JSubRedes;
        ajustarIPPrincipal();
        getDireccionesRed();
    }

    public ArrayList<Subnet> getSubredes() {
        return subnets;
    }

    public static long[] getOctetoCritico(Subnet SubRedes){
        long octetoCantidad[] = {3,SubRedes.getHostAvailable()};
        while(octetoCantidad[1]>=256){
            octetoCantidad[1] /= 256;
            octetoCantidad[0]--;
        }
        return octetoCantidad;
    }

    public void ajustarIPPrincipal(){
        long octetoCritico[] = getOctetoCritico(this.subnets.get(0));
        int division = (int) (this.initialIP[(int) octetoCritico[0]] / octetoCritico[1]);
        if(division != this.initialIP[(int) octetoCritico[0]]){
            this.initialIP[(int) octetoCritico[0]] = division*((int)octetoCritico[1]);
            for(int oct=(int)octetoCritico[0]+1; oct<4; oct++){
                this.initialIP[oct] = 0;
            }
        }
    }

    public static int[] saltosIP(int[] ip, long num){
        int newIP[] = ip.clone(), salto[] = new int[4];
        double temp = num * 1.0 / Math.pow(256,3);
        for(int i=0; i<salto.length; i++){
            salto[i] = (int) temp;
            temp -= (int) temp;
            temp *= 256.0;
        }
        for(int i=salto.length-1; i>=0; i--){
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
        if(newIP[0]>=256){return null;}
        return newIP;
    }

    public void getDireccionesRed(){ 
        for(int i=0; i<this.subnets.size(); i++){
            Subnet subredActual = this.subnets.get(i);
            int newDirIP[];
            if(i==0){
                newDirIP = this.initialIP.clone();
                subredActual.setDireccionRed(newDirIP);
            }
            else{
                Subnet subredAnterior = this.subnets.get(i-1);
                newDirIP = subredAnterior.getDireccionRed().clone();
                subredActual.setDireccionRed(saltosIP(newDirIP,subredAnterior.getHostAvailable()));
                newDirIP = subredActual.getDireccionRed().clone();
            }
            subredActual.setPrimeraUtil(saltosIP(newDirIP, 1));
            subredActual.setUltimaUtil(saltosIP(newDirIP,subredActual.getHostAvailable()-2));
            subredActual.setBroadcast(saltosIP(newDirIP,subredActual.getHostAvailable()-1));
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
        for(Subnet SubRedes: this.subnets){
            str += SubRedes+"\n";
        }
        return str;
    }

    public String toString(){
        return  "                                                                   .: IP INICIAL "+arrayToString(this.initialIP)+" /"+this.initialPrefix+" :.\n\n"+
                "+----------------------------------+------------+------------+----+-----------------+------------------+-----------------+-----------------+-----------------+-----------------+\n"+
                "|              SUBRED              |    HOST    |  ASIGNADO  | /  | MASCARA DE RED  | MASCARA WILDCARD |  DIRECCION RED  |  PRIMERA UTIL   |   ULTIMA UTIL   |    BROADCAST    |\n"+
                "+----------------------------------+------------+------------+----+-----------------+------------------+-----------------+-----------------+-----------------+-----------------+\n"+
                subredToString()+
                "+----------------------------------+------------+------------+----+-----------------+------------------+-----------------+-----------------+-----------------+-----------------+\n";
    }
}