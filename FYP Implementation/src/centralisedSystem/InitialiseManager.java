package centralisedSystem;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InitialiseManager {

	private ArrayList<ServerInfo> serverList;
	private ArrayList<ArrayList<TaskInfo>> serverListTaskSeq;
	private ArrayList<TaskInfo> taskList;
	private int taskNumber = 1;
	
	public InitialiseManager(ArrayList<ServerInfo> serverList, ArrayList<ArrayList<TaskInfo>> serverListTaskSeq, ArrayList<TaskInfo> taskList) {
		this.serverList = serverList;
		this.serverListTaskSeq = serverListTaskSeq;
		this.taskList = taskList;
	}
	
	public void initialiseServerList(String file) throws Exception {
		System.out.println("InitManager: Initialising Servers...");
		StringBuilder sb = new StringBuilder();
		
		//Read xml file
		File serversFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(serversFile);
		NodeList serverNodeList = doc.getElementsByTagName("server");
		
		//Add to serverList array
		for (int i=0; i<serverNodeList.getLength(); i++) {
			Node taskNode = serverNodeList.item(i);
			Element taskElement = (Element) taskNode;
			
			String serverName = taskElement.getElementsByTagName("name").item(0).getTextContent();
			int serverNumber = i;
			double serverComputingSpeed = Double.parseDouble(taskElement.getElementsByTagName("serverComputingSpeed").item(0).getTextContent());			
			double serverUploadSpeed = Double.parseDouble(taskElement.getElementsByTagName("uploadSpeed").item(0).getTextContent());			
			double serverDownloadSpeed = Double.parseDouble(taskElement.getElementsByTagName("downloadSpeed").item(0).getTextContent());			
			
			serverList.add(new ServerInfo(serverName, serverNumber, serverComputingSpeed, serverUploadSpeed, serverDownloadSpeed));
			sb.append(serverName + ", ");
		}
		
		sb.append("servers added.");
		System.out.println("InitManager: " + sb.toString());
	}
	
	public void initialiseServerListTaskSeq(String file) throws Exception {
		System.out.println("InitManager: Initialising Servers Current Schedule...");
		
		//Read xml file
		File serverTaskSeqFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(serverTaskSeqFile);
		NodeList taskSeqNodeList = doc.getElementsByTagName("taskSeq");
		
		//Split into each server's taskSeq
		for (int i=0; i<taskSeqNodeList.getLength(); i++) {
			Node taskSeqNode = taskSeqNodeList.item(i);
			Element taskSeqElement = (Element) taskSeqNode;
			NodeList tasks = taskSeqElement.getElementsByTagName("task");
			ArrayList<TaskInfo> newSeq = new ArrayList<TaskInfo>();
			
			//Add to server's taskSeq array
			for (int j=0; j<tasks.getLength(); j++) {
				Node taskNode = tasks.item(j);
				Element taskElement = (Element) taskNode;
				
				int dataToProcess = Integer.parseInt(taskElement.getElementsByTagName("dataToProcess").item(0).getTextContent());
				double arrivalTime = Double.parseDouble(taskElement.getElementsByTagName("arrivalTime").item(0).getTextContent());
				double deadline = Double.parseDouble(taskElement.getElementsByTagName("deadline").item(0).getTextContent());
				double waitTime = Double.parseDouble(taskElement.getElementsByTagName("waitTime").item(0).getTextContent());
				double uploadStartTime = Double.parseDouble(taskElement.getElementsByTagName("uploadStartTime").item(0).getTextContent());
				double computingStartTime = Double.parseDouble(taskElement.getElementsByTagName("computingStartTime").item(0).getTextContent());
				double uploadLatency = 0;
				double downloadLatency = serverList.get(i).getServerDownloadSpeed();
				double computingTime;
				if (i==0) {
					uploadLatency = serverList.get(i).getServerUploadSpeed();
					computingTime = 0;
				}
				else {
					//divide by upload speed then divide by 10 again to display properly and change to double
					uploadLatency = dataToProcess/serverList.get(i).getServerUploadSpeed()/10.0;
					//divide by computing speed then divide by 10 again to display properly and change to double
					computingTime = dataToProcess/serverList.get(i).getServerComputingSpeed()/10.0;
				}
				
				TaskInfo task = new TaskInfo(taskNumber, dataToProcess, arrivalTime, deadline);
				task.setUploadLatency(uploadLatency);
				task.setComputingTime(computingTime);
				task.setDownloadLatency(downloadLatency);
				task.setWaitTime(waitTime);
				task.setUploadStartTime(uploadStartTime);
				task.setComputingStartTime(computingStartTime);
				newSeq.add(task);
				taskNumber++;
			}
			
			//Add to serverListTaskSeq array
			serverListTaskSeq.add(newSeq);
		}
		
		System.out.println("InitManager: All servers task list initialsied.");
	}

	public void initialiseTaskList(String file) throws Exception{
		System.out.println("InitManager: Initialising Tasks to be added...");
		int numOfTasks = 0;
		
		//Read xml file
		File taskListFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(taskListFile);
		NodeList taskListNodeList = doc.getElementsByTagName("task");
		
		//Add to taskList array
		for (int i=0; i<taskListNodeList.getLength(); i++) {
			numOfTasks++;
			Node taskNode = taskListNodeList.item(i);
			Element taskElement = (Element) taskNode;
			
			int dataToProcess = Integer.parseInt(taskElement.getElementsByTagName("dataToProcess").item(0).getTextContent());
			double arrivalTime = Double.parseDouble(taskElement.getElementsByTagName("arrivalTime").item(0).getTextContent());
			double deadline = Double.parseDouble(taskElement.getElementsByTagName("deadline").item(0).getTextContent());
			
			taskList.add(new TaskInfo(taskNumber, dataToProcess, arrivalTime, deadline));
			taskNumber++;
		}
		
		System.out.printf("InitManager: %d tasks added.\n", numOfTasks);
	}
}
