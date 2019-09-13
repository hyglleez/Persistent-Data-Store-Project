package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.*;

public class DeleteServerCommand extends ServerCommand{
	
	@Override
	public void run() throws IOException, ServerException {
		
		String name = StreamUtil.readLine(inputStream);
		//check the command name
		if (name == null || "".equals(name)) {
			sendError("Invalid command name.");
		}
		
		boolean isDeleted = FileUtil.deleteData(name);
		if (isDeleted == true) {
			sendOK();
		} else {
			sendError("Cannot find file: " + name);
		}
	}

}
