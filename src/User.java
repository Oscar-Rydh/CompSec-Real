
public abstract class User {
	
	public abstract String getRecordListInfo();
	
	public abstract String getRecord(String recordName);
	
	public String createRecord(String command){
		return "This Command is not possible for this user";
	};
	
	public String modifyRecord(String command){
		return "This Command is not possible for this user";
	};
	
	public String deleteRecord(String command){
		return "This Command is not possible for this user";
	}
}
