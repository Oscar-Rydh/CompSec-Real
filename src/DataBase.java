import java.util.ArrayList;

public class DataBase {
	private ArrayList<Record> records;
	
	public DataBase() {
		records = new ArrayList<Record>();
	}
	
	public ArrayList<Record> getRecords(){
		return records;
	}
}
