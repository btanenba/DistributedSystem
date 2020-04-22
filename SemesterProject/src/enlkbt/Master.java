package enlkbt;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import java.io.*;
import java.net.ServerSocket;

public class Master {
	public static void main(String[] args) {
		ArrayList<Task> work = new ArrayList<>();

		SlaveA slaveA = new SlaveA();

		SlaveB slaveB = new SlaveB();

		Shared share = new Shared();

		int port1 = 30123; // Master to client1
		int port2 = 30532; // Master to client2
		int port3 = 30224;// Master to slaveA
		int port4 = 30325;// Master to slaveB

		try {
			ServerSocket serverSocket1 = new ServerSocket(port1);
			ServerSocket serverSocket2 = new ServerSocket(port2);
			ServerSocket serverSocket3 = new ServerSocket(port3);
			ServerSocket serverSocket4 = new ServerSocket(port4);
			while (true) {
				Socket clientSocket = serverSocket1.accept();
				ClientMasterThread1 thread1 = new ClientMasterThread1(clientSocket, share);
				thread1.start();
				Socket clientSocket2 = serverSocket2.accept();
				ClientMasterThread2 thread2 = new ClientMasterThread2(clientSocket2, share);
				thread2.start();
				Socket slaveASocket = serverSocket3.accept();
				Socket slaveBSocket = serverSocket4.accept();
				MasterSlaveAThread thread3 = new MasterSlaveAThread(slaveASocket, share);
				thread3.start();
				System.out.println("slaveA Connected");
				System.out.println("slaveB Connected");
				MasterSlaveBThread thread4 = new MasterSlaveBThread(slaveBSocket, share);
				thread4.start();
				ClientMasterThreadResponse1 thread5 = new ClientMasterThreadResponse1(clientSocket, share);
				thread5.start();
				ClientMasterThreadResponse2 thread6 = new ClientMasterThreadResponse2(clientSocket2, share);
				thread6.start();
			}
		} catch (java.net.BindException e) {
			System.out.println("Server Port/Socket address already in use! Server can't startup");
		} catch (IOException e) {
			System.out.println("Error occured when creating a new thread");
		}

	}
}
