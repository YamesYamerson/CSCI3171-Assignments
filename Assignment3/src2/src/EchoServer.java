import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class EchoServer{

	public static void main(String[] args) throws Exception {
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
//  TO DO:          if(validator.isValid(inputLine)) {                    //Regex to validate user input
					String ipAddress = ipFinder.find(inputLine) + " ==>  [IPLOOKUP TERMINATING]";
					System.out.println(ipAddress);
					output.println(ipAddress);
					System.out.println("[IPLOOKUP FUNCTION TERMINATING!]");
					break;

				case "/numbergame":                                         //Starts a number guessing game
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

				case "/killserver":
					System.out.println("[CLIENT REQUESTED SERVER SHUTDOWN!]");
					TimeUnit.SECONDS.sleep(1);
					System.out.println("[SERVER IS SHUTTING DOWN!");
					TimeUnit.SECONDS.sleep(1);
					closeServer(output, input, link, serverSock);
					System.exit(1);
					break;

				case "exit":
				case "quit":
				case "disconnect":
					disconnectClient(output, input, link, serverSock);
					break;
				default:
					output.println(currentMessage);
			}
		}
	}
	public static void closeServer(PrintWriter output, BufferedReader input, Socket link, ServerSocket serverSock)
			throws IOException {
		System.out.println("[CLIENT REQUESTED TO CLOSE SERVER!");
		output.close();
		input.close();
		link.close();
		serverSock.close();
	}

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