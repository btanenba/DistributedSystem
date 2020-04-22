package enlkbt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ClientMasterThread2 extends Thread {

	private Socket clientSocket;
	private PrintWriter clientWriter;
	private BufferedReader clientReader;
	private Shared share;
	private String request;

	SlaveA slaveA = new SlaveA();
	SlaveB slaveB = new SlaveB();

	public ClientMasterThread2(Socket clientSocket, Shared share) {

		this.clientSocket = clientSocket;

		this.share = share;
		try {
			clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen" + "for a connection");
			System.out.println(e.getMessage());
		}

	}

	public void run() {
		System.out.println("Created Connection and Entering ClientMasterThread2");

		while (true) {
			try {
				Thread.sleep(10);

				request = clientReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(request);

			Random rnd = new Random();
			if (rnd.nextInt() < 0.50) {

				ATask taskA = new ATask(request, 2);
				int x = 0;
				do {
					if (slaveA.getAvailibilty()) {
						share.waitingA.add(taskA);
						slaveA.setAvailabilty(false);

						x = 1;
					} else if (slaveB.getAvailibilty()) {
						share.waitingBTaskA.add(taskA);
						slaveB.setAvailabilty(false);

						x = 1;
					} else
						System.out.println("A task both A and B are busy");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				} while (x == 0);

			} else {
				int y = 0;

				BTask taskB = new BTask(request, 2);
				do {
					if (slaveB.getAvailibilty()) {
						share.waitingB.add(taskB);
						slaveB.setAvailabilty(false);

						y = 1;
					} else if (slaveA.getAvailibilty()) {
						share.waitingATaskB.add(taskB);
						slaveA.setAvailabilty(false);

						y = 1;
					} else
						System.out.println("B task both A and B are unavailable ");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (y == 0);

			}

		}
	}

}
