package com.health.jbpm.model;

public class AppointmentDTO {

	private String patientName;
	private String doctorName;
	private String issue;
	private String apptdate;

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getApptdate() {
		return apptdate;
	}

	public void setApptdate(String apptdate) {
		this.apptdate = apptdate;
	}

}
