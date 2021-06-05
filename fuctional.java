import java.io.IOException;
import java.util.Scanner;

public class fuctional {
    public static void main(String[] args) throws IOException {
        rsa rsa = new rsa();
        rsa.KeyGen();
        Scanner sc = new Scanner(System.in);
        boolean process = true;
        System.out.println("Криптосистема RSA: возможный функционал:" +
        "\n1.encrypt\n2.decrypt\n3.encrypt\n4.verify\n5.sign\n6.exit" + 
        "\nдля выбора введите слово действия(из возможного функционала.)");
        
        while (process) {
            String res = sc.nextLine();
            switch (res){
                case "encrypt": 
                    System.out.println("Введите сообщение:");
                    String message = sc.nextLine();
                    rsa.encrypt(message, "/Users/21shish/Desktop/pbkey");
                    System.out.println("что-то еще?");
                    break;

                case "decrypt": 
                    System.out.println("Введите зашифрованное сообщение:");
                    String encryptedMessage = sc.nextLine();
                    rsa.decrypt(encryptedMessage, "/Users/21shish/Desktop/prkey");
                    System.out.println("что-то еще?");
                    break;

                case "verify":
                    if(rsa.verify("pbkey", "signature") == true)
                        System.out.println("Verified");
                    else
                        System.out.println("Not verified");
                    System.out.println("что-то еще?");
                    break;

                case "sign":
                    rsa.signature("text.txt", "prkey");
                    System.out.println("что-то еще?");
                    break;

                case "exit":
                    System.out.println("Good luck!");
                    sc.close();
                    process = false;
                    return;
            }
        }
        sc.close();
    }
}
