
public class Doctor implements User {
	private String name;
	private DataBase database;

	public Doctor(DataBase database, String name) {
		this.name = name;
		this.database = database;
	}

	public String getRecordListInfo() {
		StringBuilder sb = new StringBuilder();
		
		for(Record r : database.getRecords()){
			//TODO Print only parts that this class have access to.
			sb.append(r.getName() + r.getNurse() + r.getDoctor() + r.getHospitalDivision() + r.getMedicalData() + "\n");
		}
		
		return sb.toString();
	}

	public String getRecord(String recordName) {

		return null;
	}
}
