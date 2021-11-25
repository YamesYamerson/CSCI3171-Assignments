//This is a support class that extends Thread, runs the client thread
//and sends and receives messages

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler extends Thread {
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private MultiEchoServer chatroomServer;
	private String userName;

	//Constructor for ClientHandler
	public ClientHandler(Socket socket, MultiEchoServer chatroomServer) throws IOException {
		this.client = socket;
		this.chatroomServer = chatroomServer;
	}
	//Initializes input and output
	public void run() {
		boolean login = true;
		//Connects client reader amd writer
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

			//Creates and sets username for client from user input
			while (login) {
				String userName = in.readLine();
				String userConnectMessage = "[NEW USER \"" + userName + "\" IS CONNECTED SO SERVER]";
				System.out.println(userConnectMessage);
				out.println("You are connected as: " + userName);
				login = false;
			}

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}


		//Relays and prints message information to server
		String message = "";
		while (!login && !message.equalsIgnoreCase("bye")) {
			try {

				do {
					message = in.readLine();  //code breaks here
					out.println(message);
					System.out.println(message);
				} while (!message.equals("BYE"));
			} catch (IOException e) {
				e.printStackTrace();
			}
					if (client == null) {
						System.out.println("Closing connection");
						try {
							client.close();
						} catch (IOException e) {
							e.printStackTrace();
							System.exit(0);
						}
					}

			}
		}


	//Sends message to server using writer
	void sendMessage(String message){
		out.println(message);
	}
}//end ClientHandler class
