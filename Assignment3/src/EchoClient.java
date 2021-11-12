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

		//User Input
		while ((usrInput = stdIn.readLine())!=null){
			String currentInput = usrInput;
			output.println(usrInput);
			String message = input.readLine();
			System.out.println("echo: " + message);
			output.flush();

			switch(currentInput) {
				case "echo: Enter Website URL:":
				case "iplookup terminating...":
				case "Would you like to play the number game (Y/N)?":
				case "You have guessed correctly! Game ending!":
				case "echo: Enter a message to encrypt: ":
					output.flush();

				case "/caesarcipher":
						CaesarCipher caesarCipher = new CaesarCipher();
						String serverKeyMessage = message;
						int serverKey = Integer.parseInt(serverKeyMessage.replaceAll("[^0-9]", ""));
						String currentMessage = usrInput;
						String encryptedMessage = caesarCipher.encrypt(currentMessage, serverKey);
						output.println(encryptedMessage);
						output.flush();
						break;

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
			if(isNumeric(currentInput)){
				output.flush();
			}
		}
	}
	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()){
			if(!Character.isDigit(c)) return false;
		}
		return true;
	}
}

