import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.net.*;


public class CaesarCipherServer {
    //Main method to run server program
    public static void main(String[] args) {
    //Initializes EchoServer and EchoClient Objects
    EchoServer echoServer = new EchoServer();
    EchoClient echoClient = new EchoClient();
    //Runs server program
    echoServer.run();
    echoClient.run();
    }

    //EchoServer Class
    public class EchoServer{
        public static void run() throws Exception {
            //Initializes and attempts to connect server
            ServerSocket serverSock = null;
            try{
                serverSock = new ServerSocket(8000);
            }
            catch (IOException ie){
                System.out.println("[SERVER DISCONNECTED!] Can't listen on 8000");
                System.exit(1);
            }
            Socket link = null;
            System.out.println("[SERVER CONNECTED!] Listening for connection ...");
            try {
                link = serverSock.accept();
            }
            catch (IOException ie){
                System.out.println("[SERVER DISCONNECTED!] Accept failed");
                System.exit(1);
            }

            //Connects client if handshake is successful
            System.out.println("[CLIENT CONNECTED!] Link Information:" + link);
            System.out.println("Listening for input ...");

            //Initializes input and output streams
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            String inputLine;
            while ((inputLine = input.readLine())!=null) {
                String currentMessage = inputLine;
                System.out.println("[SERVER:] " + currentMessage);
                switch(currentMessage){
                    case "/myip":                                           //Looks up local IP address of client
                        MyLocalIPAddress myIP = new MyLocalIPAddress();
                        String ip = myIP.run();
                        output.println(ip);
                        System.out.println(ip);
                        break;
                    case "/iplookup":                                       //Looks up IP of user input website
                        IPFinder ipFinder = new IPFinder();
//  TO DO:              UrlValidator validator = new UrlValidator();
                        System.out.println("[REQUESTING URL FROM CLIENT!]");
                        output.println("Enter Website URL:");
                        inputLine = input.readLine();
                        System.out.println("Server: " + inputLine);
//  TO DO:              if(validator.isValid(inputLine)) {                    //Regex to validate user input
                        String ipAddress = ipFinder.find(inputLine) + " ==>  [IPLOOKUP TERMINATING]";
                        System.out.println(ipAddress);
                        output.println(ipAddress);
                        System.out.println("[IPLOOKUP FUNCTION TERMINATING!]");
                        break;

                    case "/numbergame":                                       //Starts a number guessing game
                        //Variable initialization
                        boolean isPlaying;
                        int randomNumber = -1;
                        final int maxNum = 100;
                        //Game start message output
                        System.out.println("[CLIENT HAS INITIATED GUESS THE NUMBER GAME!]");
                        output.println("Would you like to play the number game (Y/N)?");
                        inputLine = input.readLine();
                        //Conditions for game start based on user input
                        if(inputLine.equalsIgnoreCase("Y")){
                            System.out.println("[CLIENT REQUESTS TO BEGIN A GAME!]");
                            isPlaying = true;
                            System.out.println("[SERVER IS PICKING A RANDOM NUMBER!]");
                            output.println("Enter A Number Between 0-100 (/endgame to quit)");
                            randomNumber = (int) Math.floor(Math.random() * maxNum + 1);
                            System.out.println("[RANDOM NUMBER IS: " + randomNumber + "]");
                        }else if(inputLine.equalsIgnoreCase("N")){
                            System.out.println("[GUESS THE NUMBER GAME ABORTING]");
                            output.println("Quitting guess the number game");
                            isPlaying = false;
                        }else{
                            System.out.println("[CLIENT PROVIDED FAULTY INPUT, GAME ABORTING!]");
                            output.println("Input not recognized, game aborting!");
                            isPlaying = false;
                        }

                        //Game state loop
                        while(isPlaying) {
                            System.out.println("[REQUESTING NUMBER FROM CLIENT!]");
                            inputLine = input.readLine();
                            if (inputLine.equalsIgnoreCase("/endgame")) {
                                isPlaying = false;
                            }
                            if (isNumeric(inputLine)) {
                                int currentGuess = Integer.parseInt(inputLine);
                                if (randomNumber == currentGuess) {
                                    System.out.println("[CLIENT HAS GUESSED THE CORRECT NUMBER, GAME TERMINATING!]");
                                    output.println("You have guessed correctly! Game ending!");
                                    isPlaying = false;
                                } else if (currentGuess > randomNumber) {
                                    System.out.println("[CLIENT GUESSED TOO HIGH!]");
                                    output.println("Too high! Guess again!");
                                } else if (currentGuess < randomNumber) {
                                    System.out.println("[CLIENT GUESSED TOO LOW!]");
                                    output.println("Too low! Guess again!");
                                }
                            }else if(inputLine.equalsIgnoreCase("/endgame")){
                                System.out.println("[CLIENT HAS REQUESTED TO END GAME - TERMINATING...]");
                                output.println("Quitting game, better luck next time!");
                                break;

                            }else{
                                System.out.println("[CLIENT HAS PROVIDED INCORRECT INPUT]");
                                output.println("You have not entered a valid number, try again!");
                            }
                        }
                        break;

                    case "/caesarcipher":
                        int maxKeyValue = 25;

                        int randomKey = (int) Math.floor(Math.random() * maxKeyValue + 1);
                        System.out.println("[CLIENT HAS INITIATED CAESAR CIPHER PROGRAM ----- CLIENT CAESAR CIPHER IS "
                                + randomKey + " (Type 'bye' to exit)!]");
                        output.println("Your Caesar Cipher is " + randomKey + ". Enter a message to encrypt: ");

                        while((inputLine = input.readLine()) != null) {
                            if (inputLine.equalsIgnoreCase("/caesarcipher")) {
                                output.println("Your Caesar Cipher is " + randomKey + ". Enter a message to encrypt: ");
                                output.flush();
                            }else if (inputLine.equalsIgnoreCase("/bye") ||
                                    inputLine.equalsIgnoreCase("bye")) {
                                System.out.println("[CLIENT HAS REQUESTED TO END CAESAR CIPHER MODE!]");
                                output.println("Quitting Caesar cipher mode...");
                                break;
                            }else{
                                CaesarCipher caesarCipher = new CaesarCipher();
                                currentMessage = inputLine;
                                String decryptedMessage = caesarCipher.decrypt(currentMessage, randomKey);
                                if(decryptedMessage.equalsIgnoreCase("/bye") ||
                                        decryptedMessage.equalsIgnoreCase("bye")){
                                    disconnectClient(output, input, link, serverSock);
                                    break;
                                }
                                System.out.println("[SERVER:] Received: " + currentMessage + " ==> " +
                                        decryptedMessage);
                            }
                        }
                        break;

                    case "/secretmessage":
                        System.out.println("♡♡♡♡♡♡ Shahnaz ♡♡♡♡♡♡");
                        output.println("♡♡♡♡♡♡ Shahnaz ♡♡♡♡♡♡");
                        break;

                    case "/killserver":                                                 //Closes server
                        System.out.println("[CLIENT REQUESTED SERVER SHUTDOWN!]");
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("[SERVER IS SHUTTING DOWN!");
                        TimeUnit.SECONDS.sleep(1);
                        closeServer(output, input, link, serverSock);
                        System.exit(1);
                        break;

                    case "exit":                                                       //Disconnects client
                    case "quit":
                    case "disconnect":
                        disconnectClient(output, input, link, serverSock);
                        break;
                    default:
                        output.println(currentMessage);
                }
            }
        }
        //Method to close server from client end
        public static void closeServer(PrintWriter output, BufferedReader input, Socket link, ServerSocket serverSock)
                throws IOException {
            System.out.println("[CLIENT REQUESTED TO CLOSE SERVER!");
            output.close();
            input.close();
            link.close();
            serverSock.close();
        }

        //Method to disconnect client from server
        public static void disconnectClient(PrintWriter output, BufferedReader input, Socket link, ServerSocket serverSock)
                throws IOException, InterruptedException {
            output.println("[CLIENT REQUESTED TO CLOSE CONNECTION!");
            TimeUnit.SECONDS.sleep(5);
            output.println("[DISCONNECTING!");
            output.close();
            input.close();
            link.close();
        }

        //Tests to see if a string contains all numeric values
        public static boolean isNumeric(String str) {
            for (char c : str.toCharArray()){
                if (!Character.isDigit(c)) return false;
            }
            return true;
        }
    }

    //EchoClient Class
    public class EchoClient{
        public static void run() throws IOException{
            //Variable assignment
            Socket link = null;
            PrintWriter output = null;
            BufferedReader input = null;
            String message;

            //Attempts to connect client to server
            try{
                System.out.println("[CONNECTING TO SERVER ON PORT 8000]");
                link = new Socket(InetAddress.getLocalHost(), 8000);
                output = new PrintWriter(link.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(link.getInputStream()));
                System.out.println("[CONNECTED TO SERVER]");
            }
            //Disconnects if bad host is encountered
            catch(UnknownHostException e)
            {
                System.out.println("Unknown Host");
                System.exit(1);
            }
            //Disconnects if connection is unable to be established
            catch (IOException e){
                System.out.println("Cannot connect to host");
                System.exit(1);
            }
            //Initializes client side input
            BufferedReader stdIn = new BufferedReader((new InputStreamReader(System.in)));
            String usrInput;

            //Provides client with list of server options
            System.out.println("Welcome to the Server!\n\n" +
                    "----- Here is a List Options: ----- \n " +
                    "/myip ----- will allow you to look up your ip address\n" +
                    "/iplookup ----- will allow you to look up the ip of a URL\n" +
                    "/numbergame ----- allows you to play a guess the number game\n" +
                    "/caesarcipher ----- allows you to encrypt and decrypt server messages using a caesar cipher\n" +
                    "/exit, /quit, /disconnect ---- allows you to disconnect from the server\n\n" +
                    "Enter a message:");

            //User Input
            boolean cipherActive = false;
            while ((usrInput = stdIn.readLine())!=null){
                output.println(usrInput);
                message = input.readLine();

                //Check to run server in normal mode
                if{(!cipherActive) {
                    System.out.println("echo: " + message);

                //Activates CaesarCipher mode
                }else if(usrInput.equalsIgnoreCase("/caesarcipher")){
                    cipherActive = true;

                //Routine if CaesarCipher mode is active
                if(cipherActive) {
                    CaesarCipher caesarCipher = new CaesarCipher();

//TO DO:		int serverKey;
//				java.util.regex.Matcher m = Pattern.compile(".*([0-9]+).*").matcher(serverKeyMessage);
//				if (m.matches()) serverKey = Integer.parseInt(m.group(1));
//				else serverKey = 0;

                    //Sets the server key from server key message
                    String serverKeyMessage = message;
                    int serverKey = Integer.parseInt(serverKeyMessage.replaceAll("[^0-9]", ""));
                    System.out.println(serverKeyMessage);

                    //Operations loop while CaesarCipher loop is active
                    while ((usrInput = stdIn.readLine()) != null) {
                        message = usrInput;
                        if (usrInput.equalsIgnoreCase("/caesarcipher")){

                        //Exit condition for CaesarCipher mode (Disconnects Server)
                        }else if (usrInput.equalsIgnoreCase("/bye")
                                || usrInput.equalsIgnoreCase("bye")) {
                            cipherActive = false;
                        }

                        //Sends server encrypted CaesarCipher message
                        if (!message.equalsIgnoreCase("/caesarcipher")) {
                            String currentMessage = usrInput;
                            String encryptedMessage = caesarCipher.encrypt(currentMessage, serverKey);
                            output.println(encryptedMessage);
                            System.out.println("echo: " + message);
                        }
                    }
                }

                //Switch conditions for message inputs
                switch(message) {
                    case "/quit":                                               //Disconnects server and client
                    case "/disconnect":
                    case "/killserver":
                    case "bye":
                    case "/bye":
                        System.out.println("Disconnecting from server...");
                        output.close();
                        input.close();
                        stdIn.close();
                        link.close();
                        System.exit(1);
                }
            }


            //Flushes output for number game
            if(isNumeric(usrInput)){
                output.flush();
            }
        }

        //Checks string for numerical values and returns true if all numeric, false if other characters are present
        public static boolean isNumeric(String str) {
            for (char c : str.toCharArray()){
                if(!Character.isDigit(c)) return false;
            }
            return true;
        }
    }

    //MyLocalIPAddress Class
    import java.net.*;
    import java.util.Scanner;
    public class MyLocalIPAddress{
        public static String run() throws Exception{
            try
            {
                InetAddress address = InetAddress.getLocalHost();
                return("IP address: " + address);
            }
            catch (UnknownHostException e)
            {
                return(" Could not find local host");
            }
        }
    }

    //IPFinder Class
        public class IPFinder{
            public static String find(String host) throws Exception{
                try
                {
                    InetAddress address = InetAddress.getByName(host);
                    return("IP address: " + address);
                }
                catch (UnknownHostException e)
                {
                    return("  Could not find " + host);
                }
            }
        }

    //NumberGame Class


    public class NumberGame {
        BufferedReader input;
        PrintWriter output;
        String inputLine;

        public void run() throws IOException {
            boolean isPlaying;
            int randomNumber = -1;
            final int maxNum = 100;
            System.out.println("[CLIENT HAS INITIATED GUESS THE NUMBER GAME!]");
            output.println("Would you like to play the number game (Y/N)?");
            inputLine = input.readLine();
            if(inputLine.equalsIgnoreCase("Y")){
                System.out.println("[CLIENT REQUESTS TO BEGIN A GAME!]");
                isPlaying = true;
                System.out.println("[SERVER IS PICKING A RANDOM NUMBER!]");
                output.println("Enter A Number Between 0-100 (/endgame to quit)");
                randomNumber = (int) Math.floor(Math.random() * maxNum + 1);
                System.out.println("[RANDOM NUMBER IS: " + randomNumber + "]");
            }else if(inputLine.equalsIgnoreCase("N")){
                System.out.println("[GUESS THE NUMBER GAME ABORTING]");
                output.println("Quitting guess the number game");
                isPlaying = false;
            }else{
                System.out.println("[CLIENT PROVIDED FAULTY INPUT, GAME ABORTING!]");
                output.println("Input not recognized, game aborting!");
                isPlaying = false;
            }

            while(isPlaying) {
                System.out.println("[REQUESTING NUMBER FROM CLIENT!]");
                inputLine = input.readLine();
                if (inputLine.equalsIgnoreCase("/endgame")) {
                    isPlaying = false;
                }
                if (isNumeric(inputLine)) {
                    int currentGuess = Integer.parseInt(inputLine);
                    if (randomNumber == currentGuess) {
                        System.out.println("[CLIENT HAS GUESSED THE CORRECT NUMBER, GAME TERMINATING!]");
                        output.println("You have guessed correctly! Game ending!");
                        isPlaying = false;
                    } else if (currentGuess > randomNumber) {
                        System.out.println("[CLIENT GUESSED TOO HIGH!]");
                        output.println("Too high! Guess again! (/endgame to quit)");
                    } else if (currentGuess < randomNumber) {
                        System.out.println("[CLIENT GUESSED TOO LOW!]");
                        output.println("Too low! Guess again! (/endgame to quit)");
                    }
                }else if(inputLine.equalsIgnoreCase("/endgame")){
                    System.out.println("[CLIENT HAS REQUESTED TO END GAME - TERMINATING...]");
                    output.println("Quitting game, better luck next time!");
                    break;

                }else{
                    System.out.println("[CLIENT HAS PROVIDED INCORRECT INPUT]");
                    output.println("You have not entered a valid number, try again! (/endgame to quit)");
                }
            }
        }
    //CaesarCipher Class
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

    //UrlValidator Class

        public class UrlValidator {
            public static boolean isValid(String urlToValidate) {
                String regexPattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                Pattern p = Pattern.compile(regexPattern);
                if (urlToValidate == null) {
                    return false;
                }
                Matcher m = p.matcher(urlToValidate);
                return m.matches();
            }
        }
    }
