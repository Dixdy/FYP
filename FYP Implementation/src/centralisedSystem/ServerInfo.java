package centralisedSystem;

public class ServerInfo {
	private String serverName;
	private int serverNumber;
	private double uploadSpeed;
	private double downloadSpeed;
	private double serverComputingSpeed;
	
	public ServerInfo(String serverName, int serverNumber, double serverComputingSpeed, double uploadSpeed, double downloadSpeed) {
		this.serverName = serverName;
		this.serverNumber = serverNumber;
		this.serverComputingSpeed = serverComputingSpeed;
		this.uploadSpeed = uploadSpeed;
		this.downloadSpeed = downloadSpeed;
	}

	public String getServerName() {
		return serverName;
	}

	public double getServerComputingSpeed() {
		return this.serverComputingSpeed;
	}
	
	public double getServerUploadSpeed() {
		return this.uploadSpeed;
	}
	
	public double getServerDownloadSpeed() {
		return this.downloadSpeed;
	}

	public int getServerNumber() {
		return serverNumber;
	}
}
