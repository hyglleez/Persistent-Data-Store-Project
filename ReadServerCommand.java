package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.*;

public class ReadServerCommand extends ServerCommand{
	
	@Override
	public void run() throws IOException, ServerException {
		
		String name = StreamUtil.readLine(inputStream);
		//check the command name
		if (name == null || "".equals(name)) {
			sendError("Invalid command name");
		}

		byte[] data = FileUtil.readData(name);

		//check whether file is empty
		if (data.length == 0) {
			sendError("File is empty.");
		} else {
			sendOK();
			StreamUtil.writeLine(Integer.toString(data.length) + "\n", outputStream);
			StreamUtil.writeData(data, outputStream);
		}
	}

}
