package com.fbrs.server.matchmaking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fbrs.server.matchmaking.MatchingThread.ThreadCommand;

public class MatchingService implements Runnable{

	public static ArrayList<ThreadSocketPair> Players = new ArrayList<ThreadSocketPair>();

	public static boolean listening = true;

	public synchronized static void ArrayControl(ThreadSocketPair s, boolean runMatching)
	{
		if(!runMatching)
			MatchingService.Players.add(s);
		else
		{
			int size = Players.size();
			try{

				if(Players.size() > 1)
					System.out.println("Matching "+ Players.size() +" Players");
				for(int i = 0; (i + 1) < Players.size(); i+=2)
				{
					Players.get(i).AddSocket(Players.get(i+1).socket);
					Players.get(i).t.sendCommand(ThreadCommand.stay);
					Players.get(i+1).t.sendCommand(ThreadCommand.die);
					Players.remove(i);
					Players.remove(i);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println(size);
			}	
		}
	}

	public static int getPlayerNumber()
	{
		return Players.size(); 
	}

	@Override
	public void run() {

		while(listening){
			ArrayControl(null, true);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
}
