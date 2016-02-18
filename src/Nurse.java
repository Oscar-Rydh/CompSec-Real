
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
				sb.append("\tPatient: " + r.getPatient() + ": r+w");
			}else if(r.getHospitalDivision().equals(hospitalDivision)){
				sb.append("\tPatient: " + r.getPatient() + ": r");
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
			if(r.getPatient().equals(recordName) && r.getHospitalDivision().equals(hospitalDivision)){
				return 	"Patient: " + r.getPatient() +
						"\tNurse: " + r.getNurse() +
						"\tDoctor: " + r.getDoctor() +
						"\tHospital Division: " + r.getHospitalDivision() +
						"\tMedical Data: " + r.getMedicalData();
			}
		}
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
					return "Record modified";
				}
			}
		}
		return "Record not found";
	}
	public String getPossibleCommands() {
		StringBuilder sb = new StringBuilder();
		sb.append("getList; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("getRecord 'Record name'; Returns a list of accsessible recrods with read/wright permissions\t");
		sb.append("modifyRecord 'Patient':'Nurse':'data'; Edits the data in entered fields. Use '-' if no edit is wanted. Notice that ':' is required!!\t");
		return sb.toString();
	}
}
