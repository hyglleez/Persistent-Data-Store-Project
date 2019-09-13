/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */ 
 
package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.ArrayList;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private InetAddress address;
	private int port;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
    public void write(String name, byte data[]) throws ClientException, ConnectionException
	{
		try {
			System.out.println("Opening Socket");
			@SuppressWarnings("resource")
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			System.out.println("Writing Message");
			StreamUtil.writeLine("write", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine("" + data.length, outputStream);
			StreamUtil.writeData(data, outputStream);
			
			System.out.println("Reading Response");
			String response = StreamUtil.readLine(inputStream);
			response = response.trim();
			System.out.println("Response " + response);
			if(!response.equalsIgnoreCase("ok")) {throw new ClientException(response);} 
		}
		catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
		//throw new RuntimeException("Executing Write Operation");
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
    public byte[] read(String name) throws ClientException, ConnectionException
	{
		byte[] result= new byte[0];
		try {
			System.out.println("Opening Socket");
			@SuppressWarnings("resource")
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			System.out.println(socket.isConnected());
			socket.connect(saddr);
			System.out.println(socket.isConnected());
			System.out.println("Opening Socket");
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			System.out.println("Writing Message");
			StreamUtil.writeLine("read\n", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			System.out.println("Reading Response");
			String response = StreamUtil.readLine(inputStream);
			response = response.trim();
			if(response.equalsIgnoreCase("ok")) {
				String sizestr = StreamUtil.readLine(inputStream);
				sizestr = sizestr.trim();
				int size = Integer.parseInt(sizestr);
				result = new byte[size];
				result = StreamUtil.readData(size, inputStream);
				System.out.println("Response " + result.toString());
				return result;
			}
			else throw new ClientException(response);
		}
		catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
			}
		//throw new RuntimeException("Executing Read Operation");
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException, ConnectionException
	{
		try {
			System.out.println("Opening Socket");
			@SuppressWarnings("resource")
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			System.out.println("Writing Message");
			StreamUtil.writeLine("delete\n", outputStream);
			StreamUtil.writeLine(name + "\n", outputStream);
			
			System.out.println("Reading Response");
			String response = StreamUtil.readLine(inputStream);
			response = response.trim();
			System.out.println("Response " + response);
			if(!response.equalsIgnoreCase("ok")) {throw new ClientException(response);}
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(),ex);
		}
		//throw new RuntimeException("Executing Delete Operation");
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException, ConnectionException
	{
		List<String> result = new ArrayList<String>();
		try {
			System.out.println("Opening Socket");
			@SuppressWarnings("resource")
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			System.out.println("Writing Message");
			StreamUtil.writeLine("directory\n", outputStream);
			
			System.out.println("Reading Response");
			String response = StreamUtil.readLine(inputStream);
			response = response.trim();
			if(response.equalsIgnoreCase("ok")) {
				String sizestr = StreamUtil.readLine(inputStream);
				sizestr = sizestr.trim();
				int size = Integer.parseInt(sizestr);
				for(int i = 0; i<size; i++) {
					String name = StreamUtil.readLine(inputStream);
					name = name.trim();
					result.add(name);
				}
				return result;
			}
			
			else throw new ClientException(response);
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(),ex);
		}
		//throw new RuntimeException("Executing Directory Operation");
	}

}
