import java.io.IOException;
import java.util.Scanner;

public class fuctional {
    public static void main(String[] args) throws IOException {
        rsa rsa = new rsa();
        rsa.KeyGen();
        Scanner sc = new Scanner(System.in);
        System.out.println("Криптосистема RSA: возможный функционал:" +
                            "\nencrypt\ndecrypt\nverify\nsign\nexit" + 
                            "\nдля выбора введите слово действия(из возможного функционала.)");
        while (true) {
            String res = sc.nextLine();
            switch (res){
                case "encrypt": 
                    System.out.println("Введите сообщение:");
                    String message = sc.nextLine();
                    rsa.encrypt(message, "pbkey");
                    System.out.println("Зашифрованное соообщение: " + message + "\n");
                    break;

                case "decrypt": 
                    System.out.println("Введите зашифрованное сообщение:");
                    String encryptedMessage = sc.nextLine();
                    rsa.decrypt(encryptedMessage, "prkey");
                    break;

                case "verify":
                    if(rsa.verify("pbkey", "signature") == true)
                        System.out.println("Verified");
                    else
                        System.out.println("Not verified");
                    break;

                case "sign":
                    rsa.signature("text.txt", "prkey");
                    break;

                case "exit":
                    System.out.println("Good luck!");
                    sc.close();
                    return;
            }
        }

    }
}
