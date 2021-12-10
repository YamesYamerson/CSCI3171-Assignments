//This is a support class that extends Thread, runs the client thread, and sends and receives messages

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
		//Connects client reader and writer
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

			//Creates and sets username for client from user input
			while (login) {

				//Takes first user input and assigns username
				userName = in.readLine();

				//Messages conditions for Server, current Client, and Clients to broadcast to
				String serverUserConnectMessage = "[NEW USER \"" + userName + "\" IS CONNECTED SO SERVER]";
				String clientUserConnectMessage = "You are connected as: " + userName;
				String broadcastUserConnectMessage = userName + " connected to server.";

				//Outputs login messages to respective streams
				System.out.println(serverUserConnectMessage);
				out.println(clientUserConnectMessage);
				login = false;
			}

		} catch (IOException ioException) {
			ioException.printStackTrace();
			System.out.println("Client unable to connect");
			System.exit(1);
		}


		//Relays and prints message information from client to server
		String message = "";
		do {
			try {
				message = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.println(message);
			System.out.println(message);
		} while (!message.equals("BYE"));

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

	//Sends message to server using writer
	void sendMessage(String message){
		out.println(message);
	}

}//end ClientHandler class
