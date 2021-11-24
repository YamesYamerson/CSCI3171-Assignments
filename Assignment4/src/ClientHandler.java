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
		//Connects client reader amd writer
		try {
			boolean login = true;
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

			//Creates and sets username for client from user input
			if(login) {
				String userName = in.readLine();
				String userConnectMessage = "[NEW USER \"" + userName + "\" IS CONNECTED SO SERVER]";
				System.out.println(userConnectMessage);
				out.println(userConnectMessage);
				login = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Relays and prints message information to server
		try {
			String message;
			do {
				message = in.readLine();
				out.println( client.getInputStream() + message);
				System.out.println("TESTLINE1: " + message);
			} while (!message.equals("BYE"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (client != null) {
					System.out.println("Closing connection");
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//Sends message to server using writer
	void sendMessage(String message){
		out.println(message);
	}
}//end ClientHandler class
