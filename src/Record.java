
public class Record {
	private String name, nurse, doctor, hospitalDivision, medicalData;
	
	public Record(String name, String nurse, String doctor, String hospitalDivision, String medicalData){
		this.name = name;
		this.nurse = nurse;
		this.doctor = doctor;
		this.hospitalDivision = hospitalDivision;
		this.medicalData = medicalData;
	}
	
	public String getName() {
		return name;
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

}
