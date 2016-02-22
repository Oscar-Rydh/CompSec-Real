
public class Nurse extends User {
	private String name, hospitalDivision;
	private DataBase database;
	private Log log;

	public Nurse(DataBase database, String name, String hospitalDivision, Log log, boolean firstTimeCreateLog) {
		this.name = name;
		this.database = database;
		this.hospitalDivision = hospitalDivision;
		this.log = log;
		if(firstTimeCreateLog){
			log.write("Nurse: " + name + " of " + hospitalDivision + " successfully logged in.");
		}
	}
	// Expected command: getList
	public String getRecordListInfo() {
		StringBuilder sb = new StringBuilder();
		
		for(Record r : database.getRecords()){
			if(r.getNurse().equals(name)){
				sb.append("\tPatient: " + r.getPatient() + ": r+w");
			}else if(r.getHospitalDivision().equals(hospitalDivision)){
				sb.append("\tPatient: " + r.getPatient() + ": r");
			}
		}
		if(sb.length() == 0){
			sb.append("No data found");
		}
		log.write("Nurse: " + name + " of " + hospitalDivision + " requested the record list.");
		return sb.toString();
	}
	
	// Expected command: getRecord 'Record name'
	public String getRecord(String recordName) {
		recordName = recordName.substring(recordName.indexOf(" ") + 1);
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(recordName) && r.getHospitalDivision().equals(hospitalDivision)){
				log.write("Nurse: " + name + " of " + hospitalDivision + " recieved record of Patient: " + recordName);
				return 	"Patient: " + r.getPatient() +
						"\tNurse: " + r.getNurse() +
						"\tDoctor: " + r.getDoctor() +
						"\tHospital Division: " + r.getHospitalDivision() +
						"\tMedical Data: " + r.getMedicalData();
			}
		}
		log.write("Nurse: " + name + " of " + hospitalDivision + " requested record of Patient: " + recordName + ", but was not found.");
		return "Record not found.";
	}
	
	// modifyRecord 'patient':'data' 
	public String modifyRecord(String command){
		command = command.substring(command.indexOf(" ") + 1);
		String[] args = command.split(":");
		String patient = args[0];
		String data = args[1];
		
		for(Record r: database.getRecords()){
			if(patient != null && r.getPatient().equals(patient) && r.getNurse().equals(name)){
				if(!data.equals("-")){
					r.setData(data);
					log.write("Nurse: " + name + " of " + hospitalDivision + " modified record of Patient: " + patient + " with Medical Data: " + data);
					return "Record modified";
				}
			}
		}
		log.write("Nurse: " + name + " of " + hospitalDivision + " tried to modify record of Patient: " + patient + " but something went wrong.");	
		return "Record not found";
	}
	public String getPossibleCommands() {
		StringBuilder sb = new StringBuilder();
		sb.append("getList; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("getRecord 'Record name'; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("modifyRecord 'Patient':'Nurse':'data'; Edits the data in entered fields. Use '-' if no edit is wanted. Notice that ':' is required!!\t");
		log.write("Nurse: " + name + " of " + hospitalDivision + " requested a list of the availible commands.");
		return sb.toString();
	}
	
	public void unavailibleCommand(String command) {
		log.write("Nurse: " + name + " of " + hospitalDivision + " sent an unimplemented command: " + command);
	}
}
