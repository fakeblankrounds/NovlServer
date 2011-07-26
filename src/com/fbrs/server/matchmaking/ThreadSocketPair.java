package com.fbrs.server.matchmaking;

import java.net.Socket;

public class ThreadSocketPair {
	
	public Socket socket;
	public String S1Name;
	public Socket secondSocket;
	public String S2Name;
	public MatchingThread t;
	
	public ThreadSocketPair(Socket s)
	{
		this.socket = s;
	}
	
	public void SetName(String n)
	{
		S1Name = n;
	}
	
	public void AddThread(MatchingThread t)
	{
		this.t = t;
	}
	
	public void AddSocket(Socket s, String n)
	{
		secondSocket = s;
		S2Name = n;
	}

}
