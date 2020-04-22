package enlkbt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SlaveB {

	private static boolean available = true;

	public void setAvailabilty(boolean available) {
		this.available = available;
	}

	public boolean getAvailibilty() {
		return available;
	}

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 30325;

		try (Socket clientSocket = new Socket(host, port);
				PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());

				ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

		) {
			System.out.println("Entering Slave B");
			while (true) {
				
				Task task = (Task) is.readObject();
				if (task instanceof BTask) {
					String response = method1(task);
					requestWriter.println(response);
				} else {
					String response = method2(task);
					requestWriter.println(response);
				}

			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + host);
			System.exit(1);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

	// this method is called when slave B is available to complete an B task
	public static String method1(Task taskB) {

		String result = calculate(taskB);

		System.out.println(taskB.getTask() + " completed by Slave B's Method 1");
		available = true;

		return result;
	}

	// this method is called when slave A is unavailable to complete a A task
	public static String method2(Task taskA) {

		int sum = 0;
		for (int i = 0; i < 80000; i++) {
			sum += i;

		}
		String result = calculate(taskA);

		System.out.println(taskA.getTask() + " completed by Slave B's Method 2");
		available = true;

		return result;
	}

	public static String calculate(Task task) {
		Integer answer;
		int b;
		String x = task.getTask();
		String[] workLoadArray = x.split(" ");

		try {

			answer = Integer.parseInt(workLoadArray[0]);
		} catch (Exception e) {
			return "Invalid Operation";
		}

		for (int operator = 1; operator < workLoadArray.length; operator++) {
			int integer = operator + 1;

			if (operator == workLoadArray.length) {
				break;
			}
			try {

				b = Integer.parseInt(workLoadArray[integer]);
			} catch (Exception e) {
				return "Invalid Operation";
			}

			switch (workLoadArray[operator]) {
			case "+":
				answer += b;
				break;
			case "-":
				answer -= b;
				break;
			case "*":
				answer *= b;
				break;
			case "/":
				answer /= b;
				break;

			default:
				System.out.println("Invalid Operator ");
			}

			operator += 1;
		}
		return answer.toString();
	}

}
