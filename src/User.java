import java.io.IOException;


public abstract class User {
	
	public abstract String getRecordListInfo() throws IOException;
	
	public abstract String getRecord(String recordName) throws IOException;
	
	public String createRecord(String command) throws IOException{
		return "This Command is not possible for this user";
	};
	
	public String modifyRecord(String command) throws IOException{
		return "This Command is not possible for this user";
	};
	
	public String deleteRecord(String command) throws IOException{
		return "This Command is not possible for this user";
	}
	public abstract String getPossibleCommands() throws IOException; 
	
	public abstract void unavailibleCommand(String command) throws IOException;
}
