
public class Record {
	private String patient, nurse, doctor, hospitalDivision, medicalData;
	
	public Record(String patient, String nurse, String doctor, String hospitalDivision, String medicalData){
		this.patient = patient;
		this.nurse = nurse;
		this.doctor = doctor;
		this.hospitalDivision = hospitalDivision;
		this.medicalData = medicalData;
	}
	
	public String getPatient() {
		return patient;
	}

	public String getNurse() {
		return nurse;
	}

	public String getDoctor() {
		return doctor;
	}

	public String getHospitalDivision() {
		return hospitalDivision;
	}

	public String getMedicalData() {
		return medicalData;
	}

	public void setNurse(String nurse) {
		this.nurse = nurse;
		
	}

	public void setData(String data) {
		medicalData = data;
	}

}
