package com.fbrs.server.utils;

import java.util.HashMap;
import java.util.Scanner;

import com.fbrs.server.ServerEntry;
import com.fbrs.server.matchmaking.MatchingServer;
import com.fbrs.server.matchmaking.MatchingService;


public class ServerCommands implements Runnable{

	@Override
	public void run() {

		Scanner scan = new Scanner(System.in);
		while(true)
		{
			try{
				commandlist.get(scan.nextLine()).go();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public HashMap<String, ICommand> commandlist;

	public ServerCommands()
	{
		commandlist = new HashMap<String, ICommand>();
		commandlist.put("shutdown", new ICommand(){

			@Override
			public String go(String... s) {
				ServerEntry.listening = false;
				MatchingServer.listening = false;
				return null;
			}

			@Override
			public String getCommands(String s) {
				return null;
			}

		});
		commandlist.put("killmatching", new ICommand(){

			@Override
			public String go(String... s) {
				MatchingServer.listening = false;
				return null;
			}

			@Override
			public String getCommands(String s) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		commandlist.put("playercount", new ICommand(){

			@Override
			public String go(String... s) {
				System.out.println(MatchingService.getPlayerNumber());
				return null;
			}

			@Override
			public String getCommands(String s) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
	}

}
