import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class EchoClient{
	public static void main(String[] args) throws IOException, InterruptedException {
		Socket link = null;
		PrintWriter output = null;
		BufferedReader input = null;
		String message;

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
		boolean cipherActive = false;
		while ((usrInput = stdIn.readLine())!=null){
			output.println(usrInput);
			message = input.readLine();

			if(usrInput.equalsIgnoreCase("/caesarcipher")){
				cipherActive = true;
			}

			if(!cipherActive){
				System.out.println("echo: " + message);
				output.flush();
			}
			if(cipherActive) {
				CaesarCipher caesarCipher = new CaesarCipher();
				String serverKeyMessage = message;
//				int serverKey;
//				java.util.regex.Matcher m = Pattern.compile(".*([0-9]+).*").matcher(serverKeyMessage);
//				if (m.matches()) serverKey = Integer.parseInt(m.group(1));
//				else serverKey = 0;
				int serverKey = Integer.parseInt(serverKeyMessage.replaceAll("[^0-9]", ""));
				System.out.println(serverKeyMessage);

				while ((usrInput = stdIn.readLine()) != null) {
					message = usrInput;
					if (usrInput.equalsIgnoreCase("/caesarcipher")){
						output.flush();
					}else if (usrInput.equalsIgnoreCase("/bye")
							|| usrInput.equalsIgnoreCase("bye")) {
						output.println(usrInput);
						//Caesar Cipher Active deactivated
						cipherActive = false;

						System.out.println("You are disconnecting from server in:");
						TimeUnit.SECONDS.sleep(1);
						System.out.println("3");
						TimeUnit.SECONDS.sleep(1);
						System.out.println("2");
						TimeUnit.SECONDS.sleep(1);
						System.out.println("1");
						System.out.println("Disconnecting from server...");

						output.close();
						input.close();
						stdIn.close();
						link.close();
						System.exit(1);
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
			}
			switch(usrInput) {
				case "/quit":
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