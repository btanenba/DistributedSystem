package enlkbt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientMasterThreadResponse2 extends Thread {

	private Socket clientSocket;
	private PrintWriter clientWriter;
	private BufferedReader clientReader;
	private Shared share;

	public ClientMasterThreadResponse2(Socket clientSocket, Shared share) {

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
		System.out.println("Created Connection and Entering ClientMasterThreadResponse2");

		while (true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			getResults();
		}
	}

	public void getResults() {

		if (!share.finished.isEmpty()) {
			synchronized (share.finished) {
				for (int i = 0; i < share.finished.size(); i++) {
					if (share.finished.get(i).getID() == 2) {
						clientWriter.println(share.finished.get(i).getResult());
						System.out.println("sent result to client");
						share.finished.remove(i);
					}
				}
			}

		}
	}

}
