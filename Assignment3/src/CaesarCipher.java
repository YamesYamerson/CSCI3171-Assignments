public class CaesarCipher {
    public static String encrypt(String stringToEncrypt, int offset) {
        String ciphertext = "";
        char cipherChars;
        for(int i=0; i < stringToEncrypt.length();i++)
        {
            // CipherChar is between a-z
            cipherChars = stringToEncrypt.charAt(i);
            if(cipherChars >= 'a' && cipherChars <= 'z')
            {
                //Offset cipherChars
                cipherChars = (char) (cipherChars + offset);
                if(cipherChars > 'z') {
                    cipherChars = (char) (cipherChars+'a'-'z'-1); //reset to initial position
                }
                ciphertext = ciphertext + cipherChars;
            }

            //CipherChar is between A-Z
            else if(cipherChars >= 'A' && cipherChars <= 'Z') {
                cipherChars = (char) (cipherChars + offset);
                if(cipherChars > 'Z') {
                    cipherChars = (char) (cipherChars+'A'-'Z'-1);  //reset to initial position
                }
                ciphertext = ciphertext + cipherChars;
            }
            else {
                ciphertext = ciphertext + cipherChars;
            }
        }
        return(ciphertext);
    }
    public static String decrypt(String stringToEncrypt, int offset) {
        int decryptKey = 26 - offset;
        String ciphertext = "";
        char cipherChars;
        for(int i=0; i < stringToEncrypt.length();i++)
        {
            // Offsets between a-z
            cipherChars = stringToEncrypt.charAt(i);
            if(cipherChars >= 'a' && cipherChars <= 'z')
            {
                //Offset cipherChars
                cipherChars = (char) (cipherChars + decryptKey);
                if(cipherChars > 'z') {
                    cipherChars = (char) (cipherChars+'a'-'z'-1); //reset to initial position
                }
                ciphertext = ciphertext + cipherChars;
            }

            //Offsets between A-Z
            else if(cipherChars >= 'A' && cipherChars <= 'Z') {
                cipherChars = (char) (cipherChars + decryptKey);
                if(cipherChars > 'Z') {
                    cipherChars = (char) (cipherChars+'A'-'Z'-1);  //reset to initial position
                }
                ciphertext = ciphertext + cipherChars;
            }
            else {
                ciphertext = ciphertext + cipherChars;
            }
        }
        return(ciphertext);
    }
}
