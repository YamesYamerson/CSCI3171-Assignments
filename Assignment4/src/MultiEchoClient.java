import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MultiEchoClient{
	private static final int PORT = 1234;
	private static Socket link;
	private static BufferedReader in;
	private static PrintWriter out;
	private static BufferedReader kbd;
	private static String userName;

	public static void run() throws IOException {
		try {
			link = new Socket("127.0.0.1", PORT);
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			out = new PrintWriter(link.getOutputStream(), true);
			kbd = new BufferedReader(new InputStreamReader(System.in));
			String message, response;

			//Checks to see if client has just logged in and asks for username
			boolean login = true;
			if (login) {
				System.out.println("Enter your username (BYE to quit)");
				message = kbd.readLine();
				userName = message;
				out.println(userName);

				response = in.readLine();
				System.out.println(response);
			}

			//Checks link and closes it if it is null
			try {
				if (link == null) {
					System.out.println("Closing");
					link.close();
				}
			} catch (IOException e) {
				System.exit(1);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String message = "", response;
		//Exit conditions for server
		while (!message.equals("BYE")) {

			//Default conditions if client is not connecting to server
			System.out.println("Enter message (BYE to quit)");
			message = kbd.readLine();
			out.println(userName + " says: " + message);
//			response = in.readLine();
//			System.out.println(response);
		}
	}//end run() method

	public static void main(String[] args) throws Exception {
		MultiEchoClient newClient= new MultiEchoClient();
		newClient.run();
	}
}
//end class MultiEchoClient
	
	
