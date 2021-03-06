import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class rsa {

    public rsa(){
    }
    
    public void encrypt(String str, String fileWithKey) throws IOException{
        BigInteger[] key = readKeyFile(fileWithKey);
        System.out.println(key[0] + "\n" + key[1] + "\n");
        String st = ASCIItoHEX(str);
        System.out.println(st);
        BigInteger bg = new BigInteger(st, 16);
        BigInteger res = bg.modPow(key[0], key[1]);
        System.out.println(res); 
        byte[] b = res.toByteArray();
        String encryptedMessage = new String(b);
        System.out.println(ASCIItoHEX(encryptedMessage));
    }
    
    public void decrypt(String encryptedMessage, String fileWithKey) throws IOException{
        BigInteger[] key = readKeyFile(fileWithKey);
        String st = ASCIItoHEX(encryptedMessage);
        System.out.println(st);
        BigInteger bg = new BigInteger(st, 16);
        BigInteger res = bg.modPow(key[0], key[1]);
        System.out.println(res);
        byte[] bytes = res.toByteArray();
        String decryptedMessage = new String(bytes);
        System.out.println(ASCIItoHEX(decryptedMessage));
    }

    public static String unHex(String arg) {        
        String ascii = "";
        for (int i = 0; i < arg.length(); i += 2) {
            String part = arg.substring(i, i + 2);
            char ch = (char)Integer.parseInt(part, 16);
            ascii = ascii + ch;
        }
 
        return ascii;
    }

    public static String ASCIItoHEX(String ascii) {
       
        String hex = "";
        for (int i = 0; i < ascii.length(); i++) {
            char ch = ascii.charAt(i);
            int in = (int)ch;
            String part = Integer.toHexString(in);
            hex += part;
        }
        return hex;
    }

    public void KeyGen() throws IOException {
        BigInteger firstPrimeNum = getPrime();
        BigInteger secondPrimeNum = getPrime();
        System.out.println(firstPrimeNum + "\n" + secondPrimeNum + "\n");
        BigInteger n = firstPrimeNum.multiply(secondPrimeNum); // ???????????????????? ????????????
        BigInteger phi = firstPrimeNum.subtract(BigInteger.ONE).multiply(secondPrimeNum.subtract(BigInteger.ONE)); // ?????????????? ????????????
        System.out.println(phi);
        BigInteger e = BigInteger.valueOf(65537);
        BigInteger d = extended_gcd(e, phi);
        while (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(phi);
        }
        BigInteger[] pbKey = new BigInteger[] { e, n };
        BigInteger[] prKey = new BigInteger[] { d, n };

        writeKeyToFile("pbkey", pbKey);
        writeKeyToFile("prkey", prKey);
    }

    private static void writeKeyToFile(String fileName, BigInteger[] key) throws IOException {
        File file = new File("/Users/21shish/Desktop/" + fileName);
        FileWriter fw = new FileWriter(file);
        fw.write(toString(key, 0));
        fw.write(toString(key, 1));
        fw.close();
    }

    private static String toString(BigInteger[] key) {
        return key[0] + "\n" + key[1];
    }

    private static String read(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName)).reduce("", String::concat);
    }

    public static BigInteger[] readKeyFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String fString = "";
        String sString = "";
        fString = br.readLine();
        sString = br.readLine();
        br.close();
        return new BigInteger[] { new BigInteger(fString), new BigInteger(sString) };
    }

    public void signature(String fileName, String FileWithKey) throws IOException {
        StringBuilder s = new StringBuilder();
        String data = read(fileName);
        BigInteger[] key = readKeyFile(FileWithKey);
        char[] value = data.toCharArray();
        for (int i = 0; i < value.length; i++) {
            String num = String.valueOf((long) value[i]);
            BigInteger digit = new BigInteger(num);
            BigInteger res = digit.modPow(key[0], key[1]);
            long codeRes = res.longValue();
            String hex = Long.toHexString(codeRes).toUpperCase();
            if (hex.length() < 16) hex = "0" + hex;
            s.append(hex);
            //System.out.println(hex);
        }
        FileWriter out = new FileWriter("/Users/21shish/Desktop/signature");
        out.write(s.toString());
        out.close();
    }


    public boolean verify( String FileWithKey, String sign) throws IOException, ArrayIndexOutOfBoundsException {
        StringBuilder output = new StringBuilder();
        String signature = read(sign);
        BigInteger[] key = readKeyFile(FileWithKey);
        char[] value = signature.toCharArray();
        for (int i = 0; i < value.length; i+=16){
            String buf = "";
            for (int j = i; j < 16+i; j++){
                buf += value[j];
            }
            BigInteger bi = new BigInteger(buf, 16);
            bi.modPow(key[0], key[1]);
            String s = bi.toString(16).toUpperCase();
            if (s.length() < 16) s = "0" + s;
            output.append(s);
            //System.out.println(s);
        }
        String s = output.toString();
        
        return s.equals(signature);
    }

    public static void readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String fString = "";
        String sString = "";
        fString = br.readLine();
        sString = br.readLine();
        BigInteger[] bi = new BigInteger[]{new BigInteger(fString), new BigInteger(sString)};
        System.out.println(toString(bi));
        br.close();
    }

    public static void getKey() throws IOException {
        BigInteger firstPrimeNum = getPrime();
        BigInteger secondPrimeNum = getPrime();
        BigInteger n = firstPrimeNum.multiply(secondPrimeNum); // ???????????????????? ????????????
        BigInteger phi = firstPrimeNum.subtract(BigInteger.ONE).multiply(secondPrimeNum.subtract(BigInteger.ONE)); // ?????????????? ????????????
        BigInteger e = BigInteger.valueOf(65537);
        //BigInteger d = e.modInverse(n);
        BigInteger d = extended_gcd(e, phi);
        while(d.compareTo(BigInteger.ZERO) < 0){
            d = d.add(phi);
        }
        BigInteger[] pbKey = new BigInteger[]{e,n};
        BigInteger[] prKey = new BigInteger[]{d,n};
       
        File prkey = new File("/Users/21shish/Desktop/prkey");
        File pbkey = new File("/Users/21shish/Desktop/pbkey");
        FileWriter fw = new FileWriter(prkey);
        FileWriter fw2 = new FileWriter(pbkey);
        fw.write(toString(prKey,0));
        fw.write(toString(prKey,1));
        fw2.write(toString(pbKey, 0));
        fw2.write(toString(pbKey, 1));

        fw.close();
        fw2.close();
    }

    private static String toString(BigInteger[] key, int i) {
        return key[i] + "\n";
    }

    public static BigInteger extended_gcd(BigInteger a, BigInteger b) {
        BigInteger x = BigInteger.ZERO;
        BigInteger y = BigInteger.ONE;
        BigInteger lastx = BigInteger.ONE;
        BigInteger lasty = BigInteger.ZERO; 

        while(b.compareTo(BigInteger.ZERO) != 0){
            BigInteger q = a.divide(b);
            BigInteger c = a.mod(b);
            a = b;
            b = c;
            c = x;
            x = lastx.subtract(q.multiply(x));
            lastx = c;
            c = y;
            y = lasty.subtract(q.multiply(y));
            lasty = c;
        }
        return lastx;
    }
    public static BigInteger getPrime() {
        BigInteger maxLimit = new BigInteger("5000000000000");
        BigInteger minLimit = new BigInteger("25000000000");
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        SecureRandom randNum = new SecureRandom();
        int len = maxLimit.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        if (res.compareTo(minLimit) < 0)
            res = res.add(minLimit);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(minLimit);
            //System.out.println("The random BigInteger = "+res);
        BigInteger bi = BigInteger.probablePrime(1024, randNum);
        if (isPrime(bi) == false)
            getPrime(); 
        return bi;
    }
    public static boolean isPrime(BigInteger n, int precision) {
 
        if (n.compareTo(new BigInteger("341550071728321")) >= 0) {
            return n.isProbablePrime(precision);
        }
 
        int intN = n.intValue();
        if (intN == 1 || intN == 4 || intN == 6 || intN == 8) return false;
        if (intN == 2 || intN == 3 || intN == 5 || intN == 7) return true;
 
        int[] primesToTest = getPrimesToTest(n);
        if (n.equals(new BigInteger("3215031751"))) {
            return false;
        }
        BigInteger d = n.subtract(BigInteger.ONE);
        BigInteger s = BigInteger.ZERO;
        while (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            d = d.shiftRight(1);
            s = s.add(BigInteger.ONE);
        }
        for (int a : primesToTest) {
            if (try_composite(a, d, n, s)) {
                return false;
            }
        }
        return true;
    }
    public static boolean isPrime(BigInteger n) {
        return isPrime(n, 100);
    }
    private static int[] getPrimesToTest(BigInteger n) {
        if (n.compareTo(new BigInteger("3474749660383")) >= 0) {
            return new int[]{2, 3, 5, 7, 11, 13, 17};
        }
        if (n.compareTo(new BigInteger("2152302898747")) >= 0) {
            return new int[]{2, 3, 5, 7, 11, 13};
        }
        if (n.compareTo(new BigInteger("118670087467")) >= 0) {
            return new int[]{2, 3, 5, 7, 11};
        }
        if (n.compareTo(new BigInteger("25326001")) >= 0) {
            return new int[]{2, 3, 5, 7};
        }
        if (n.compareTo(new BigInteger("1373653")) >= 0) {
            return new int[]{2, 3, 5};
        }
        return new int[]{2, 3};
    }
    private static boolean try_composite(int a, BigInteger d, BigInteger n, BigInteger s) {
        BigInteger aB = BigInteger.valueOf(a);
        if (aB.modPow(d, n).equals(BigInteger.ONE)) {
            return false;
        }
        for (int i = 0; BigInteger.valueOf(i).compareTo(s) < 0; i++) {
            if (aB.modPow(BigInteger.valueOf(2).pow(i).multiply(d), n).equals(n.subtract(BigInteger.ONE))) {
                return false;
            }
        }
        return true;
    }
}
