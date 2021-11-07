import java.net.*;
import java.util.Scanner;
public class MyLocalIPAddress{
	public static String run() throws Exception{
		try
		{
			InetAddress address = InetAddress.getLocalHost();
			return("IP address: " + address);
		}
		catch (UnknownHostException e)
		{
			return(" Could not find local host");
		}
	}
}
