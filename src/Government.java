
public class Government extends User {
	private String name;
	private DataBase database;

	public Government(DataBase database, String name) {
		this.name = name;
		this.database = database;
	}

	// Expected command: getList
	public String getRecordListInfo() {
		StringBuilder sb = new StringBuilder();

		for (Record r : database.getRecords()) {
			sb.append("\tPatient: " + r.getPatient() + ": r+delete");
		}
		if(sb.length() == 0){
			sb.append("No data found");
		}
		return sb.toString();
	}

	// Expected command: getRecord 'Record name':'Hospital Division'
	public String getRecord(String command) {
			command = command.substring(command.indexOf(" ") + 1);
			String[] args = command.split(":");
			String patient = args[0];
			String hospitalDivision = args[1];
			
			for(Record r : database.getRecords()){
				if(r.getPatient().equals(patient) && r.getHospitalDivision().equals(hospitalDivision)){
					return 	"Patient: " + r.getPatient() +
							"\tNurse: " + r.getNurse() +
							"\tDoctor: " + r.getDoctor() +
							"\tHospital Division: " + r.getHospitalDivision() +
							"\tMedical Data: " + r.getMedicalData();
				}
			}
			return "Record not found.";
		}

	// Expected command: deleteRecord 'Record name':'Hospital Division'
	public String deleteRecord(String command){
		command = command.substring(command.indexOf(" ") + 1);
		String[] args = command.split(":");
		String patient = args[0];
		String hospitalDivision = args[1];
		
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(patient) && r.getHospitalDivision().equals(hospitalDivision)){
				database.remove(r);
				return "Record Removed";
			}
		}
		return "Record not found";
	}
}
