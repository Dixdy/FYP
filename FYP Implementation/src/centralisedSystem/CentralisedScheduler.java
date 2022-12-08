package centralisedSystem;

import java.util.ArrayList;

public class CentralisedScheduler {

	private ArrayList<ServerInfo> serverList;
	
	public CentralisedScheduler(ArrayList<ServerInfo> serverList) {
		this.serverList = new ArrayList<ServerInfo>();
		for (ServerInfo server : serverList) {
			this.serverList.add(server);
		}
	}
	
	public ReturnInfo scheduleTask(ArrayList<TaskInfo> originalSeq, TaskInfo R, int targetServer) {
		ReturnInfo returnInfo = new ReturnInfo(scheduler1(originalSeq, R, targetServer));
		return returnInfo;
	}
	
	private ReturnInfo scheduler1(ArrayList<TaskInfo> originalSeq, TaskInfo R, int targetServer) {
		
		System.out.println("Scheduler: Attempting to schedule task on server " + targetServer);
		
		int inputTasks = originalSeq.size() + 1;
		//Algo1 line 1
		float tasksCompletedPercentage = 0;
		double avgCompletionTime = 9999999;
		ArrayList<TaskInfo> bestSeq = null;
		ReturnInfo returnInfo = null;
		
		//Set upload and download latency
		if (targetServer == 0) {
			R.setComputingTime(0);
		}
		else {
			//divide by upload speed then divide by 10 again to display properly and change to double
			R.setUploadLatency(R.getDataToProcess()/this.serverList.get(targetServer).getServerUploadSpeed()/10.0);
			//divide by computing speed then divide by 10 again to display properly and in double
			R.setComputingTime(R.getDataToProcess()/this.serverList.get(targetServer).getServerComputingSpeed()/10.0);
		}
		R.setUploadLatency(this.serverList.get(targetServer).getServerDownloadSpeed());
		
		//Algo1 line 2
		ArrayList<TaskInfo> newSeq = new ArrayList<TaskInfo>();
		for (TaskInfo task : originalSeq) {
			newSeq.add(new TaskInfo(task));	
		}
		newSeq.add(new TaskInfo(R));
		
		//Algo1 line 3-5
		if (isFeasible(newSeq)) {
			bestSeq = new ArrayList<TaskInfo>();
			for (TaskInfo task : newSeq) {
				bestSeq.add(new TaskInfo(task));
			}
			
			avgCompletionTime = calcACT(newSeq, targetServer);
			tasksCompletedPercentage = bestSeq.size()/inputTasks;
		}
		
		//Algo1 line 6-10
		for (int i=originalSeq.size()-1; i>=0; i--) {
			newSeq = new ArrayList<TaskInfo>();
			for (TaskInfo task : originalSeq) {
				newSeq.add(new TaskInfo(task));	
			}
			newSeq.add(i, new TaskInfo(R));
			
			if (isFeasible(newSeq) && (calcACT(newSeq, targetServer)<avgCompletionTime)) {
				bestSeq = new ArrayList<TaskInfo>();
				for (TaskInfo task : newSeq) {
					bestSeq.add(new TaskInfo(task));
				}
				
				avgCompletionTime = calcACT(newSeq, targetServer);
				tasksCompletedPercentage = bestSeq.size()/inputTasks;
			}
		}
		
		//Algo1 line 11-12
		if (bestSeq == null) {
			System.out.println("Task replaced!");
			returnInfo = new ReturnInfo(scheduler2(originalSeq, R, targetServer));
		}
		else {
			System.out.printf("Scheduler: No tasks replaced! Score: N=%.0f%%, ACT=%.2f\n", tasksCompletedPercentage*100, avgCompletionTime);
			returnInfo = new ReturnInfo(bestSeq, tasksCompletedPercentage, avgCompletionTime);
		}
		
		return returnInfo;
	}

	private ReturnInfo scheduler2(ArrayList<TaskInfo> originalSeq, TaskInfo R, int targetServer) {
		
		//Algo2 line 1-2
		float tasksCompletedPercentage = originalSeq.size()/(originalSeq.size()+1);
		double avgCompletionTime = calcACT(originalSeq, targetServer);
		ArrayList<TaskInfo> bestSeq = new ArrayList<TaskInfo>();
		for (TaskInfo task : originalSeq) {
			bestSeq.add(new TaskInfo(task));	
		}
		
		//Algo2 line 3
		for (int i=0; i<originalSeq.size(); i++) {
			
			//Algo2 line 4
			if (overlappingTransmissionLinkOrSameServer(R, originalSeq.get(i))) {
			
				//Algo2 line 5-6
				if(startedProcessing(originalSeq.get(i))) {
					continue;
				}
				
				//Algo2 line 7
				ArrayList<TaskInfo> newSeq = new ArrayList<TaskInfo>();
				for (int j=0; j<originalSeq.size(); j++) {
					if (j==i) {
						newSeq.add(new TaskInfo(R));
					}
					else {
						newSeq.add(new TaskInfo(originalSeq.get(j)));
					}
				}				
				
				//Algo2 line 8-10
				if (isFeasible(newSeq) && (calcACT(newSeq, targetServer)<avgCompletionTime)) {
					bestSeq = new ArrayList<TaskInfo>();
					for (TaskInfo task : newSeq) {
						bestSeq.add(new TaskInfo(task));	
					}
//					bestSeq = new ArrayList<TaskInfo>(newSeq);
					
					avgCompletionTime = calcACT(newSeq, targetServer);
				}
			}
		}
		
		ReturnInfo returnInfo = new ReturnInfo(bestSeq, tasksCompletedPercentage, avgCompletionTime);
		return returnInfo;
	}

	private boolean overlappingTransmissionLinkOrSameServer(TaskInfo R, TaskInfo taskToReplace) {
		// TODO Auto-generated method stub
		//Assume have overlapping links or share server
		return true;
	}
	
	private boolean startedProcessing(TaskInfo taskToReplace) {
		// TODO Auto-generated method stub
		//Assume have not start processing
		return false;
	}

	private boolean isFeasible(ArrayList<TaskInfo> newSeq) {
		// TODO Auto-generated method stub
		// calculate if enough bandwidth
		setStartTimings(newSeq);
		return true;
	}

	private double calcACT(ArrayList<TaskInfo> newSeq, int targetServer) {
		int totalTasks = newSeq.size();
		double taskTimeToComplete = 0;
		double totalTime = 0;
		double avgTimeToComplete = 0;
		setWaitTimings(newSeq);
		//Completion time = arrival time + wait time + upload latency + computation time + download latency
		if (targetServer != 0) {
			for (int i=0; i<totalTasks; i++) {
				taskTimeToComplete = newSeq.get(i).getWaitTime() + newSeq.get(i).getUploadLatency() + newSeq.get(i).getComputingTime() + newSeq.get(i).getDownloadLatency();
				totalTime = totalTime + taskTimeToComplete;
			}
		}
		//Cloud completion time = arrival time + upload latency + computation time + download latency
		else {
			for (int i=0; i<totalTasks; i++) {
				taskTimeToComplete = newSeq.get(i).getUploadLatency() + newSeq.get(i).getComputingTime() + newSeq.get(i).getDownloadLatency();
				totalTime = totalTime + taskTimeToComplete;
			}
		}
		avgTimeToComplete = totalTime/totalTasks;
		return avgTimeToComplete;
	}
	
	private void setStartTimings(ArrayList<TaskInfo> newSeq) {
		double currentTime = 0;
		double earliestUploadFinishedTime = 0;
		double earliestAvailableComputingTime = 0;
		//Set earliest upload start times
		for (int i=0; i<newSeq.size(); i++) {
			if (currentTime < newSeq.get(i).getArrivalTime()) {
				currentTime = newSeq.get(i).getArrivalTime();
			}
			newSeq.get(i).setUploadStartTime(currentTime);
			currentTime = currentTime + newSeq.get(i).getUploadLatency();
		}
		//Set earliest computing start times
		for (int i=0; i<newSeq.size(); i++) {
			earliestUploadFinishedTime = newSeq.get(i).getUploadStartTime() + newSeq.get(i).getUploadLatency();
			if (earliestUploadFinishedTime > earliestAvailableComputingTime) {
				earliestAvailableComputingTime = earliestUploadFinishedTime;
			}
			newSeq.get(i).setComputingStartTime(earliestAvailableComputingTime);
			earliestAvailableComputingTime = earliestAvailableComputingTime + newSeq.get(i).getComputingTime();
		}
	}

	private void setWaitTimings(ArrayList<TaskInfo> newSeq) {
		for (int i=0; i<newSeq.size(); i++) {
			//wait time till uploadStart = uploadStart - arrivalTime
			double waitTime = (newSeq.get(i).getUploadStartTime() - newSeq.get(i).getArrivalTime())
					//wait time till computingStart = computingStart - (uploadStart + uploadLatency)
					+ (newSeq.get(i).getComputingStartTime() - (newSeq.get(i).getUploadStartTime() + newSeq.get(i).getUploadLatency()));
			newSeq.get(i).setWaitTime(waitTime);
		}
	}
}
