import java.util.ArrayList;

public class DataBase {
	private ArrayList<Record> records;

	public DataBase() {
		records = new ArrayList<Record>();

		records.add(new Record("Goran", "Helen", "Stig", "Allergikliniken", "Notallergi"));
		records.add(new Record("Helmer", "Anna-Britta", "Kurt", "Fingeravdelningen", "Stukat finger"));
		records.add(new Record("Anna", "Lars", "Greger", "Sjukdomskliniken", "Influensa"));
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
