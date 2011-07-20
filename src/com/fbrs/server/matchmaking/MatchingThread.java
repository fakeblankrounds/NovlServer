package com.fbrs.server.matchmaking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MatchingThread implements Runnable{

	private ThreadSocketPair clientSocket = null;

	private ThreadCommand command = ThreadCommand.empty;
	
	private Object lock = new Object();

	public enum ThreadCommand{empty, die, stay};

	private boolean endGame = false;

	private BufferedReader in = null;
	private PrintWriter out = null;
	private BufferedReader in2 = null;
	private PrintWriter out2 = null;

	public MatchingThread(ThreadSocketPair socket)
	{
		this.clientSocket = socket;
		clientSocket.t = this;
		MatchingService.ArrayControl(clientSocket, false);
	}

	public void run()
	{
		try {
			synchronized (lock){
				lock.wait();
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(command == ThreadCommand.die)
			return;
		
		try{
			in = new BufferedReader(
					new InputStreamReader(
							clientSocket.socket.getInputStream()));
			out = new PrintWriter(
					clientSocket.socket.getOutputStream(), true);

			in2 = new BufferedReader(
					new InputStreamReader(
							clientSocket.secondSocket.getInputStream()));
			out2 = new PrintWriter(
					clientSocket.secondSocket.getOutputStream(), true);

			//Sync up the clients and see if we have a connection to both
			Sync();
			
		}
		catch (Exception e){
			sendReconnect(out, out2);
			e.printStackTrace();
		}
		System.out.println("Starting Game");
		while(!endGame)
		{
			try{
				if(in.ready()){	
					out2.println(in.readLine());
				}
				if(in2.ready()){
					out.println(in2.readLine());
				}
			}
			catch (Exception e){
				e.printStackTrace();
				endGame = true;
				sendReconnect(out,out2);
			}

			if(clientSocket.socket.isClosed() || clientSocket.secondSocket.isClosed() || !checkConnection(out, in) || !checkConnection(out2, in2)){
				sendReconnect(out, out2);
				endGame = true;
			}
		}
		try {
			clientSocket.secondSocket.close();
			clientSocket.socket.close();
			System.out.println("EndingGame");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendCommand(ThreadCommand t)
	{
		synchronized (lock){
		command = t;
		lock.notify();
		}
	}
	
	public void Sync() throws InterruptedException, IOException
	{
		String one = "";
		String two = "";
		long starttime = System.currentTimeMillis();
		while((!one.equals("300") && !two.equals("300")))
		{
			out.println("300");
			out2.println("300");
			Thread.sleep(100);
			if(in.ready() && !one.equals("300"))
				one = in.readLine();
			if(in2.ready() && !two.equals("300"))
				two = in.readLine();
			if((System.currentTimeMillis() - starttime) > 60000)
			{
				sendReconnect(out,out2);
			}

		}
		return;
	}

	//checks to see if a connection is already bad.
	public boolean checkConnection(PrintWriter out, BufferedReader in){
		try{
			while(true){
				out.println("@");
				if(in.ready()){
					if(in.readLine().equals("@"))
						return true;
					else
						return false;
				}
				Thread.sleep(10);
			}
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public void sendReconnect(PrintWriter out, PrintWriter out2)
	{
		try{
			out.println("888");
			clientSocket.socket.close();
		}
		catch (Exception e){
		}
		try{
			out2.println("888");
			clientSocket.secondSocket.close();
		}
		catch (Exception e){
		}
		return;
	}
}
