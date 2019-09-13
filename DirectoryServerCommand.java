package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.*;

import utd.persistentDataStore.utils.*;

public class DirectoryServerCommand extends ServerCommand{
	
	@Override
	public void run() throws IOException, ServerException {
		
		List<String> resultList = new ArrayList<>();
		resultList = FileUtil.directory();
		int length = resultList.size();

		sendOK();
		StreamUtil.writeLine(length + "\n", outputStream);
		for(int i = 0; i < length; i++) {
			StreamUtil.writeLine(resultList.get(i), outputStream);
		}
	}

}
