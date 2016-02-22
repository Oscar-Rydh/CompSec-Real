
public class Patient extends User {
	private String name;
	private DataBase database;
	private Log log;

	public Patient(DataBase database, String name, Log log, boolean firstTimeCreateLog) {
		this.name = name;
		this.database = database;
		this.log = log;
		if(firstTimeCreateLog){
			log.write("Patient: " + name + " successfully logged in.");
		}
	}
	// Expected command: getList
	public String getRecordListInfo() {
		StringBuilder sb = new StringBuilder();
		
		for(Record r : database.getRecords()){
			if(r.getPatient().equals(name)){
				sb.append("\tPatient: " + r.getPatient() + ": r");
			}
		}
		if(sb.length() == 0){
			sb.append("No data found");
		}
		log.write("Patient: " + name + " requested his/her record list.");
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
				log.write("Patient: " + name + " recieved his/her record of " + hospitalDivision);
				return 	"Patient: " + r.getPatient() +
						"\tNurse: " + r.getNurse() +
						"\tDoctor: " + r.getDoctor() +
						"\tHospital Division: " + r.getHospitalDivision() +
						"\tMedical Data: " + r.getMedicalData();
			}
		}
		log.write("Patient: " + name + " requested his/her record of " + hospitalDivision + ", but was not found.");
		return "Record not found.";
	}
	public String getPossibleCommands() {
		StringBuilder sb = new StringBuilder();
		sb.append("getList; Returns a list of accsessible recrods with read permissions\t");
		sb.append("getRecord 'Record name'; Returns the specified record\t");
		log.write("Patient: " + name + " requested a list of the availible commands.");
		return sb.toString();
	}
	
	public void unavailibleCommand(String command) {
		log.write("Patient: " + name + " sent an unimplemented command: " + command);
	}
}
