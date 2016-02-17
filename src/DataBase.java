import java.util.ArrayList;

public class DataBase {
	private ArrayList<Record> records;

	public DataBase() {
		records = new ArrayList<Record>();
		// Patient: Nurse: Doctor: Hospital Division: Data 
		records.add(new Record("Goran", "Helen", "Stig", "allergikliniken", "Notallergi"));
		records.add(new Record("Helmer", "Anna-Britta", "Kurt", "fingeravdelningen", "Stukat finger"));
		records.add(new Record("Anna", "Lars", "123123", "jdfsjhkdfskj", "Influensa"));

	}

	public ArrayList<Record> getRecords(){
		return records;
	}

	public void add(Record record) {
		records.add(record);
	}

	public void remove(Record record) {
		records.remove(record);
		
	}
}
