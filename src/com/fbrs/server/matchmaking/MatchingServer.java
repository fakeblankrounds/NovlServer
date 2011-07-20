package com.fbrs.server.matchmaking;

import java.io.IOException;
import java.net.ServerSocket;

public class MatchingServer implements Runnable{


	public static final int port = 7777;
	private static ServerSocket serverSocket;
	public static boolean listening = true;

	public MatchingServer()
	{
		new Thread(new MatchingService()).start();
	}


	@Override
	public void run() {

		try {
			serverSocket = new ServerSocket(port);

			System.out.println("Starting Server on port " + port);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + port);
			System.exit(-1);
		}

		try{
			while(listening)
			{
				(new Thread(new MatchingThread(new ThreadSocketPair(serverSocket.accept())))).start();
				//Players.add(serverSocket.accept());
				System.out.println("Connect");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
