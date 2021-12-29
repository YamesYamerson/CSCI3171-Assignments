import java.io.*;
import java.net.*;
public class MultiEchoClient {
	private static final int PORT = 1234;
	private static Socket link;
	private static BufferedReader in;
	private static PrintWriter out;
	private static BufferedReader kbd;
	private static String userName;

	public static void main(String[] args) throws Exception{
		try{
			link = new Socket("127.0.0.1", PORT);
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			out = new PrintWriter(link.getOutputStream(), true);
			kbd = new BufferedReader(new InputStreamReader(System.in));
			String message, response;
			boolean userAccepted = false;

			//Takes user input for username
			do {
				String tempName;
				System.out.println("Enter a username (BYE to quit):");
				tempName = kbd.readLine();
				out.println(tempName);
				response = in.readLine();
//				System.out.println(response);
				if(response.equals("USERNAME OK")){
					userAccepted = true;
				}else if(response.equals("USERNAME ALREADY IN CLIENTLIST")){
					userAccepted = false;
				}
			}while(!userAccepted);

			System.out.println("You are connected as: " + userName);

			do {
				System.out.println("Enter message (BYE to quit)");
				message = kbd.readLine();
				out.println(userName + ": " + message);

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



	public boolean messageStartsWith(String message, String startsWith){
		int start = 0;
		int end = (startsWith.length()-1);
		if(message.substring(start, end).equalsIgnoreCase(startsWith)){
			return true;
		}else{
			return false;
		}
	}
}//end class MultiEchoClient
	
	
