package BackEnd;

public class Subnet {
    private String name;
    private long hostNeeded;
    private int exponent;
    private long availableHosts;
    private int CIDR;
    private String[] binMask;
    private int[] decMask;
    private int[] wildcardMask;
    private int[] ipAddress;
    private int[] firstUsable;
    private int[] lastUsable;
    private int[] broadcast;

    public Subnet(String name, long hostNeeded){
        this.name = name;
        this.hostNeeded = hostNeeded;
        this.exponent = getExponent(this.hostNeeded);
        this.availableHosts = getAvailableHosts(this.hostNeeded);
        this.CIDR = getCIDR(this.exponent);
        this.binMask = getBinMask(this.exponent);
        this.decMask = getDecMask(this.binMask);
        this.wildcardMask = getWildcardMask(this.decMask);
    }

    public static int getExponent(long hostNeeded){
        return (int) (Math.ceil(Math.log(hostNeeded+2) / Math.log(2)));
    }

    public static long getAvailableHosts(long hostNeeded){
        return (long) Math.pow(2,getExponent(hostNeeded));
    }
    
    public static int getCIDR(int exp){
        return 32-exp;
    }

    public static String multiString(String str, int num){
        String newStr = "";
        for(int i=0; i<num; i++) newStr += str;
        return newStr;
    }

    public static String[] getBinMask(int exp){
        String stringMascara = multiString("1",getCIDR(exp))+multiString("0",exp);
        String binMask[] = {"","","",""};
        for(int bit=0; bit<32; bit++) binMask[bit/8] += stringMascara.charAt(bit);
        return binMask;
    }

    public static int[] getDecMask(String[] binMask){
        int decMask[] = new int[4];
        for(int i=0; i<decMask.length; i++) decMask[i] = Integer.parseInt(binMask[i],2);
        return decMask;
    }

    public static int[] getWildcardMask(int[] decMask){
        int wildcard[] = new int[4];
        for(int oct=0; oct<wildcard.length; oct++) wildcard[oct] = 255-decMask[oct];
        return wildcard;
    }

    public String getName() {
        return name;
    }

    public long getHost(){
        return this.hostNeeded;
    }
    
    public long getAvailableHosts(){
        return this.availableHosts;
    }

    public int[] getDecMask(){
        return this.decMask;
    }

    public int[] getDireccionRed(){
        return this.ipAddress;
    }

    public void setDireccionRed(int[] dr){
        this.ipAddress = dr;
    }

    public void setPrimeraUtil(int[] pu){
        this.firstUsable = pu;
    }

    public void setUltimaUtil(int[] uu){
        this.lastUsable = uu;
    }

    public void setBroadcast(int[] bc){
        this.broadcast = bc;
    }

    public String[] toStringTable(){
        return new String[] {this.name,
            String.valueOf(this.hostNeeded),
            String.valueOf(this.availableHosts),
            String.valueOf(this.CIDR),
            VLSM.arrayToString(this.decMask),
            VLSM.arrayToString(this.wildcardMask),
            VLSM.arrayToString(this.ipAddress),
            VLSM.arrayToString(this.firstUsable),
            VLSM.arrayToString(this.lastUsable),
            VLSM.arrayToString(this.broadcast)
        };
    }

    public String toString(){
        String data[] = toStringTable();
        return String.format("| %-32s | %10s | %10s | %2s | %-15s | %-16s | %-15s | %-15s | %-15s | %-15s |",
        data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9]);
    }
}
