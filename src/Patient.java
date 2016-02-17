
public class Patient extends User {
	private String name;
	private DataBase database;

	public Patient(DataBase database, String name) {
		this.name = name;
		this.database = database;
	}
	// Expected command: getList
	public String getRecordListInfo() {
		StringBuilder sb = new StringBuilder();
		
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(name)){
				sb.append("Patient: " + r.getPatient() + ": r");
			}
		}
		if(sb.length() == 0){
			sb.append("No data found");
		}
		return sb.toString();
	}
	
	// Expected command: getRecord 'Record name':'Hospital Division'
	public String getRecord(String command) {
		command = command.substring(0, command.indexOf(" "));
		String[] args = command.split(":");
		String patient = args[0];
		String hospitalDivision = args[1];
		
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(patient) && r.getHospitalDivision().equals(hospitalDivision)){
				return 	"Patient: " + r.getPatient() +
						"Nurse: " + r.getNurse() +
						"Doctor: " + r.getDoctor() +
						"Hospital Division: " + r.getHospitalDivision() +
						"Medical Data: " + r.getMedicalData();
			}
		}
		return "Record not found.";
	}
}
