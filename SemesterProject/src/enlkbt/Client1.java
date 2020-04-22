package enlkbt;

import java.io.*;
import java.net.*;

public class Client1 {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 30123;

		try (Socket clientSocket = new Socket(host, port);
				PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedReader stdIn = // standard input stream to get user's requests
						new BufferedReader(new InputStreamReader(System.in))) {

			while (true) {
				System.out.println("Enter Mathematical Expression With Spaces Between the Number and Operation:");
				// READING PHASE: Read input from the user
				String request = stdIn.readLine();
				// WRITING PHASE: Send Task to Master

				requestWriter.println(request);

				// LISTENING PHASE: The Master sends the response
				String response = serverReader.readLine();
				System.out.println(response);
			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + host);
			System.exit(1);
		}
	}

}
