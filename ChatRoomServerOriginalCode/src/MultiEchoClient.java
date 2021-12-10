import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.net.*;
public class MultiEchoClient{
	private static final int PORT = 1234;
	private static Socket link;
	private static BufferedReader in;
	private static PrintWriter out;
	private static BufferedReader kbd;
	private static String userName;

	//
	public static void main(String[] args) throws Exception{
		try{
			link = new Socket("127.0.0.1", PORT);
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			out = new PrintWriter(link.getOutputStream(), true);
			kbd = new BufferedReader(new InputStreamReader(System.in));
			String message, response;

			//Takes user input for username
			System.out.println("Enter Username: ");
			userName = kbd.readLine();
			out.println(userName);
//					response = in.readLine();
			System.out.println("You are connected as: " + userName);
			do {


				System.out.println("Enter message (BYE to quit)");
				message = kbd.readLine();
				out.println(userName + ": " + message);
//				response = in.readLine();
//				System.out.println(response);

			}while (!message.equals("BYE"));
		}
		catch(UnknownHostException e){System.exit(1);}
		catch(IOException e){System.exit(1);}
		finally{
			try{
				if (link!=null){
					System.out.println("Closing");
					link.close();
				}
			}
			catch(IOException e){System.exit(1);}
		}
	}//end main
}//end class MultiEchoClient
	
	