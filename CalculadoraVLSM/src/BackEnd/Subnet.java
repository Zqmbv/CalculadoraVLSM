package BackEnd;

public class Subnet {
    private String nombre;
    private int host;
    private int exponente;
    private int asignado;
    private int prefijo;
    private String[] mascaraBin;
    private int[] mascaraDec;
    private int[] wildcardMask;
    private int[] direccionRed;
    private int[] primeraUtil;
    private int[] ultimaUtil;
    private int[] broadcast;

    public Subnet(String nombre, int host){
        this.nombre = nombre;
        this.host = host;
        this.exponente = getExponente(this.host);
        this.asignado = getAsignado(this.host);
        this.prefijo = getPrefijo(this.exponente);
        this.mascaraBin = getMascaraBin(this.exponente);
        this.mascaraDec = getMascaraDec(this.mascaraBin);
        this.wildcardMask = getWildcardMask(this.mascaraDec);
    }

    public static int getExponente(int host){
        return (int) (Math.ceil(Math.log(host+2) / Math.log(2)));
    }

    public static int getAsignado(int host){
        return (int) Math.pow(2,getExponente(host));
    }
    
    public static int getPrefijo(int exp){
        return 32-exp;
    }

    public static String multiString(String str, int num){
        String newStr = "";
        for(int i=0; i<num; i++) newStr += str;
        return newStr;
    }

    public static String[] getMascaraBin(int exp){
        String stringMascara = multiString("1",getPrefijo(exp))+multiString("0",exp);
        String mascaraBin[] = {"","","",""};
        for(int bit=0; bit<32; bit++) mascaraBin[bit/8] += stringMascara.charAt(bit);
        return mascaraBin;
    }

    public static int[] getMascaraDec(String[] mascaraBin){
        int mascaraDec[] = new int[4];
        for(int i=0; i<mascaraDec.length; i++) mascaraDec[i] = Integer.parseInt(mascaraBin[i],2);
        return mascaraDec;
    }

    public static int[] getWildcardMask(int[] mascaraDec){
        int wildcard[] = new int[4];
        for(int oct=0; oct<wildcard.length; oct++) wildcard[oct] = 255-mascaraDec[oct];
        return wildcard;
    }

    public int getHost(){
        return this.host;
    }
    
    public int getAsignado(){
        return this.asignado;
    }

    public int[] getMascaraDec(){
        return this.mascaraDec;
    }

    public int[] getDireccionRed(){
        return this.direccionRed;
    }

    public void setDireccionRed(int[] dr){
        this.direccionRed = dr;
    }

    public void setPrimeraUtil(int[] pu){
        this.primeraUtil = pu;
    }

    public void setUltimaUtil(int[] uu){
        this.ultimaUtil = uu;
    }

    public void setBroadcast(int[] bc){
        this.broadcast = bc;
    }

    public String toString(){
        return String.format("| %-32s | %10d | %10d | %2d | %-15s | %-16s | %-15s | %-15s | %-15s | %-15s |",
        this.nombre,
        this.host,
        this.asignado,
        this.prefijo,
        VLSM.arrayToString(this.mascaraDec),
        VLSM.arrayToString(this.wildcardMask),
        VLSM.arrayToString(this.direccionRed),
        VLSM.arrayToString(this.primeraUtil),
        VLSM.arrayToString(this.ultimaUtil),
        VLSM.arrayToString(this.broadcast));
    }

    public String[] toStringTable(){
        return new String[] {this.nombre,
            String.valueOf(this.host),
            String.valueOf(this.asignado),
            String.valueOf(this.prefijo),
            VLSM.arrayToString(this.mascaraDec),
            VLSM.arrayToString(this.wildcardMask),
            VLSM.arrayToString(this.direccionRed),
            VLSM.arrayToString(this.primeraUtil),
            VLSM.arrayToString(this.ultimaUtil),
            VLSM.arrayToString(this.broadcast)
        };
    }
}
