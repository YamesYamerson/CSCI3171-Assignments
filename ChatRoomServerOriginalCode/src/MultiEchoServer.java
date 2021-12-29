import com.sun.security.ntlm.Client;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class MultiEchoServer {
	//declare the ServerSocket variable and the port number for the server(constant)
	private static ServerSocket serverSock;
	private static final int PORT = 1234;
	public static Set<ClientHandler> clientHandlers = new HashSet<>();
	public static Set<String> clientUserNames = new HashSet<>();

	//The main method will create the ServerSocket object and listens to inputs from multiple clients
	public void run(){
		try {
			serverSock = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("[CAN'T LISTEN ON PORT" + PORT + "]");
			System.exit(1);
		}
		do {
			Socket client;
			System.out.println("[LISTENING FOR NEW CONNECTIONS...]");
			try {
				client = serverSock.accept();
				System.out.println("[NEW CLIENT ACCEPTED]");
				ClientHandler handler = new ClientHandler(client, this);
				addHandler(handler);
				handler.start();

				//Prints a list of active threads in clientHandlers list
				System.out.println(getHandlers().toString());
			} catch (IOException e) {
				System.out.println("[ACCEPT FAILED]");
				System.exit(1);
			}
			System.out.println("[LISTENING FOR INPUT...]");

		} while (true);

	}//end run

	//Adds handler to HashSet clientHandlers
	void addHandler(ClientHandler clientHandler) {
		clientHandlers.add(clientHandler);
	}

	//Removes handler from Hashset clientHandlers
	void removeHandler(ClientHandler clientHandler) {clientHandlers.remove(clientHandler); }

	//Adds user to HashSet userNames
	void addUserName(String user) {clientUserNames.add(user); }

	//Removes user from HashSet userNames
	void removeUserName(String user){clientUserNames.remove(user); }

	//Returns a list of current client usernames from HashSet clientUserNames
	public Set<String> getClientUserNames(){
		return clientUserNames;
	}

	//Gets list of ClientHandlers
	public Set<ClientHandler> getHandlers() {
		return clientHandlers;
	}

	//Sends message from one user/client to other active users/clients
	void broadcastMessage(String broadcastMessage, ClientHandler activeClient){
		for (ClientHandler client: clientHandlers){
			if(client != activeClient){
				activeClient.sendMessage(broadcastMessage);
			}
		}
	}
	//This is a support class that extends Thread, runs the client thread
	//and sends and receives messages

	public class ClientHandler extends Thread{

		//Local variables for ClientHandler class
		private MultiEchoServer chatroomServer;
		private Socket clientSocket;
		private BufferedReader in;
		private PrintWriter out;
		private String userName;
		private boolean nameAccepted = false;

		//Constructor for ClientHandler class
		public ClientHandler(Socket clientSocket, MultiEchoServer chatroomServer){
			this.clientSocket = clientSocket;
			this.chatroomServer = chatroomServer;
		}

		//Method to run ClientHandler thread
		public void run() {

			//Local variables for run method
			String tempName;
			boolean contains = false;

			//Initializes input and output stream for ClientHandler
			try {
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					out = new PrintWriter(clientSocket.getOutputStream(), true);

				//Takes user input for username, checks if username is valid, and sets ClientHandler username
				do{
					tempName = in.readLine();
					if (tempName == null) {
						return;
					}

					//Checks to see if list of handlers contains input username
					if (chatroomServer.getClientUserNames().contains(tempName)) {
						out.println("USERNAME ALREADY IN CLIENTLIST");
						contains = true;
					}

					//If username is not in userNameList
					if(!contains){
						nameAccepted = true;
						out.println("USERNAME OK");
					}

				}while (!nameAccepted);

				//Sets username and adds ClientHandler clientHandlers list
				setUserName(tempName);
				addUserName(tempName);
				addHandler(this);
				System.out.println("[" + userName + " CONNECTED TO SERVER]");

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				String received;
				do {
					received = in.readLine();
					String message = (received);
					chatroomServer.broadcastMessage(message, this);
					System.out.println(message);
				} while (!received.equals("BYE"));

				//Outputs disconnect message to server
				if(received.equals("BYE")) {
					System.out.println("[USER HAS REQUESTED TO DISCONNECT FROM SERVER]");
				}

			} catch (IOException e) {
				e.printStackTrace();

			//
			}finally{
					try {
						if (clientSocket != null) {
							System.out.println("[CLOSING CONNECTION TO " + userName + "]");
							removeHandler(this);
							removeUserName(this.getUserName());
							clientSocket.close();
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}//end run

		//Sends a message from the client to the server using PrintWriter
		void sendMessage(String message){
			out.println(message);
		}

		//Gets client userName from list of ClientHandler
		public String getUserName(){
			return userName;
		}

		//Sets clients userName in ClientHandler
		public void setUserName(String userName) {
			this.userName = userName;
		}
	}//end ClientHandler class

	//Main method for MultiEchoServer class
	public static void main(String[] args) {
		MultiEchoServer chatroomServer = new MultiEchoServer();
		chatroomServer.run();
	}
}//end class MultiEchoServer

