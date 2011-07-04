package com.fbrs.server.rebound;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MatchThread implements Runnable{


	public static Lock QueueLock = new ReentrantLock();

	public static LinkedList<Object> connectionQueue = new LinkedList<Object>();
	
	public static ArrayList<Object> Log = new ArrayList<Object>();


	public MatchThread()
	{
	}

	public void run()
	{
		while(true) {
			PrintWriter out = null;
			PrintWriter out2 = null;
			Socket sock1 = null;
			Socket sock2 = null;
			QueueLock.lock();
			while(connectionQueue.size() >= 2)
			{
				sock1 = (Socket) connectionQueue.pop();
				sock2 = (Socket) connectionQueue.pop();

				try {
					out = new PrintWriter(
							sock1.getOutputStream(), true);
					out2 = new PrintWriter(
							sock2.getOutputStream(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				out.println("Connected");
				out2.println("Connected");
				out.close();
				out2.close();

				try {

					sock1.close();
					sock2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Matched");
			}
			QueueLock.unlock();
			try {
			//	System.out.println("Nothing to match, Sleeping" + "Size_" + connectionQueue.size());
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
