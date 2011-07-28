package com.fbrs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fbrs.server.matchmaking.MatchingServer;
import com.fbrs.server.novl.staticweb.Help;
import com.fbrs.server.utils.AdminConsole;
import com.fbrs.server.utils.ServerCommands;

public class ServerEntry {

	static ServerSocket serverSocket;
	public static boolean listening = true;
	public static final int port = 8888;
	public static final float version = 11.4f;

	public static boolean verbose = false;
	public static boolean unittest = false;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if(args.length > 0){
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-v") || args[i].equals("-verbose"))
					verbose = true;
				if(args[i].equals("-vs") || args[i].equals("-version"))	
				{
					System.out.println(version);
					System.exit(0);
				}
				if(args[i].equals("-debug"))	
				{
					System.out.println("can connect to s3: " + AdminConsole.s3client.doesBucketExist("NovlDataStore"));
					System.exit(0);
				}
				if(args[i].equals("-u") || args[i].equals("-enableUnitTests"))
				{
					unittest = true;
				}
			}

		}
		//setup static pages
		Commands.Populate();
		Help.PackHelp();
		
		//set up serer utils
		(new Thread(new ServerCommands())).start();
		
		try {
			serverSocket = new ServerSocket(port);

			System.out.println("Starting Server on port " + port);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + port);
			System.exit(-1);
		}
		new Thread(new MatchingServer()).start();

		ExecutorService pool = Executors.newFixedThreadPool(5);



		while(listening)
		{
			//(new Thread(new ClientThread(serverSocket.accept()))).start();
			pool.execute(new ClientThread(serverSocket.accept()));
			System.out.println("Connect");
		}
		System.out.println("Shutting down");
		serverSocket.close();
	}


}
