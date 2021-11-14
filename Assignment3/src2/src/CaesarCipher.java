import java.util.*;

public class CaesarCipher {
    public static String encrypt(String stringToEncrypt, int offset) {
        String ciphertext = "";
        char alphabet;
        for(int i=0; i < stringToEncrypt.length();i++)
        {
            // Shift one character at a time
            alphabet = stringToEncrypt.charAt(i);

            // if alphabet lies between a and z
            if(alphabet >= 'a' && alphabet <= 'z')
            {
                // shift alphabet
                alphabet = (char) (alphabet + offset);
                // if shift alphabet greater than 'z'
                if(alphabet > 'z') {
                    // reshift to starting position
                    alphabet = (char) (alphabet+'a'-'z'-1);
                }
                ciphertext = ciphertext + alphabet;
            }

            //Alphabet is between 'A' and 'Z'
            else if(alphabet >= 'A' && alphabet <= 'Z') {
                //Offset alphabet
                alphabet = (char) (alphabet + offset);
                // Offset alphabet greater than 'Z'
                if(alphabet > 'Z') {
                    //reshift to starting position
                    alphabet = (char) (alphabet+'A'-'Z'-1);
                }
                ciphertext = ciphertext + alphabet;
            }
            else {
                ciphertext = ciphertext + alphabet;
            }
        }
        return(ciphertext);

    }
    public static String decrypt(String stringToEncrypt, int offset) {
        int decryptKey = 26 - offset;
        String ciphertext = "";
        char alphabet;
        for(int i=0; i < stringToEncrypt.length();i++)
        {
            // Shift one character at a time
            alphabet = stringToEncrypt.charAt(i);

            // if alphabet lies between a and z
            if(alphabet >= 'a' && alphabet <= 'z')
            {
                // shift alphabet
                alphabet = (char) (alphabet + decryptKey);
                // if shift alphabet greater than 'z'
                if(alphabet > 'z') {
                    // reshift to starting position
                    alphabet = (char) (alphabet+'a'-'z'-1);
                }
                ciphertext = ciphertext + alphabet;
            }

            // if alphabet lies between 'A'and 'Z'
            else if(alphabet >= 'A' && alphabet <= 'Z') {
                // shift alphabet
                alphabet = (char) (alphabet + decryptKey);

                // if shift alphabet greater than 'Z'
                if(alphabet > 'Z') {
                    //reshift to starting position
                    alphabet = (char) (alphabet+'A'-'Z'-1);
                }
                ciphertext = ciphertext + alphabet;
            }
            else {
                ciphertext = ciphertext + alphabet;
            }
        }
        return(ciphertext);
    }
}
