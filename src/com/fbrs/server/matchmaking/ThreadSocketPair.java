package com.fbrs.server.matchmaking;

import java.net.Socket;

public class ThreadSocketPair {
	
	public Socket socket;
	public Socket secondSocket;
	public MatchingThread t;
	
	public ThreadSocketPair(Socket s)
	{
		this.socket = s;
	}
	
	public void AddThread(MatchingThread t)
	{
		this.t = t;
	}
	
	public void AddSocket(Socket s)
	{
		secondSocket = s;
	}

}
