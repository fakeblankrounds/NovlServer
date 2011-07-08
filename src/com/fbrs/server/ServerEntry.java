package com.fbrs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fbrs.server.novl.staticweb.Help;
import com.fbrs.server.rebound.MatchThread;
import com.fbrs.server.utils.AdminConsole;

public class ServerEntry {

	static ServerSocket serverSocket;
	public static boolean listening = true;
	public static final int port = 8888;
	public static final int version = 10;

	public static boolean verbose = false;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if(args.length > 0){
			if(args[0].equals("-v") || args[0].equals("-verbose"))
				verbose = true;
			if(args[0].equals("-vs") || args[0].equals("-version"))	
			{
				System.out.println(version);
				System.exit(0);
			}
			if(args[0].equals("-debug"))	
			{
				System.out.println("can connect to s3: " + AdminConsole.s3client.doesBucketExist("NovlDataStore"));
				System.exit(0);
			}

		}
		Commands.Populate();
		Help.PackHelp();
		
		try {
			serverSocket = new ServerSocket(port);

			System.out.println("Starting Server on port " + port);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + port);
			System.exit(-1);
		}
		(new Thread(new MatchThread())).start();

		ExecutorService pool = Executors.newFixedThreadPool(10);



		while(listening)
		{
			//(new Thread(new ClientThread(serverSocket.accept()))).start();
			pool.execute(new ClientThread(serverSocket.accept()));
			System.out.println("Connect");
		}

		serverSocket.close();
	}


}
