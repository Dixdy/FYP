package centralisedSystem;

import java.util.ArrayList;

public class CentralisedDispatcher {

	private ArrayList<ServerInfo> serverList;
	private ArrayList<ArrayList<TaskInfo>> serverListTaskSeq;
	private CentralisedScheduler scheduler;
	
	public CentralisedDispatcher(ArrayList<ServerInfo> serverList, ArrayList<ArrayList<TaskInfo>> serverListTaskSeq) {
		this.serverList = new ArrayList<ServerInfo>();
		for (ServerInfo server : serverList) {
			this.serverList.add(server);
		}
		
		this.serverListTaskSeq = new ArrayList<ArrayList<TaskInfo>>();
		for (ArrayList<TaskInfo> serverTaskSeq : serverListTaskSeq) {
			ArrayList<TaskInfo> temp = new ArrayList<TaskInfo>();
			for (TaskInfo task : serverTaskSeq) {
				temp.add(new TaskInfo(task));
			}
			this.serverListTaskSeq.add(new ArrayList<TaskInfo>(temp));
		}
		
		this.scheduler = new CentralisedScheduler(this.serverList);
	}
	
	public int dispatchTask(TaskInfo R) {
		//Algo3 line 1-3
		int targetServer = 0; //Cloud
		float tasksCompletionPercentageBest = 0;
		double avgCompletionTimeBest = 9999999;
		
		ArrayList<TaskInfo> bestSeq = new ArrayList<TaskInfo>();
		for (TaskInfo task : serverListTaskSeq.get(0) ) {
			bestSeq.add(new TaskInfo(task));
		}
		
		ReturnInfo returnInfo = null;
		
		//Algo3 line 4
		for (int i=0; i<serverList.size(); i++) {
			
			//Algo3 line 5-6
			if (i==0) {
				//If target is cloud
				//R.setComputingTime(0);
			}
			
			//Algo3 line line 7-8
			returnInfo = new ReturnInfo(scheduler.scheduleTask(serverListTaskSeq.get(i), R, i));
			
			//Algo3 line 9
			if (returnInfo.getTasksCompletedPercentage() > tasksCompletionPercentageBest ||
				((returnInfo.getTasksCompletedPercentage() == tasksCompletionPercentageBest) &&
				(returnInfo.getAvgCompletionTime() < avgCompletionTimeBest))) {
				
				if(i!=0) {
					System.out.printf("Dispatcher: Checking if task %d met deadline on server %d...\n", R.getTaskNumber(), i);
					//Algo3 line 10-13
					if (deadlineMet(R)) {
						targetServer = i;
						System.out.printf("Dispatcher: Task %d deadline met on server %d (N=%.0f%%, ACT=%.2f ms).\n",
								R.getTaskNumber(), targetServer, returnInfo.getTasksCompletedPercentage()*100, returnInfo.getAvgCompletionTime());
					}
					else {
						targetServer = 0;
						System.out.printf("Dispatcher: Task %d deadline not met on server %d (N=%.0f%%, ACT=%.2f ms), sending to cloud server...\n",
								R.getTaskNumber(), targetServer, returnInfo.getTasksCompletedPercentage()*100, returnInfo.getAvgCompletionTime());
					}
				}
				
				//Algo3 line 14
				tasksCompletionPercentageBest = returnInfo.getTasksCompletedPercentage();
				avgCompletionTimeBest = returnInfo.getAvgCompletionTime();
				
				bestSeq = new ArrayList<TaskInfo>();
				for (TaskInfo task : returnInfo.getNewSeq()) {
					bestSeq.add(new TaskInfo(task));
				}
			}
		}
		
		System.out.printf("Dispatcher: Best server = %d, Score: N=%.0f%%, ACT=%.2f ms\n", targetServer, tasksCompletionPercentageBest*100, avgCompletionTimeBest);
		
		for (int i=0; i<serverList.size(); i++) {
			if (i==targetServer) {
				ArrayList<TaskInfo> temp = new ArrayList<TaskInfo>();
				for (TaskInfo task : bestSeq) {
					temp.add(new TaskInfo(task));
				}
				this.serverListTaskSeq.add(temp);
			}
			else {
				ArrayList<TaskInfo> temp = new ArrayList<TaskInfo>();
				for (TaskInfo task : this.serverListTaskSeq.get(i)) {
					temp.add(new TaskInfo(task));
				}
				this.serverListTaskSeq.add(temp);
			}
		}
		
		for (int i=0; i<serverList.size(); i++) {
			this.serverListTaskSeq.remove(0);
		}
		
		return targetServer;
	}
	
	private boolean deadlineMet(TaskInfo R) {
		boolean met = false;
		double taskEndTime = R.getArrivalTime() + R.getWaitTime() + R.getUploadLatency() + R.getComputingTime() + R.getDownloadLatency();
		if (taskEndTime < R.getDeadline()) {
			met = true;
		}
		return met;
	}

	public ArrayList<ArrayList<TaskInfo>> updateServersTaskSeq() {
		return this.serverListTaskSeq;
	}
}
