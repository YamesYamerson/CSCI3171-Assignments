import java.io.*;
import java.net.*;

public class EchoClient{
	public static void main(String[] args) throws IOException{
		Socket link = null;
		PrintWriter output = null;
		BufferedReader input = null;
		Boolean cipherModeActive = true;

		try{
			System.out.println("[CONNECTING TO SERVER ON PORT 8000]");
			link = new Socket(InetAddress.getLocalHost(), 8000);
			output = new PrintWriter(link.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(link.getInputStream()));
			System.out.println("[CONNECTED TO SERVER]");
		}
		catch(UnknownHostException e)
		{
			System.out.println("Unknown Host");
			System.exit(1);
		}
		catch (IOException e){
			System.out.println("Cannot connect to host");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader((new InputStreamReader(System.in)));
		String usrInput;

		System.out.println("Welcome to the Server!\n\n" +
				"----- Here is a List Options: ----- \n " +
				"/myip ----- will allow you to look up your ip address\n" +
				"/iplookup ----- will allow you to look up the ip of a URL\n" +
				"/numbergame ----- allows you to play a guess the number game\n" +
				"/caesarcipher ----- allows you to encrypt and decrypt server messages using a caesar cipher\n" +
				"/exit, /quit, /disconnect ---- allows you to disconnect from the server\n\n" +
				"Enter a message:");

		//Initializes CaesarCipher variable and object
		boolean cipherActive = false;
		String serverKeyMessage;
		int serverKey;
		CaesarCipher caesarCipher = new CaesarCipher();
		//Checks for user input
		while ((usrInput = stdIn.readLine())!=null) {
			output.println(usrInput);
			String message = input.readLine();
			if (usrInput.equalsIgnoreCase("/caesarcipher")) {
				serverKeyMessage = message;
				int tempServerKey = Integer.parseInt(serverKeyMessage.replaceAll("[^0-9]", ""));
				serverKey = tempServerKey;
				cipherActive = true;
				if (cipherActive) {
					while ((usrInput = stdIn.readLine()) != null) {
						message = usrInput;
						if (usrInput.equalsIgnoreCase("/caesarcipher")) {
							System.out.println(input.readLine());
						} else if (usrInput.equalsIgnoreCase("/bye")
								|| usrInput.equalsIgnoreCase("bye")) {
							break;
						}
						//Encrypts message to send to server

						//Prints server response to client
						if (!message.equalsIgnoreCase("/caesarcipher")) {
							String currentMessage = usrInput;
							String encryptedMessage = caesarCipher.encrypt(currentMessage, serverKey);
							output.println(encryptedMessage);
							System.out.println("echo: " + message);

						}
					}
					break;
				}

			}

			if (!cipherActive) {
				String echo = input.readLine();
				System.out.println("echo: " + echo);
				output.flush();
			}
		}

		switch(usrInput) {
//			case "echo: Enter Website URL:":
//			case "iplookup terminating...":
//			case "Would you like to play the number game (Y/N)?":
//			case "You have guessed correctly! Game ending!":
//			case "echo: Enter a message to encrypt: ":
//				output.flush();

			case "echo: /quit":
			case "echo: /disconnect":
			case "echo: /killserver":
				output.close();
				input.close();
				stdIn.close();
				link.close();
				break;
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

