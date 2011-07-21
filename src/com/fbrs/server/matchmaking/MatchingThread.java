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
	
	private long last_recive1;
	private long last_recive2;


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
		String s = null;
		
		boolean inWarning = false;
		while(!endGame)
		{
			try{
				if(in.ready()){	
					s = InputProcessing.Process(in.readLine());
					if(s != null)
					out2.println("#" + s);
					out2.flush();
					last_recive1 = System.currentTimeMillis();
				}
				if(in2.ready()){
					s = InputProcessing.Process(in2.readLine());
					if(s != null)
					out.println("#" + s);
					out.flush();
					last_recive2 = System.currentTimeMillis();
				}
				
				if(((System.currentTimeMillis() - last_recive1 > 30000) || (System.currentTimeMillis() - last_recive1 > 30000)) && !inWarning)
				{
					if(((System.currentTimeMillis() - last_recive1 > 30000) || (System.currentTimeMillis() - last_recive1 > 30000)))
						sendReconnect(out,out2);
					else
						sendWarning(out, out2);
				}
				else if(inWarning)
					Sync();
				
			}
			catch (Exception e){
				e.printStackTrace();
				endGame = true;
				sendReconnect(out,out2);
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
		while(!one.equals("C300") && !two.equals("C300"))
		{
			out.println("S300");
			out2.println("S300");
			Thread.sleep(100);
			if(in.ready() && (!one.equals("C300"))){
				one = in.readLine();
			}
			if(in2.ready() && (!two.equals("C300"))){
				two = in.readLine();
			}
			if((System.currentTimeMillis() - starttime) > 60000)
			{
				sendReconnect(out,out2);
			}
		}
		//System.out.println("Sync Complete");
		last_recive1 = System.currentTimeMillis();
		last_recive2 = System.currentTimeMillis();
		return;
	}

	public void sendReconnect(PrintWriter out, PrintWriter out2)
	{
		try{
			out.println("888");
			out.flush();
			clientSocket.socket.close();
		}
		catch (Exception e){
		}
		try{
			out2.println("888");
			out.flush();
			clientSocket.secondSocket.close();
		}
		catch (Exception e){
		}
		System.out.println("SentReconnect");
		endGame = true;
		return;
	}
	
	public void sendWarning(PrintWriter out, PrintWriter out2)
	{
		try{
			out.println("555");
			out.flush();
		}
		catch (Exception e){
		}
		try{
			out2.println("555");
			out.flush();
		}
		catch (Exception e){
			sendReconnect(out,out2);
		}
		System.out.println("SentWarning");
		return;
	}
}
