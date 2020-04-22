package enlkbt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MasterSlaveAThread extends Thread {

	private Socket slaveSocket;
	private PrintWriter slaveWriter;
	private BufferedReader slaveReader;

	private ObjectOutputStream os;
	private ObjectInputStream is;

	private Shared share;
	private String answer;

	private SlaveA slaveA = new SlaveA();

	public MasterSlaveAThread(Socket slaveSocket, Shared share) {

		this.slaveSocket = slaveSocket;

		this.share = share;

		try {
			slaveWriter = new PrintWriter(slaveSocket.getOutputStream(), true);
			slaveReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));

			os = new ObjectOutputStream(slaveSocket.getOutputStream());
			is = new ObjectInputStream(slaveSocket.getInputStream());

		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen" + "for a connection");
			System.out.println(e.getMessage());
		}

	}

	public void run() {
		System.out.println("Created Connection and Entering MasterSlaveA Thread");

		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}

			while (!share.waitingA.isEmpty() || !share.waitingATaskB.isEmpty()) {
				if (!share.waitingA.isEmpty()) {
					ATask task = share.waitingA.get(0);
					try {
						Thread.sleep(5000);
						os.writeObject(task);
						Thread.sleep(5000);
					} catch (IOException e) {

						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					share.waitingA.remove(0);

					try {
						answer = slaveReader.readLine();
						slaveA.setAvailabilty(true);

					} catch (IOException e) {
						e.printStackTrace();
					}
					task.setResult(answer);
					share.finished.add(task);
				} else {
					BTask task = share.waitingATaskB.get(0);
					try {
						Thread.sleep(5000);
						os.writeObject(task);
						Thread.sleep(5000);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					share.waitingATaskB.remove(0);

					try {
						answer = slaveReader.readLine();
						slaveA.setAvailabilty(true);

					} catch (IOException e) {

						e.printStackTrace();
					}
					task.setResult(answer);
					share.finished.add(task);

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}
		}
	}
}
