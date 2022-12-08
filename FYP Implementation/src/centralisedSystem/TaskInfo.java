package centralisedSystem;

public class TaskInfo {

	private int taskNumber;
	private int dataToProcess;
	private double arrivalTime;
	private double waitTime;
	private double uploadLatency;
	private double computingTime;
	private double downloadLatency;
	private double deadline;
	private double uploadStartTime;
	private double computingStartTime;
	
	public TaskInfo(int taskNumber, int dataToProcess, double arrivalTime, double deadline) {
		this.taskNumber = taskNumber;
		this.dataToProcess = dataToProcess;
		this.deadline = deadline;
		this.arrivalTime = arrivalTime;
	}
	
	public int getTaskNumber() {
		return this.taskNumber;
	}
	
	public int getDataToProcess() {
		return this.dataToProcess;
	}
	
	public double getArrivalTime() {
		return this.arrivalTime;
	}
	
	public double getUploadLatency() {
		return this.uploadLatency;
	}
	
	public double getComputingTime() {
		return this.computingTime;
	}
	
	public double getDownloadLatency() {
		return this.downloadLatency;
	}
	
	public double getDeadline() {
		return this.deadline;
	}

	public void setUploadLatency(double uploadLatency2) {
		this.uploadLatency = uploadLatency2;
	}

	public void setComputingTime(double computingTime) {
		this.computingTime = computingTime;
	}
	
	public void setDownloadLatency(double downloadLatency) {
		this.downloadLatency = downloadLatency;	
	}

	public double getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}

	public double getUploadStartTime() {
		return uploadStartTime;
	}

	public void setUploadStartTime(double uploadStartTime) {
		this.uploadStartTime = uploadStartTime;
	}

	public double getComputingStartTime() {
		return computingStartTime;
	}

	public void setComputingStartTime(double computingStartTime) {
		this.computingStartTime = computingStartTime;
	}
	
	public TaskInfo(TaskInfo taskInfo) {
		this.taskNumber = taskInfo.getTaskNumber();
		this.dataToProcess = taskInfo.getDataToProcess();
		this.arrivalTime = taskInfo.getArrivalTime();
		this.waitTime = taskInfo.getWaitTime();
		this.uploadLatency = taskInfo.getUploadLatency();
		this.computingTime = taskInfo.getComputingTime();
		this.downloadLatency = taskInfo.getDownloadLatency();
		this.deadline = taskInfo.getDeadline();
		this.uploadStartTime = taskInfo.getUploadStartTime();
		this.computingStartTime = taskInfo.getComputingStartTime();
	}
}
