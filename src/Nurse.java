
public class Nurse extends User {
	private String name, hospitalDivision;
	private DataBase database;

	public Nurse(DataBase database, String name, String hospitalDivision) {
		this.name = name;
		this.database = database;
		this.hospitalDivision = hospitalDivision;
	}
	// Expected command: getList
	public String getRecordListInfo() {
		StringBuilder sb = new StringBuilder();
		
		for(Record r : database.getRecords()){
			if(r.getNurse().equals(name)){
				sb.append("Patient: " + r.getPatient() + ": r+w");
			}else if(r.getHospitalDivision().equals(hospitalDivision)){
				sb.append("Patient: " + r.getPatient() + ": r");
			}
		}
		return sb.toString();
	}
	
	// Expected command: getRecord 'Record name'
	public String getRecord(String recordName) {
		recordName = recordName.substring(0, recordName.indexOf(" "));
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(recordName) && r.getHospitalDivision().equals(hospitalDivision)){
				return 	"Patient: " + r.getPatient() +
						"Nurse: " + r.getNurse() +
						"Doctor: " + r.getDoctor() +
						"Hospital Division: " + r.getHospitalDivision() +
						"Medical Data: " + r.getMedicalData();
			}
		}
		return "Record not found.";
	}
	
	// modifyRecord 'patient':'data' 
	public void modifyRecord(String command){
		command.substring(0, command.indexOf(" "));
		String[] args = command.split(":");
		String patient = args[0];
		String data = args[1];
		
		for(Record r: database.getRecords()){
			if(patient != null && r.getPatient().equals(patient) && r.getNurse().equals(name)){
				if(!data.equals("-")){
					r.setData(data);
					
				}
			}
		}
	}
}
