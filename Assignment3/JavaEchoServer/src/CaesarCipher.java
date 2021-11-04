import java.util.Scanner;
import java.util.Random;

public class CaesarCipher {
    Scanner scanner = new Scanner(System.in);
    public String rawPasword;
    public String cipherPassword = "";
    char alphabet;
    Random randomInteger = new Random();

    public String createCipherPassword(String rawPassword, int cipherNumber){
        for(int i = 0; i < rawPassword.length(); i++){

            //Shifts one character at a time
            alphabet = rawPassword.charAt(i);

            //If alphabet char is between a-z
            if(alphabet >= 'a' && alphabet <= 'z'){

                //Shift alphabet character
                alphabet = (char) (alphabet+'a'-'z'-1);
            }
            //If alphabet shift is greater than 'Z'
            if(alphabet >= 'A' && alphabet <= 'Z'){
                //Shift alphabet
//                alphabet = (char)(alphabet + shift);
            }
        }

        return cipherPassword;
    }

}
