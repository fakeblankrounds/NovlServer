package com.fbrs.server;

import java.util.HashMap;

import com.fbrs.server.novl.staticweb.Friends;
import com.fbrs.server.novl.staticweb.Groups;
import com.fbrs.server.novl.staticweb.Help;
import com.fbrs.server.novl.staticweb.UserData;
import com.fbrs.server.utils.ICommand;
import com.fbrs.server.utils.UnitTests;

public class Commands {
	
	public static HashMap<String, ICommand> Command = new HashMap<String, ICommand>();
	
	private static String rootcalls = "";
	
	public static void Populate()
	{
		Commands.addCommand("Friends", new Friends());
		Commands.addCommand("Users", new UserData());
		Commands.addCommand("Test", new UnitTests());
		Commands.addCommand("Groups", new Groups());
		Commands.addCommand("help", new Help());
	}
	
	public static String getRoot()
	{
		return rootcalls;
	}
	
	private static void addCommand(String c, ICommand i)
	{
		Command.put(c,i);
		rootcalls += c + ",";
	}

}
