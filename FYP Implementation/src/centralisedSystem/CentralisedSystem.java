package centralisedSystem;

import java.util.ArrayList;
import java.util.Scanner;

public class CentralisedSystem {

	private final ArrayList<ServerInfo> serverList = new ArrayList<ServerInfo>();
	private ArrayList<ArrayList<TaskInfo>> serverListTaskSeq = new ArrayList<ArrayList<TaskInfo>>();
	private ArrayList<TaskInfo> taskList = new ArrayList<TaskInfo>();
	
	public static void main(String[] args) throws Exception {
        new CentralisedSystem();
    }
	
	private CentralisedSystem() throws Exception {
        int width = 1000;
        int height = 500;
        int xMargin = 50;
        int yMargin = 50;
        GraphPlot graphPlot = new GraphPlot(width, height, xMargin, yMargin);

		System.out.println("-----Centralised System-----");
		
		InitialiseManager initManager = new InitialiseManager(serverList, serverListTaskSeq, taskList);
		//Run from current directory
		initManager.initialiseServerList(System.getProperty("user.dir") + "/serversList.xml");
		initManager.initialiseServerListTaskSeq(System.getProperty("user.dir") + "/serversTaskSeq.xml");
		initManager.initialiseTaskList(System.getProperty("user.dir") + "/taskList.xml");
		//Run from absolute path
//		initManager.initialiseServerList("C:/Users/Dixdy/Desktop/School Documents/FYP/FYP/serversList.xml");
//		initManager.initialiseServerListTaskSeq("C:/Users/Dixdy/Desktop/School Documents/FYP/FYP/serversTaskSeq.xml");
//		initManager.initialiseTaskList("C:/Users/Dixdy/Desktop/School Documents/FYP/FYP/taskList.xml");
		
		for (int i=0; i<serverList.size(); i++) {
		    graphPlot.generateGraph("Original", serverListTaskSeq.get(i), serverList.get(i).getServerName());
		}
	        
		CentralisedDispatcher dispatcher = new CentralisedDispatcher(serverList, serverListTaskSeq);
		

		//Scanner myObj = new Scanner(System.in);
		//myObj.close();
		
		int targetServer = 0;
		while(!taskList.isEmpty()) {
			//String str = myObj.next();

			System.out.println("");
			System.out.printf("Centralised System: Sending task %d to dispatcher...\n", taskList.get(0).getTaskNumber());
			
			targetServer = dispatcher.dispatchTask(taskList.get(0));
			
			if (targetServer == 0) {
				System.out.printf("Centralised System: Task %s has been dispatched to the cloud server.\n", taskList.get(0).getTaskNumber());
			}
			else {
				System.out.printf("Centralised System: Task %s has been dispatched to edge server %d.\n", taskList.get(0).getTaskNumber(), targetServer);
			}
			taskList.remove(0);

			
			ArrayList<ArrayList<TaskInfo>> updatedServerListTaskSeq = new ArrayList<ArrayList<TaskInfo>>(dispatcher.updateServersTaskSeq());
			this.serverListTaskSeq = new ArrayList<ArrayList<TaskInfo>>();
			for (ArrayList<TaskInfo> serverTaskSeq : updatedServerListTaskSeq) {
				ArrayList<TaskInfo> temp = new ArrayList<TaskInfo>();
				for (TaskInfo task : serverTaskSeq) {
					temp.add(new TaskInfo(task));
				}
				this.serverListTaskSeq.add(new ArrayList<TaskInfo>(temp));
			}
			
//			for (int i=0; i<serverList.size(); i++) {
//			    graphPlot.generateGraph("Final", serverListTaskSeq.get(i), serverList.get(i).getServerName());
//			}	
		}
		
		System.out.println("\nCentralised System: All tasks completed, shutting down...");
		System.out.println("-----END-----");
		
		for (int i=0; i<serverList.size(); i++) {
		    graphPlot.generateGraph("Final", serverListTaskSeq.get(i), serverList.get(i).getServerName());
		}
	    
	}
}
