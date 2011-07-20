package com.fbrs.server.matchmaking;

import java.util.ArrayList;

public class ConnectionMaintainer implements Runnable{
	
	public static ArrayList<ThreadSocketPair> clients;
	
	public synchronized void AddClient(ThreadSocketPair c)
	{
		clients.add(c);
	}

	@Override
	public void run() {
		
		
		
	}

}
