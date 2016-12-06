package testP2P;
import java.net.*;
import java.io.*;
import java.net.*;

public class p2p
{
	public static void main(String[] args) throws IOException 
	{
		reciever reciever = new reciever();
		sender sender = new sender();
		
		reciever.start();
		sender.start();
	}
	
	static public class reciever extends Thread
	{
		
		String multicastgr = "239.255.255.250";
		int port = 12000;
		
		public void run()
		{
			while(true)
			try
			{
				InetAddress address = InetAddress.getByName(multicastgr);
				MulticastSocket socket = new MulticastSocket(port);
				
				socket.joinGroup(address);
				
				DatagramPacket packet;
				byte[] buf = new byte[256];
				
				
				packet = new DatagramPacket(buf,buf.length);
				
				socket.receive(packet);
				
				
				String rtn=new String(packet.getData());
				
				InetAddress sender = packet.getAddress();
				String hostname = sender.getHostName();
				System.out.println(">"+hostname +": " + rtn);
				
				socket.leaveGroup(address);
				socket.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
	}

	static public class sender extends Thread
	{
		
			DatagramSocket socket = new DatagramSocket();
			String multicastID = "239.255.255.250";
			
			int port = 12000;
			
			public sender() throws IOException
			{
				
			}
			
			public void run()
			{
				InetAddress group;
				BufferedReader userinput = new BufferedReader(new InputStreamReader(System.in));
				try 
				{
					group = InetAddress.getByName(multicastID);
					while(true)
					{
						try
						{
						
							byte[] buffer = new byte[256];
							String outgoing ="";
							
							outgoing = userinput.readLine();
							buffer = outgoing.getBytes();
							DatagramPacket packet = new DatagramPacket(buffer,buffer.length,group,port);
							socket.send(packet);
						}
						catch (IOException e) 
						{
							e.printStackTrace();
							socket.close();
							break;
						}
					}
				} 
				catch (UnknownHostException e1) 
				{
					e1.printStackTrace();
				}
			}
	}
}
