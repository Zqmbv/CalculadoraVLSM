package BackEnd;

import java.util.ArrayList;

public class VLSM {
    private int[] initialIP;
    private int initialPrefix;
    private ArrayList<Subnet> subnets;

    public VLSM(int initialIP[] , int initialPrefix,  ArrayList<Subnet> JSubRedes){
        this.initialIP = initialIP.clone();
        this.initialPrefix = initialPrefix;
        this.subnets = JSubRedes;
        adjustMainIP();
        calculateNetworkAddresses();
    }

    public ArrayList<Subnet> getSubredes() {
        return subnets;
    }

    public static long[] getCriticalOctect(Subnet subnet){
        long availableHosts = subnet.getAvailableHosts();
        long octectData[] = {3,availableHosts}; // Por defecto en el último octeto

        while(octectData[1]>=256){
            octectData[1] /= 256;
            octectData[0]--;
        }
        return octectData;
    }

    public void adjustMainIP(){
        long criticalData[] = getCriticalOctect(this.subnets.get(0));
        int criticalOct = (int) criticalData[0];
        int value = (int) criticalData[1];

        int intDiv = (int) this.initialIP[criticalOct] / value; // MATH.FLOOR
        this.initialIP[criticalOct] = intDiv*(value);

        for(int oct=(int)criticalData[0]+1; oct<4; oct++){
            this.initialIP[oct] = 0;
        }
    }

    public static int[] IPJump(int[] ip, long offset){
        int newIP[] = ip.clone(), jump[] = new int[4];
        double temp = offset * 1.0 / Math.pow(256,3);
        for(int i=0; i<jump.length; i++){
            jump[i] = (int) temp;
            temp -= (int) temp;
            temp *= 256.0;
        }
        for(int i=jump.length-1; i>=0; i--){
            newIP[i] += jump[i];
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

    public void calculateNetworkAddresses(){ 
        for(int i=0; i<this.subnets.size(); i++){
            Subnet currentSubnet = this.subnets.get(i);
            int newDirIP[];
            if(i==0){
                newDirIP = this.initialIP.clone();
                currentSubnet.setDireccionRed(newDirIP);
            }
            else{
                Subnet subredAnterior = this.subnets.get(i-1);
                newDirIP = subredAnterior.getDireccionRed().clone();
                currentSubnet.setDireccionRed(IPJump(newDirIP,subredAnterior.getAvailableHosts()));
                newDirIP = currentSubnet.getDireccionRed().clone();
            }
            currentSubnet.setPrimeraUtil(IPJump(newDirIP, 1));
            currentSubnet.setUltimaUtil(IPJump(newDirIP,currentSubnet.getAvailableHosts()-2));
            currentSubnet.setBroadcast(IPJump(newDirIP,currentSubnet.getAvailableHosts()-1));
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
        for(Subnet subnet: this.subnets){
            str += subnet+"\n";
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