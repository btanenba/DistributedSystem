package enlkbt;

import java.io.Serializable;

public class Task implements Serializable {

	private String work;
	private String result;
	private int clientID;

	public Task(String work, int clientID) {
		this.work = work;
		this.clientID = clientID;
		this.result = null;

	}

	public String getTask() {
		return work;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public int getID() {
		return clientID;
	}

}
