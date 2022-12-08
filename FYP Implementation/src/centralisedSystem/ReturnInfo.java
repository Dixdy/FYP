package centralisedSystem;

import java.util.ArrayList;

public class ReturnInfo {
	private ArrayList<TaskInfo> newSeq = new ArrayList<TaskInfo>();
	private float tasksCompletedPercentage = 0;
	private double avgCompletionTime = 0;
	
	public ReturnInfo(ArrayList<TaskInfo> newSeq, float tasksCompletedPercentage, double avgCompletionTime) {
		this.newSeq = new ArrayList<TaskInfo>();
		for (TaskInfo task : newSeq) {
			this.newSeq.add(new TaskInfo(task));
		}
//		this.newSeq = new ArrayList<TaskInfo>(newSeq);
		
		this.tasksCompletedPercentage = tasksCompletedPercentage;
		this.avgCompletionTime = avgCompletionTime;
	}
	
	public float getTasksCompletedPercentage() {
		return this.tasksCompletedPercentage;
	}
	
	public double getAvgCompletionTime() {
		return this.avgCompletionTime;
	}
	
	public ArrayList<TaskInfo> getNewSeq() {
		return this.newSeq;
	}
	
	public ReturnInfo(ReturnInfo returnInfo) {
		this.newSeq = new ArrayList<TaskInfo>();
		for (TaskInfo task : returnInfo.getNewSeq()) {
			newSeq.add(new TaskInfo(task));	
		}
		this.tasksCompletedPercentage = returnInfo.getTasksCompletedPercentage();
		this.avgCompletionTime = returnInfo.getAvgCompletionTime();
	}
}
