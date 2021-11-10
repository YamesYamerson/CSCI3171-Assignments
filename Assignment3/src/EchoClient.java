import java.io.*;
import java.net.*;
public class EchoClient{
	public static void main(String[] args) throws IOException{
		Socket link = null;
		PrintWriter output = null;
		BufferedReader input = null;

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
				"/myip ----- allows you to look up your ip address\n" +
				"/iplookup ----- allows you to look up the ip of a URL\n" +
				"/numbergame ----- allows user to play a number game\n" +
				"/exit, /quit, /disconnect ---- allows you to disconnect from the server\n\n" +
				"Welcome to the server, enter a message:");

		//User Input
		while ((usrInput = stdIn.readLine())!=null){
			String currentInput = usrInput;
			output.println(usrInput);
			System.out.println("echo: " + input.readLine());
			output.flush();

			switch(currentInput){
				case "echo: Enter Website URL:":
				case "iplookup terminating...":
				case "Would you like to play the number game (Y/N)?":
				case "Quitting guess the number game":
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
		}
	}
}