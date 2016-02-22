
public class Doctor extends User {
	private String name, hospitalDivision;
	private DataBase database;
	private Log log;

	public Doctor(DataBase database, String name, String hospitalDivision, Log log, boolean firstTimeCreateLog) {
		this.name = name;
		this.database = database;
		this.hospitalDivision = hospitalDivision;
		this.log = log;
		if(firstTimeCreateLog){
			log.write("Doctor: " + name + " of " + hospitalDivision + " successfully logged in.");
		}
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
		log.write("Doctor: " + name + " of " + hospitalDivision + " requested the record list.");
		return sb.toString();
	}
	
	// Expected command: getRecord 'Record name'
	public String getRecord(String recordName) {
		recordName = recordName.substring(recordName.indexOf(" ") + 1);
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(recordName) && r.getDoctor().equals(name) ||
					r.getPatient().equals(recordName) && r.getHospitalDivision().equals(hospitalDivision)){
				log.write("Doctor: " + name + " of " + hospitalDivision + " recieved record of Patient: " + recordName);
				return 	"Patient: " + r.getPatient() +
						"\tNurse: " + r.getNurse() +
						"\tDoctor: " + r.getDoctor() +
						"\tHospital Division: " + r.getHospitalDivision() +
						"\tMedical Data: " + r.getMedicalData();
			}
		}
		log.write("Doctor: " + name + " of " + hospitalDivision + " requested record of Patient: " + recordName + ", but was not found.");
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
		log.write("Doctor: " + name + " of " + hospitalDivision + " created a record for Patient: " + patient + " with assigned Nurse: " + nurse + " and Medical Data: " + data);
		return "Record Created";
	}
	
	// modifyRecord 'patient':'nurse':'data' om ingen skillnad 'patient':-:'data'
	public String modifyRecord(String command) {
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
			log.write("Doctor: " + name + " of " + hospitalDivision + " modified record of Patient: " + patient + " to Nurse: " + nurse + " and Medical Data: " + data);
			return "Record modfied";
		}
		log.write("Doctor: " + name + " of " + hospitalDivision + " tried to modify record of Patient: " + patient + " but something went wrong.");	
		return "Something went wrong";
	}
	@Override
	public String getPossibleCommands() {
		StringBuilder sb = new StringBuilder();
		sb.append("getList; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("getRecord 'Record name'; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("createRecord 'Patient':'Nurse':'data'; Creates a new Record with entered data. Notice that ':' is required!!\t");
		sb.append("modifyRecord 'Patient':'Nurse':'data'; Edits the data in entered fields. Use '-' if no edit is wanted. Notice that ':' is required!!\t");
		log.write("Doctor: " + name + " of " + hospitalDivision + " requested a list of the availible commands.");
		return sb.toString();
	}
	
	@Override
	public void unavailibleCommand(String command) {
		log.write("Doctor: " + name + " of " + hospitalDivision + " sent an unimplemented command: " + command);
	}
}
