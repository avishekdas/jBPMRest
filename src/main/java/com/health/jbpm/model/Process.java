package com.health.jbpm.model;

public class Process {

	String processid;
	String processname;
	String packagename;
	String containerid;

	// Getters and setters

	@Override
	public String toString() {
		return "Process [processid=" + processid + ", processname=" + processname + ", packagename=" + packagename
				+ ", containerid=" + containerid + "]";
	}

	public String getProcessid() {
		return processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	public String getProcessname() {
		return processname;
	}

	public void setProcessname(String processname) {
		this.processname = processname;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getContainerid() {
		return containerid;
	}

	public void setContainerid(String containerid) {
		this.containerid = containerid;
	}
}
