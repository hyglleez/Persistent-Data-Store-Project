package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;


import utd.persistentDataStore.utils.*;

public class WriteServerCommand extends ServerCommand{
	
	@Override
	public void run() throws IOException, ServerException {
		
		String name = StreamUtil.readLine(inputStream);
		//check the command name
		if (name == null || "".equals(name)) {
			sendError("Invalid command name.");
		}
		
		String dataSize = StreamUtil.readLine(inputStream);
		if (dataSize == null || "".equals(dataSize)) {
			sendError("No data size.");
		}
		
		int Length = Integer.parseInt(dataSize);
		byte[] data = StreamUtil.readData(Length, inputStream);
		FileUtil.writeData(name, data);
		sendOK();	

	}

}
