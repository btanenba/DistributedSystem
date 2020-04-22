package enlkbt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SlaveA {
	private static boolean available = true;

	public void setAvailabilty(boolean available) {
		this.available = available;
	}

	public boolean getAvailibilty() {
		return available;
	}

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 30224;

		try (Socket slaveSocket = new Socket(host, port);
				PrintWriter requestWriter = new PrintWriter(slaveSocket.getOutputStream(), true);
				BufferedReader serverReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));
				ObjectOutputStream os = new ObjectOutputStream(slaveSocket.getOutputStream());
				ObjectInputStream is = new ObjectInputStream(slaveSocket.getInputStream());) {
			System.out.println("slave A connected");
			while (true) {
				
				Task task = (Task) is.readObject();
				
				if (task instanceof ATask) {
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

	// this method is called when slave A is available to complete an A task
	public static String method1(Task taskA) {

		String result = calculate(taskA);

		System.out.println(taskA.getTask() + " completed by Slave A's Method 1");
		available = true;
		return result;
	}

	// this method is called when slave B is unavailable to complete a B task
	public static String method2(Task taskB) {

		int sum = 0;
		for (int i = 0; i < 80000; i++) {
			sum += i;

		}
		String result = calculate(taskB);

		System.out.println(taskB.getTask() + " completed by Slave A's Method 2");
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
				return ("Invalid Operator ");
			}

			operator += 1;
		}
		return answer.toString();
	}
}
