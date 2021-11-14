import java.net.*;
import java.util.Scanner;
public class IPFinder{
	public static String find(String host) throws Exception{
		try
		{
			InetAddress address = InetAddress.getByName(host);
			return("IP address: " + address);
		}
		catch (UnknownHostException e)
		{
			return("  Could not find " + host);
		}
	}
}
