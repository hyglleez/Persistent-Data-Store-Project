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
 
package utd.persistentDataStore.datastoreServer;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import utd.persistentDataStore.datastoreServer.commands.ServerCommand;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreServer
{
	static public final int port = 10023;

	public void startup() throws IOException
	{
		System.out.println("Starting Service at port " + port);

		ServerSocket serverSocket = new ServerSocket(port);

		InputStream inputStream = null;
		OutputStream outputStream = null;
		while (true) {
			try {
				System.out.println("Waiting for request");
				// The following accept() will block until a client connection 
				// request is received at the configured port number
				Socket clientSocket = serverSocket.accept();
				System.out.println("Request received");

				inputStream = clientSocket.getInputStream();
				outputStream = clientSocket.getOutputStream();

				ServerCommand command = dispatchCommand(inputStream);
				System.out.println("Processing Request: " + command);
				command.setInputStream(inputStream);
				command.setOutputStream(outputStream);
				command.run();
				
				StreamUtil.closeSocket(inputStream);
			}
			catch (ServerException ex) {
				String msg = ex.getMessage();
				System.out.println("Exception while processing request. " + msg);
				StreamUtil.sendError(msg, outputStream);
				StreamUtil.closeSocket(inputStream);
			}
			catch (Exception ex) {
				System.out.println("Exception while processing request. " + ex.getMessage());
				ex.printStackTrace();
				StreamUtil.closeSocket(inputStream);
			}
		}
	}

	private ServerCommand dispatchCommand(InputStream inputStream) throws ServerException
	{
		String commandString = StreamUtil.readLine(inputStream);

		if ("write".equalsIgnoreCase(commandString)) {
			ServerCommand serverCommand = new WriteServerCommand(); //WriteServerCommand()
			return serverCommand;
		}
		else if ("read".equalsIgnoreCase(commandString)) {
			ServerCommand serverCommand = new ReadServerCommand();//ReadServerCommand()
			return serverCommand;
		}
		else if ("delete".equalsIgnoreCase(commandString)) {
			ServerCommand serverCommand = new DeleteServerCommand();//DeleteServerCommand()
			return serverCommand;
		}
		else if ("directory".equalsIgnoreCase(commandString)) {
			ServerCommand serverCommand = new DirectoryServerCommand();//DirectoryServerCommand()
			return serverCommand;
		}
		else {
			throw new ServerException("Unknown Request: " + commandString);
		}
	}

	public static void main(String args[])
	{
		DatastoreServer server = new DatastoreServer();
		try {
			server.startup();
		}
		catch (IOException ex) {
			System.out.println("Unable to start server. " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
