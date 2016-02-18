
public class Doctor extends User {
	private String name, hospitalDivision;
	private DataBase database;

	public Doctor(DataBase database, String name, String hospitalDivision) {
		this.name = name;
		this.database = database;
		this.hospitalDivision = hospitalDivision;
	}
	// Expected command: getList
	public String getRecordListInfo() {
		StringBuilder sb = new StringBuilder();
		
		for(Record r : database.getRecords()){
			if(r.getDoctor().equals(name)){
				sb.append("Patient: " + r.getPatient() + ": r+w");
			}else if(r.getHospitalDivision().equals(hospitalDivision)){
				sb.append("Patient: " + r.getPatient() + ": r");
			}
		}
		if(sb.length() == 0){
			sb.append("No data found");
		}
		return sb.toString();
	}
	
	// Expected command: getRecord 'Record name'
	public String getRecord(String recordName) {
		recordName = recordName.substring(recordName.indexOf(" ") + 1);
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(recordName) && r.getDoctor().equals(name) ||
					r.getPatient().equals(recordName) && r.getHospitalDivision().equals(hospitalDivision)){
				return 	"Patient: " + r.getPatient() +
						"\tNurse: " + r.getNurse() +
						"\tDoctor: " + r.getDoctor() +
						"\tHospital Division: " + r.getHospitalDivision() +
						"\tMedical Data: " + r.getMedicalData();
			}
		}
		return "Record not found.";
	}
	
	//Expected command: createRecord "'patient':'nurse':'data'"
	public String createRecord(String command){
		command = command.substring(command.indexOf(" ") + 1);
		String[] args = command.split(":");
		String patient = args[0];
		String nurse = args[1];
		String data = args[2];
		database.add(new Record(patient, nurse, name, hospitalDivision, data));
		return "Record Created";
	}
	
	// modifyRecord 'patient':'nurse':'data' om ingen skillnad 'patient':-:'data'
	public String modifyRecord(String command){
		command = command.substring(command.indexOf(" ") + 1);
		String[] args = command.split(":");
		String patient = args[0];
		String nurse = args[1];
		String data = args[2];
		int modified = 0;
		for(Record r: database.getRecords()){
			if(patient != null && r.getPatient().equals(patient) && r.getDoctor().equals(name)){
				if(!nurse.equals("-")){
					r.setNurse(nurse);
					modified++;
				}
				if(!data.equals("-")){
					r.setData(data);
					modified++;
				}
			}
		}
		if(modified >= 1){
			return "Record modfied";
		}
			return "Something went wrong";
	}
	@Override
	public String getPossibleCommands() {
		StringBuilder sb = new StringBuilder();
		sb.append("getList; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("getRecord 'Record name'; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("createRecord 'Patient':'Nurse':'data'; Creates a new Record with entered data. Notice that ':' is required!!\t");
		sb.append("modifyRecord 'Patient':'Nurse':'data'; Edits the data in entered fields. Use '-' if no edit is wanted. Notice that ':' is required!!\t");
		return sb.toString();
	}
}
