import java.util.ArrayList;

public class DataBase {
	private ArrayList<Record> records;
	
	public DataBase() {
		records = new ArrayList<Record>();
		
		records.add(new Record("Göran", "Helen", "Stig", "Allergikliniken", "Nötallergi"));
		records.add(new Record("Helmer", "Anna-Britta", "Kurt", "Fingeravdelningen", "Stukat finger"));
		records.add(new Record("Anna", "Lars", "Greger", "Sjukdomskliniken", "Influensa"));
	}
	
	public ArrayList<Record> getRecords(){
		return records;
	}
}
