import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MultiEchoServer{
	//declare the ServerSocket variable and the port number for the server(constant)
	private static ServerSocket serverSock;
	private static final int PORT = 1234;
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();


	//The main method will create the ServerSocket object and listens to inputs
	//from multiple clients

	public void run() throws IOException{
		try{
			serverSock = new ServerSocket(PORT);
		}
		catch(IOException e){
			System.out.println("[CAN'T LISTEN ON PORT" + PORT +"]");
			System.exit(1);
		}
		do{
			Socket client = null;
			System.out.println("[LISTENING FOR NEW CONNECTIONS...]");
			try{
				client = serverSock.accept();
				System.out.println("[NEW CLIENT ACCEPTED]");
				ClientHandler handler = new ClientHandler(client);
				addHandler(handler);
				handler.start();
			}
			catch(IOException e){
				System.out.println("[ACCEPT FAILED]");
				System.exit(1);
			}
			System.out.println("[CONNECTION SUCCESSFUL]");
			System.out.println("[LISTENING FOR INPUT...]");



		} while (true);

	}//end run
	public static void addHandler(ClientHandler clientHandler){
		clientHandlers.add(clientHandler);
	}

	//Gets list of ClientHandlers
	public static ArrayList<ClientHandler> getHandlers(){
		return clientHandlers;
	}

	public static void broadcastMessage(String broadcastMessage, ClientHandler activeHandler){
		for(ClientHandler clientHandler: clientHandlers){
			if(!clientHandler.getUserName().equals(activeHandler)){
				clientHandler.sendMessage(broadcastMessage);
			}
		}
	}
	//This is a support class that extends Thread, runs the client thread
//and sends and receives messages

	public class ClientHandler extends Thread{

		private Socket clientSocket;
		private BufferedReader in;
		private PrintWriter out;
		private String userName;

		public ClientHandler(Socket socket){
			clientSocket = socket;
			try{
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		public void run(){
			userName = null;
			try {
				userName = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.println(userName);
			System.out.println("[" + userName + " CONNECTED TO SERVER]");

			try{
				String received;
				do{
					received = in.readLine();
					sendMessage(received);
					System.out.println(received);
					out.println(received);

				} while (!received.equals("BYE"));
			}
			catch(IOException e){
				System.out.println("[USER HAS REQUESTED TO DISCONNECT FROM SERVER]");

			}
			finally{
				try{
					if(clientSocket!=null){
						System.out.println("[CLOSING CONNECTION TO " + userName + "]");
						clientSocket.close();
					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}//end run
		public void sendMessage(String message){
			out.println(message);
		}

		public String getUserName(){
			return userName;
		}
	}//end ClientHandler class

}//end class MultiEchoServer

